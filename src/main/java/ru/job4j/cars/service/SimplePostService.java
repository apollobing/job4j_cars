package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SimplePostService implements PostService {

    private final PostRepository postRepository;

    private final EngineRepository engineRepository;

    private final CarBodyRepository carBodyRepository;

    private final CarRepository carRepository;

    private final OwnerRepository ownerRepository;

    private final OwnerHistoryRepository ownerHistoryRepository;

    private final HistoryRepository historyRepository;

    private final FileService fileService;

    public SimplePostService(PostRepository postRepository, EngineRepository engineRepository,
                             CarBodyRepository carBodyRepository, FileService fileService, CarRepository carRepository,
                             OwnerRepository ownerRepository, OwnerHistoryRepository ownerHistoryRepository,
                             HistoryRepository historyRepository) {
        this.postRepository = postRepository;
        this.engineRepository = engineRepository;
        this.carBodyRepository = carBodyRepository;
        this.fileService = fileService;
        this.carRepository = carRepository;
        this.ownerRepository = ownerRepository;
        this.ownerHistoryRepository = ownerHistoryRepository;
        this.historyRepository = historyRepository;
    }

    @Override
    public Post create(PostDto postDto, FileDto image) {
        Optional<Owner> ownerOptional = ownerRepository.findByUserId(postDto.getUser().getId());
        Owner owner;

        if (ownerOptional.isEmpty()) {
            owner = new Owner();
            owner.setName(postDto.getUser().getName());
            owner.setUser(postDto.getUser());
            ownerRepository.create(owner);
        } else {
            owner = ownerOptional.get();
        }

        History history = new History();
        history.setStartAt(LocalDateTime.of(postDto.getOwnershipYear(), 1, 1, 0, 0));
        history.setEndAt(LocalDateTime.now().withNano(0));
        historyRepository.create(history);

        Optional<Car> carByVin = carRepository.findByVin(postDto.getVin());
        Car car;

        if (carByVin.isEmpty()) {
            PriceHistory priceHistory = new PriceHistory();
            priceHistory.setPriceBefore(0);
            priceHistory.setPriceAfter(postDto.getPrice());
            priceHistory.setCreated(LocalDateTime.now().withNano(0));

            car = new Car();
            car.setName(postDto.getName());
            car.setVin(postDto.getVin());
            car.setEngine(engineRepository.findById(postDto.getEngineId()).orElseThrow());
            car.setPrice(List.of(priceHistory));
            car.setCarBody(carBodyRepository.findById(postDto.getCarBodyId()).orElseThrow());
            car.setOwner(owner);
            carRepository.create(car);
        } else {
            car = carByVin.get();
            car.setOwner(owner);
            car.getPrice().add(getPriceHistory(postDto, car));
            carRepository.update(car);
            postRepository.findByVin(postDto.getVin()).ifPresent(postRepository::delete);
        }

        OwnerHistory ownerHistory = new OwnerHistory();
        ownerHistory.setCar(car);
        ownerHistory.setOwner(owner);
        ownerHistory.setHistory(history);
        ownerHistoryRepository.create(ownerHistory);

        Post post = new Post();
        post.setDescription(postDto.getDescription());
        post.setCreated(postDto.getCreated());
        post.setUser(postDto.getUser());
        post.setCar(car);
        post.setParticipates(Set.of(postDto.getUser()));

        if (image.getContent().length > 0) {
            saveNewFile(post, image);
        } else {
            post.setFileId(1);
        }

        postRepository.create(post);

        return post;
    }

    @Override
    public void update(PostDto postDto, FileDto image) {
        Post post = findById(postDto.getId()).orElseThrow();

        Car car = post.getCar();
        car.setName(postDto.getName());
        car.setVin(postDto.getVin());
        car.setEngine(engineRepository.findById(postDto.getEngineId()).orElseThrow());
        car.setCarBody(carBodyRepository.findById(postDto.getCarBodyId()).orElseThrow());

        if (postDto.getPrice() != car.getPrice().get(car.getPrice().size() - 1).getPriceAfter()) {
            car.getPrice().add(getPriceHistory(postDto, car));
        }

        OwnerHistory ownerHistory =
                ownerHistoryRepository.findByCarIdAndOwnerId(car.getId(), car.getOwner().getId()).orElseThrow();
        History history = ownerHistory.getHistory();
        history.setStartAt(LocalDateTime.of(postDto.getOwnershipYear(), 1, 1, 0, 0));
        historyRepository.update(history);

        post.setDescription(postDto.getDescription());

        boolean isNewFileEmpty = image.getContent().length == 0;
        if (isNewFileEmpty) {
            postRepository.update(post);
        } else {
            int oldFileId = post.getFileId();
            saveNewFile(post, image);
            postRepository.update(post);
            fileService.deleteById(oldFileId);
        }
    }

    @Override
    public void markCarAsSold(Post post) {
        postRepository.markCarAsSold(post);

        Car car = post.getCar();
        OwnerHistory ownerHistory =
                ownerHistoryRepository.findByCarIdAndOwnerId(car.getId(), car.getOwner().getId()).orElseThrow();
        History history = ownerHistory.getHistory();
        history.setEndAt(LocalDateTime.now().withNano(0));
        historyRepository.update(history);
    }

    @Override
    public void delete(Post post) {
        postRepository.delete(post);

        int fileId = post.getFileId();
        if (fileId > 1) {
            fileService.deleteById(fileId);
        }
    }

    @Override
    public Collection<Post> findAllOrderById() {
        return postRepository.findAllOrderById();
    }

    @Override
    public Optional<Post> findById(int postId) {
        return postRepository.findById(postId);
    }

    @Override
    public Optional<Post> findByVin(String vin) {
        return postRepository.findByVin(vin);
    }

    @Override
    public Collection<Post> findByName(String postName) {
        return postRepository.findByName(postName);
    }

    @Override
    public Collection<Post> findNew() {
        return postRepository.findNew();
    }

    @Override
    public Collection<Post> findWithPhoto() {
        return postRepository.findWithPhoto();
    }

    @Override
    public void follow(Post post, User user) {
        post.getParticipates().add(user);
        postRepository.update(post);
    }

    private void saveNewFile(Post post, FileDto image) {
        File file = fileService.save(image);
        post.setFileId(file.getId());
    }

    private PriceHistory getPriceHistory(PostDto postDto, Car car) {
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPriceBefore(car.getPrice().get(car.getPrice().size() - 1).getPriceAfter());
        priceHistory.setPriceAfter(postDto.getPrice());
        priceHistory.setCreated(LocalDateTime.now().withNano(0));
        return priceHistory;
    }
}
