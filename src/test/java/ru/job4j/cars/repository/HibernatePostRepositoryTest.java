package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.MutationQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class HibernatePostRepositoryTest {

    private static SessionFactory sf;

    private static CrudRepository crudRepository;

    private final PostRepository postRepository;

    private final EngineRepository engineRepository;

    private final CarBodyRepository carBodyRepository;

    private final CarRepository carRepository;

    private final OwnerRepository ownerRepository;

    private final OwnerHistoryRepository ownerHistoryRepository;

    private final HistoryRepository historyRepository;

    private final UserRepository userRepository;

    private final FileRepository fileRepository;

    public HibernatePostRepositoryTest() {
        this.postRepository = new HibernatePostRepository(crudRepository);
        this.engineRepository = new HibernateEngineRepository(crudRepository);
        this.carBodyRepository = new HibernateCarBodyRepository(crudRepository);
        this.userRepository = new HibernateUserRepository(crudRepository);
        this.carRepository = new HibernateCarRepository(crudRepository);
        this.ownerRepository = new HibernateOwnerRepository(crudRepository);
        this.ownerHistoryRepository = new HibernateOwnerHistoryRepository(crudRepository);
        this.historyRepository = new HibernateHistoryRepository(crudRepository);
        this.fileRepository = new HibernateFileRepository(crudRepository);
    }

    @BeforeAll
    public static void init() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();

        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();

        crudRepository = new CrudRepository(sf);
    }

    @AfterEach
    public void cleanTables() {
        Session session = sf.openSession();
        Transaction txn = session.beginTransaction();
        List<String> entities = List.of("PriceHistory", "Post", "Car", "Owner", "User", "History", "File");
        for (Object entity : entities) {
            String q;
            q = entity.equals("File") ? "DELETE FROM File WHERE id > 1" : "DELETE FROM " + entity;
            MutationQuery query = session.createMutationQuery(q);
            query.executeUpdate();
        }
        txn.commit();
        session.close();
    }

    @Test
    public void whenAddNewPostThenGetThisPostFromDB() {

        User user1 = new User();
        user1.setName("User1Name");
        user1.setLogin("User1Login");
        user1.setPassword("User1Password");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("User2Name");
        user2.setLogin("User2Login");
        user2.setPassword("User2Password");
        userRepository.save(user2);

        PriceHistory priceHistory1 = new PriceHistory();
        priceHistory1.setPriceBefore(100);
        priceHistory1.setPriceAfter(200);
        priceHistory1.setCreated(LocalDateTime.now().withNano(0));

        PriceHistory priceHistory2 = new PriceHistory();
        priceHistory2.setPriceBefore(350);
        priceHistory2.setPriceAfter(420);
        priceHistory2.setCreated(LocalDateTime.now().withNano(0));

        Owner owner1 = new Owner();
        owner1.setName("John");
        owner1.setUser(user1);
        ownerRepository.create(owner1);

        Engine engine = engineRepository.findById(1).orElseThrow();

        CarBody carBody = carBodyRepository.findById(1).orElseThrow();

        Car car1 = new Car();
        car1.setName("BMW");
        car1.setVin("1q2");
        car1.setEngine(engine);
        car1.setCarBody(carBody);
        car1.setOwner(owner1);
        car1.setPrice(List.of(priceHistory1, priceHistory2));
        carRepository.create(car1);

        History history1 = new History();
        history1.setStartAt(LocalDateTime.now().withNano(0));
        history1.setEndAt(LocalDateTime.now().withNano(0));
        historyRepository.create(history1);

        OwnerHistory ownerHistory1 = new OwnerHistory();
        ownerHistory1.setOwner(owner1);
        ownerHistory1.setCar(car1);
        ownerHistory1.setHistory(history1);
        ownerHistoryRepository.create(ownerHistory1);

        Post post1 = new Post();
        post1.setDescription("Sell new BMW");
        post1.setCreated(LocalDateTime.now().withNano(0));
        post1.setSold(false);
        post1.setFileId(1);
        post1.setUser(user1);
        post1.setParticipates(Set.of(user2));
        post1.setCar(car1);
        postRepository.create(post1);

        Post result = postRepository.findById(post1.getId()).orElseThrow();
        assertThat(result.getDescription()).isEqualTo(post1.getDescription());
    }

    @Test
    public void whenUpdatePostThenGetUpdatedPostFromDB() {
        User user1 = new User();
        user1.setName("User1Name");
        user1.setLogin("User1Login");
        user1.setPassword("User1Password");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("User2Name");
        user2.setLogin("User2Login");
        user2.setPassword("User2Password");
        userRepository.save(user2);

        PriceHistory priceHistory1 = new PriceHistory();
        priceHistory1.setPriceBefore(100);
        priceHistory1.setPriceAfter(200);
        priceHistory1.setCreated(LocalDateTime.now().withNano(0));

        PriceHistory priceHistory2 = new PriceHistory();
        priceHistory2.setPriceBefore(350);
        priceHistory2.setPriceAfter(420);
        priceHistory2.setCreated(LocalDateTime.now().withNano(0));

        Owner owner1 = new Owner();
        owner1.setName("John");
        owner1.setUser(user1);
        ownerRepository.create(owner1);

        Engine engine = engineRepository.findById(1).orElseThrow();

        CarBody carBody = carBodyRepository.findById(1).orElseThrow();

        Car car1 = new Car();
        car1.setName("BMW");
        car1.setVin("1q2");
        car1.setEngine(engine);
        car1.setCarBody(carBody);
        car1.setOwner(owner1);
        car1.setPrice(List.of(priceHistory1, priceHistory2));
        carRepository.create(car1);

        History history1 = new History();
        history1.setStartAt(LocalDateTime.now().withNano(0));
        history1.setEndAt(LocalDateTime.now().withNano(0));
        historyRepository.create(history1);

        OwnerHistory ownerHistory1 = new OwnerHistory();
        ownerHistory1.setOwner(owner1);
        ownerHistory1.setCar(car1);
        ownerHistory1.setHistory(history1);
        ownerHistoryRepository.create(ownerHistory1);

        Post post1 = new Post();
        post1.setDescription("Sell new BMW");
        post1.setCreated(LocalDateTime.now().withNano(0));
        post1.setSold(false);
        post1.setFileId(1);
        post1.setUser(user1);
        post1.setParticipates(Set.of(user2));
        post1.setCar(car1);
        postRepository.create(post1);

        PriceHistory priceHistory3 = new PriceHistory();
        priceHistory3.setPriceBefore(500);
        priceHistory3.setPriceAfter(600);
        priceHistory3.setCreated(LocalDateTime.now().withNano(0));

        car1.setPrice(List.of(priceHistory1, priceHistory2, priceHistory3));

        postRepository.update(post1);

        Post result = postRepository.findById(post1.getId()).orElseThrow();
        assertThat(result.getCar().getPrice().size())
                .isEqualTo(Set.of(priceHistory1, priceHistory2, priceHistory3).size());
    }

    @Test
    public void whenDeletePostThenCanNotGetThisPostFromDB() {
        User user1 = new User();
        user1.setName("User1Name");
        user1.setLogin("User1Login");
        user1.setPassword("User1Password");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("User2Name");
        user2.setLogin("User2Login");
        user2.setPassword("User2Password");
        userRepository.save(user2);

        PriceHistory priceHistory1 = new PriceHistory();
        priceHistory1.setPriceBefore(100);
        priceHistory1.setPriceAfter(200);
        priceHistory1.setCreated(LocalDateTime.now().withNano(0));

        PriceHistory priceHistory2 = new PriceHistory();
        priceHistory2.setPriceBefore(350);
        priceHistory2.setPriceAfter(420);
        priceHistory2.setCreated(LocalDateTime.now().withNano(0));

        Owner owner1 = new Owner();
        owner1.setName("John");
        owner1.setUser(user1);
        ownerRepository.create(owner1);

        Engine engine = engineRepository.findById(1).orElseThrow();

        CarBody carBody = carBodyRepository.findById(1).orElseThrow();

        Car car1 = new Car();
        car1.setName("BMW");
        car1.setVin("1q2");
        car1.setEngine(engine);
        car1.setCarBody(carBody);
        car1.setOwner(owner1);
        car1.setPrice(List.of(priceHistory1, priceHistory2));
        carRepository.create(car1);

        History history1 = new History();
        history1.setStartAt(LocalDateTime.now().withNano(0));
        history1.setEndAt(LocalDateTime.now().withNano(0));
        historyRepository.create(history1);

        OwnerHistory ownerHistory1 = new OwnerHistory();
        ownerHistory1.setOwner(owner1);
        ownerHistory1.setCar(car1);
        ownerHistory1.setHistory(history1);
        ownerHistoryRepository.create(ownerHistory1);

        Post post1 = new Post();
        post1.setDescription("Sell new BMW");
        post1.setCreated(LocalDateTime.now().withNano(0));
        post1.setSold(false);
        post1.setFileId(1);
        post1.setUser(user1);
        post1.setParticipates(Set.of(user2));
        post1.setCar(car1);
        postRepository.create(post1);

        postRepository.delete(post1);

        Optional<Post> result = postRepository.findById(post1.getId());
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    public void whenFindAllPostsOrderedByIdThenGetThemFromDB() {
        User user1 = new User();
        user1.setName("User1Name");
        user1.setLogin("User1Login");
        user1.setPassword("User1Password");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("User2Name");
        user2.setLogin("User2Login");
        user2.setPassword("User2Password");
        userRepository.save(user2);

        PriceHistory priceHistory1 = new PriceHistory();
        priceHistory1.setPriceBefore(100);
        priceHistory1.setPriceAfter(200);
        priceHistory1.setCreated(LocalDateTime.now().withNano(0));

        PriceHistory priceHistory2 = new PriceHistory();
        priceHistory2.setPriceBefore(350);
        priceHistory2.setPriceAfter(420);
        priceHistory2.setCreated(LocalDateTime.now().withNano(0));

        Owner owner1 = new Owner();
        owner1.setName("John");
        owner1.setUser(user1);
        ownerRepository.create(owner1);

        Engine engine = engineRepository.findById(1).orElseThrow();

        CarBody carBody = carBodyRepository.findById(1).orElseThrow();

        Car car1 = new Car();
        car1.setName("BMW");
        car1.setVin("1q2");
        car1.setEngine(engine);
        car1.setCarBody(carBody);
        car1.setOwner(owner1);
        car1.setPrice(List.of(priceHistory1));
        carRepository.create(car1);

        History history1 = new History();
        history1.setStartAt(LocalDateTime.now().withNano(0));
        history1.setEndAt(LocalDateTime.now().withNano(0));
        historyRepository.create(history1);

        OwnerHistory ownerHistory1 = new OwnerHistory();
        ownerHistory1.setOwner(owner1);
        ownerHistory1.setCar(car1);
        ownerHistory1.setHistory(history1);
        ownerHistoryRepository.create(ownerHistory1);

        Owner owner2 = new Owner();
        owner2.setName("Bob");
        owner2.setUser(user2);
        ownerRepository.create(owner2);

        Car car2 = new Car();
        car2.setName("Volkswagen");
        car2.setVin("3e4");
        car2.setEngine(engine);
        car2.setCarBody(carBody);
        car2.setOwner(owner2);
        car2.setPrice(List.of(priceHistory2));
        carRepository.create(car2);

        History history2 = new History();
        history2.setStartAt(LocalDateTime.now().withNano(0));
        history2.setEndAt(LocalDateTime.now().withNano(0));
        historyRepository.create(history2);

        OwnerHistory ownerHistory2 = new OwnerHistory();
        ownerHistory2.setCar(car2);
        ownerHistory2.setOwner(owner2);
        ownerHistory2.setHistory(history2);
        ownerHistoryRepository.create(ownerHistory2);

        Post post1 = new Post();
        post1.setDescription("Sell new BMW");
        post1.setCreated(LocalDateTime.now().withNano(0));
        post1.setSold(false);
        post1.setFileId(1);
        post1.setUser(user1);
        post1.setParticipates(Set.of(user2));
        post1.setCar(car1);
        postRepository.create(post1);

        Post post2 = new Post();
        post2.setDescription("Sell new Volkswagen");
        post2.setCreated(LocalDateTime.now().withNano(0));
        post2.setSold(false);
        post2.setFileId(1);
        post2.setUser(user2);
        post2.setParticipates(Set.of(user1));
        post2.setCar(car2);
        postRepository.create(post2);

        Collection<Post> result = postRepository.findAllOrderById();
        assertThat(result).containsExactlyElementsOf(List.of(post2, post1));
    }

    @Test
    public void whenFindPostsByNameThenGetThemFromDB() {
        User user1 = new User();
        user1.setName("User1Name");
        user1.setLogin("User1Login");
        user1.setPassword("User1Password");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("User2Name");
        user2.setLogin("User2Login");
        user2.setPassword("User2Password");
        userRepository.save(user2);

        PriceHistory priceHistory1 = new PriceHistory();
        priceHistory1.setPriceBefore(100);
        priceHistory1.setPriceAfter(200);
        priceHistory1.setCreated(LocalDateTime.now().withNano(0));

        PriceHistory priceHistory2 = new PriceHistory();
        priceHistory2.setPriceBefore(350);
        priceHistory2.setPriceAfter(420);
        priceHistory2.setCreated(LocalDateTime.now().withNano(0));

        Owner owner1 = new Owner();
        owner1.setName("John");
        owner1.setUser(user1);
        ownerRepository.create(owner1);

        Engine engine = engineRepository.findById(1).orElseThrow();

        CarBody carBody = carBodyRepository.findById(1).orElseThrow();

        Car car1 = new Car();
        car1.setName("BMW X5");
        car1.setVin("1q2");
        car1.setEngine(engine);
        car1.setCarBody(carBody);
        car1.setOwner(owner1);
        car1.setPrice(List.of(priceHistory1));
        carRepository.create(car1);

        History history1 = new History();
        history1.setStartAt(LocalDateTime.now().withNano(0));
        history1.setEndAt(LocalDateTime.now().withNano(0));
        historyRepository.create(history1);

        OwnerHistory ownerHistory1 = new OwnerHistory();
        ownerHistory1.setOwner(owner1);
        ownerHistory1.setCar(car1);
        ownerHistory1.setHistory(history1);
        ownerHistoryRepository.create(ownerHistory1);

        Owner owner2 = new Owner();
        owner2.setName("Bob");
        owner2.setUser(user2);
        ownerRepository.create(owner2);

        Car car2 = new Car();
        car2.setName("BMW X3");
        car2.setVin("3e4");
        car2.setEngine(engine);
        car2.setCarBody(carBody);
        car2.setOwner(owner2);
        car2.setPrice(List.of(priceHistory2));
        carRepository.create(car2);

        History history2 = new History();
        history2.setStartAt(LocalDateTime.now().withNano(0));
        history2.setEndAt(LocalDateTime.now().withNano(0));
        historyRepository.create(history2);

        OwnerHistory ownerHistory2 = new OwnerHistory();
        ownerHistory2.setCar(car2);
        ownerHistory2.setOwner(owner2);
        ownerHistory2.setHistory(history2);
        ownerHistoryRepository.create(ownerHistory2);

        Post post1 = new Post();
        post1.setDescription("Sell new BMW X5");
        post1.setCreated(LocalDateTime.now().withNano(0));
        post1.setSold(false);
        post1.setFileId(1);
        post1.setUser(user1);
        post1.setParticipates(Set.of(user2));
        post1.setCar(car1);
        postRepository.create(post1);

        Post post2 = new Post();
        post2.setDescription("Sell new BMW X3");
        post2.setCreated(LocalDateTime.now().withNano(0));
        post2.setSold(false);
        post2.setFileId(1);
        post2.setUser(user2);
        post2.setParticipates(Set.of(user1));
        post2.setCar(car2);
        postRepository.create(post2);

        Collection<Post> result = postRepository.findByName("bmw");
        assertThat(result).containsExactlyElementsOf(List.of(post2, post1));
    }

    @Test
    public void whenFindNewPostsThenGetThemFromDB() {
        User user1 = new User();
        user1.setName("User1Name");
        user1.setLogin("User1Login");
        user1.setPassword("User1Password");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("User2Name");
        user2.setLogin("User2Login");
        user2.setPassword("User2Password");
        userRepository.save(user2);

        PriceHistory priceHistory1 = new PriceHistory();
        priceHistory1.setPriceBefore(100);
        priceHistory1.setPriceAfter(200);
        priceHistory1.setCreated(LocalDateTime.now().withNano(0));

        PriceHistory priceHistory2 = new PriceHistory();
        priceHistory2.setPriceBefore(350);
        priceHistory2.setPriceAfter(420);
        priceHistory2.setCreated(LocalDateTime.now().withNano(0));

        Owner owner1 = new Owner();
        owner1.setName("John");
        owner1.setUser(user1);
        ownerRepository.create(owner1);

        Engine engine = engineRepository.findById(1).orElseThrow();

        CarBody carBody = carBodyRepository.findById(1).orElseThrow();

        Car car1 = new Car();
        car1.setName("BMW");
        car1.setVin("1q2");
        car1.setEngine(engine);
        car1.setCarBody(carBody);
        car1.setOwner(owner1);
        car1.setPrice(List.of(priceHistory1));
        carRepository.create(car1);

        History history1 = new History();
        history1.setStartAt(LocalDateTime.now().withNano(0));
        history1.setEndAt(LocalDateTime.now().withNano(0));
        historyRepository.create(history1);

        OwnerHistory ownerHistory1 = new OwnerHistory();
        ownerHistory1.setOwner(owner1);
        ownerHistory1.setCar(car1);
        ownerHistory1.setHistory(history1);
        ownerHistoryRepository.create(ownerHistory1);

        Owner owner2 = new Owner();
        owner2.setName("Bob");
        owner2.setUser(user2);
        ownerRepository.create(owner2);

        Car car2 = new Car();
        car2.setName("Volkswagen");
        car2.setVin("3e4");
        car2.setEngine(engine);
        car2.setCarBody(carBody);
        car2.setOwner(owner2);
        car2.setPrice(List.of(priceHistory2));
        carRepository.create(car2);

        History history2 = new History();
        history2.setStartAt(LocalDateTime.now().withNano(0));
        history2.setEndAt(LocalDateTime.now().withNano(0));
        historyRepository.create(history2);

        OwnerHistory ownerHistory2 = new OwnerHistory();
        ownerHistory2.setCar(car2);
        ownerHistory2.setOwner(owner2);
        ownerHistory2.setHistory(history2);
        ownerHistoryRepository.create(ownerHistory2);

        Post post1 = new Post();
        post1.setDescription("Sell new BMW");
        post1.setCreated(LocalDateTime.now().withNano(0));
        post1.setSold(false);
        post1.setFileId(1);
        post1.setUser(user1);
        post1.setParticipates(Set.of(user2));
        post1.setCar(car1);
        postRepository.create(post1);

        Post post2 = new Post();
        post2.setDescription("Sell new Volkswagen");
        post2.setCreated(LocalDateTime.of(2000, 1, 1, 0, 0));
        post2.setSold(false);
        post2.setFileId(1);
        post2.setUser(user2);
        post2.setParticipates(Set.of(user1));
        post2.setCar(car2);
        postRepository.create(post2);

        Collection<Post> result = postRepository.findNew();
        assertThat(result).containsExactlyElementsOf(List.of(post1));
    }

    @Test
    public void whenFindPostsWithPhotoThenGetThemFromDB() {
        User user1 = new User();
        user1.setName("User1Name");
        user1.setLogin("User1Login");
        user1.setPassword("User1Password");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("User2Name");
        user2.setLogin("User2Login");
        user2.setPassword("User2Password");
        userRepository.save(user2);

        PriceHistory priceHistory1 = new PriceHistory();
        priceHistory1.setPriceBefore(100);
        priceHistory1.setPriceAfter(200);
        priceHistory1.setCreated(LocalDateTime.now().withNano(0));

        PriceHistory priceHistory2 = new PriceHistory();
        priceHistory2.setPriceBefore(350);
        priceHistory2.setPriceAfter(420);
        priceHistory2.setCreated(LocalDateTime.now().withNano(0));

        Owner owner1 = new Owner();
        owner1.setName("John");
        owner1.setUser(user1);
        ownerRepository.create(owner1);

        Engine engine = engineRepository.findById(1).orElseThrow();
        CarBody carBody = carBodyRepository.findById(1).orElseThrow();

        Car car1 = new Car();
        car1.setName("BMW");
        car1.setVin("1q2");
        car1.setEngine(engine);
        car1.setCarBody(carBody);
        car1.setOwner(owner1);
        car1.setPrice(List.of(priceHistory1));
        carRepository.create(car1);

        History history1 = new History();
        history1.setStartAt(LocalDateTime.now().withNano(0));
        history1.setEndAt(LocalDateTime.now().withNano(0));
        historyRepository.create(history1);

        OwnerHistory ownerHistory1 = new OwnerHistory();
        ownerHistory1.setOwner(owner1);
        ownerHistory1.setCar(car1);
        ownerHistory1.setHistory(history1);
        ownerHistoryRepository.create(ownerHistory1);

        Owner owner2 = new Owner();
        owner2.setName("Bob");
        owner2.setUser(user2);
        ownerRepository.create(owner2);

        Car car2 = new Car();
        car2.setName("Volkswagen");
        car2.setVin("3e4");
        car2.setEngine(engine);
        car2.setCarBody(carBody);
        car2.setOwner(owner2);
        car2.setPrice(List.of(priceHistory2));
        carRepository.create(car2);

        History history2 = new History();
        history2.setStartAt(LocalDateTime.now().withNano(0));
        history2.setEndAt(LocalDateTime.now().withNano(0));
        historyRepository.create(history2);

        OwnerHistory ownerHistory2 = new OwnerHistory();
        ownerHistory2.setCar(car2);
        ownerHistory2.setOwner(owner2);
        ownerHistory2.setHistory(history2);
        ownerHistoryRepository.create(ownerHistory2);

        Post post1 = new Post();
        post1.setDescription("Sell new BMW");
        post1.setCreated(LocalDateTime.now().withNano(0));
        post1.setFileId(1);
        post1.setUser(user1);
        post1.setParticipates(Set.of(user2));
        post1.setCar(car1);
        postRepository.create(post1);

        File newFile = new File("file2Name", "file2.png");
        fileRepository.save(newFile);

        Post post2 = new Post();
        post2.setDescription("Sell new Volkswagen");
        post2.setCreated(LocalDateTime.now());
        post2.setFileId(newFile.getId());
        post2.setUser(user2);
        post2.setParticipates(Set.of(user1));
        post2.setCar(car2);
        postRepository.create(post2);

        Collection<Post> result = postRepository.findWithPhoto();
        assertThat(result).containsExactlyElementsOf(List.of(post2));
    }
}