package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PostRepositoryTest {

    private CrudRepository crudRepository;

    private SessionFactory sf;

    @BeforeEach
    public void init() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();

        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();

        crudRepository = new CrudRepository(sf);
    }

    @Test
    public void whenAddNewPostThenGetThisPostFromDB() {
        PostRepository postRepository = new PostRepository(crudRepository);

        User user1 = new User();
        user1.setLogin("User1Login");
        user1.setPassword("User1Password");
        create(user1, sf);

        User user2 = new User();
        user2.setLogin("User2Login");
        user2.setPassword("User2Password");
        create(user2, sf);

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
        create(owner1, sf);

        Engine engine = new Engine();
        engine.setName("V8");
        create(engine, sf);

        Car car1 = new Car();
        car1.setName("BMW");
        car1.setEngine(engine);
        car1.setOwner(owner1);
        create(car1, sf);

        History history1 = new History();
        history1.setStartAt(LocalDateTime.now().withNano(0));
        history1.setEndAt(LocalDateTime.now().withNano(0));
        create(history1, sf);

        OwnerHistory ownerHistory1 = new OwnerHistory();
        ownerHistory1.setCar(car1);
        ownerHistory1.setOwner(owner1);
        ownerHistory1.setHistory(history1);
        create(ownerHistory1, sf);

        Post post1 = new Post();
        post1.setName("BMW");
        post1.setDescription("Sell new BMW");
        post1.setFileId(1);
        post1.setCreated(LocalDateTime.now().withNano(0));
        post1.setUser(user1);
        post1.setPrice(Set.of(priceHistory1, priceHistory2));
        post1.setParticipates(Set.of(user2));
        post1.setCar(car1);
        postRepository.create(post1);

        Post result = postRepository.findById(post1.getId()).orElseThrow();
        assertThat(result.getDescription()).isEqualTo(post1.getDescription());
    }

    @Test
    public void whenUpdatePostThenGetUpdatedPostFromDB() {
        PostRepository postRepository = new PostRepository(crudRepository);

        User user1 = new User();
        user1.setLogin("User1Login");
        user1.setPassword("User1Password");
        create(user1, sf);

        User user2 = new User();
        user2.setLogin("User2Login");
        user2.setPassword("User2Password");
        create(user2, sf);

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
        create(owner1, sf);

        Engine engine = new Engine();
        engine.setName("V8");
        create(engine, sf);

        Car car1 = new Car();
        car1.setName("BMW");
        car1.setEngine(engine);
        car1.setOwner(owner1);
        create(car1, sf);

        History history1 = new History();
        history1.setStartAt(LocalDateTime.now().withNano(0));
        history1.setEndAt(LocalDateTime.now().withNano(0));
        create(history1, sf);

        OwnerHistory ownerHistory1 = new OwnerHistory();
        ownerHistory1.setCar(car1);
        ownerHistory1.setOwner(owner1);
        ownerHistory1.setHistory(history1);
        create(ownerHistory1, sf);

        Post post1 = new Post();
        post1.setName("BMW");
        post1.setDescription("Sell new BMW");
        post1.setFileId(1);
        post1.setCreated(LocalDateTime.now().withNano(0));
        post1.setUser(user1);
        post1.setPrice(Set.of(priceHistory1, priceHistory2));
        post1.setParticipates(Set.of(user2));
        post1.setCar(car1);
        postRepository.create(post1);

        PriceHistory priceHistory3 = new PriceHistory();
        priceHistory3.setPriceBefore(500);
        priceHistory3.setPriceAfter(600);
        priceHistory3.setCreated(LocalDateTime.now().withNano(0));

        post1.setPrice(Set.of(priceHistory1, priceHistory2, priceHistory3));

        postRepository.update(post1);

        Post result = postRepository.findById(post1.getId()).orElseThrow();
        assertThat(result.getPrice().size()).isEqualTo(Set.of(priceHistory1, priceHistory2, priceHistory3).size());
    }

    @Test
    public void whenDeletePostThenCanNotGetThisPostFromDB() {
        PostRepository postRepository = new PostRepository(crudRepository);

        User user1 = new User();
        user1.setLogin("User1Login");
        user1.setPassword("User1Password");
        create(user1, sf);

        User user2 = new User();
        user2.setLogin("User2Login");
        user2.setPassword("User2Password");
        create(user2, sf);

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
        create(owner1, sf);

        Engine engine = new Engine();
        engine.setName("V8");
        create(engine, sf);

        Car car1 = new Car();
        car1.setName("BMW");
        car1.setEngine(engine);
        car1.setOwner(owner1);
        create(car1, sf);

        History history1 = new History();
        history1.setStartAt(LocalDateTime.now().withNano(0));
        history1.setEndAt(LocalDateTime.now().withNano(0));
        create(history1, sf);

        OwnerHistory ownerHistory1 = new OwnerHistory();
        ownerHistory1.setCar(car1);
        ownerHistory1.setOwner(owner1);
        ownerHistory1.setHistory(history1);
        create(ownerHistory1, sf);

        Post post1 = new Post();
        post1.setName("BMW");
        post1.setDescription("Sell new BMW");
        post1.setFileId(1);
        post1.setCreated(LocalDateTime.now().withNano(0));
        post1.setUser(user1);
        post1.setPrice(Set.of(priceHistory1, priceHistory2));
        post1.setParticipates(Set.of(user2));
        post1.setCar(car1);
        postRepository.create(post1);

        postRepository.delete(post1);

        Optional<Post> result = postRepository.findById(post1.getId());
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    public void whenFindAllPostsOrderedByIdThenGetThemFromDB() {
        PostRepository postRepository = new PostRepository(crudRepository);

        User user1 = new User();
        user1.setLogin("User1Login");
        user1.setPassword("User1Password");
        create(user1, sf);

        User user2 = new User();
        user2.setLogin("User2Login");
        user2.setPassword("User2Password");
        create(user2, sf);

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
        create(owner1, sf);

        Engine engine = new Engine();
        engine.setName("V8");
        create(engine, sf);

        Car car1 = new Car();
        car1.setName("BMW");
        car1.setEngine(engine);
        car1.setOwner(owner1);
        create(car1, sf);

        History history1 = new History();
        history1.setStartAt(LocalDateTime.now().withNano(0));
        history1.setEndAt(LocalDateTime.now().withNano(0));
        create(history1, sf);

        OwnerHistory ownerHistory1 = new OwnerHistory();
        ownerHistory1.setCar(car1);
        ownerHistory1.setOwner(owner1);
        ownerHistory1.setHistory(history1);
        create(ownerHistory1, sf);

        Owner owner2 = new Owner();
        owner2.setName("Bob");
        owner2.setUser(user2);
        create(owner2, sf);

        Car car2 = new Car();
        car2.setName("Volkswagen");
        car2.setEngine(engine);
        car2.setOwner(owner2);
        create(car2, sf);

        History history2 = new History();
        history2.setStartAt(LocalDateTime.now().withNano(0));
        history2.setEndAt(LocalDateTime.now().withNano(0));
        create(history2, sf);

        OwnerHistory ownerHistory2 = new OwnerHistory();
        ownerHistory2.setCar(car2);
        ownerHistory2.setOwner(owner2);
        ownerHistory2.setHistory(history2);
        create(ownerHistory2, sf);

        Post post1 = new Post();
        post1.setName("BMW");
        post1.setDescription("Sell new BMW");
        post1.setFileId(1);
        post1.setCreated(LocalDateTime.now().withNano(0));
        post1.setUser(user1);
        post1.setPrice(Set.of(priceHistory1));
        post1.setParticipates(Set.of(user2));
        post1.setCar(car1);
        postRepository.create(post1);

        Post post2 = new Post();
        post2.setName("Volkswagen");
        post2.setDescription("Sell new Volkswagen");
        post2.setCreated(LocalDateTime.now().withNano(0));
        post2.setUser(user2);
        post2.setPrice(Set.of(priceHistory2));
        post2.setParticipates(Set.of(user1));
        post2.setCar(car2);
        postRepository.create(post2);

        Collection<Post> result = postRepository.findAllOrderById();
        assertThat(result).containsExactlyElementsOf(List.of(post2, post1));
    }

    @Test
    public void whenFindPostsByNameThenGetThemFromDB() {
        PostRepository postRepository = new PostRepository(crudRepository);

        User user1 = new User();
        user1.setLogin("User1Login");
        user1.setPassword("User1Password");
        create(user1, sf);

        User user2 = new User();
        user2.setLogin("User2Login");
        user2.setPassword("User2Password");
        create(user2, sf);

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
        create(owner1, sf);

        Engine engine = new Engine();
        engine.setName("V8");
        create(engine, sf);

        Car car1 = new Car();
        car1.setName("BMW");
        car1.setEngine(engine);
        car1.setOwner(owner1);
        create(car1, sf);

        History history1 = new History();
        history1.setStartAt(LocalDateTime.now().withNano(0));
        history1.setEndAt(LocalDateTime.now().withNano(0));
        create(history1, sf);

        OwnerHistory ownerHistory1 = new OwnerHistory();
        ownerHistory1.setCar(car1);
        ownerHistory1.setOwner(owner1);
        ownerHistory1.setHistory(history1);
        create(ownerHistory1, sf);

        Owner owner2 = new Owner();
        owner2.setName("Bob");
        owner2.setUser(user2);
        create(owner2, sf);

        Car car2 = new Car();
        car2.setName("Volkswagen");
        car2.setEngine(engine);
        car2.setOwner(owner2);
        create(car2, sf);

        History history2 = new History();
        history2.setStartAt(LocalDateTime.now().withNano(0));
        history2.setEndAt(LocalDateTime.now().withNano(0));
        create(history2, sf);

        OwnerHistory ownerHistory2 = new OwnerHistory();
        ownerHistory2.setCar(car2);
        ownerHistory2.setOwner(owner2);
        ownerHistory2.setHistory(history2);
        create(ownerHistory2, sf);

        Post post1 = new Post();
        post1.setName("BMW X5");
        post1.setDescription("Sell new BMW");
        post1.setFileId(1);
        post1.setCreated(LocalDateTime.now().withNano(0));
        post1.setUser(user1);
        post1.setPrice(Set.of(priceHistory1));
        post1.setParticipates(Set.of(user2));
        post1.setCar(car1);
        postRepository.create(post1);

        Post post2 = new Post();
        post2.setName("BMW X5");
        post2.setDescription("Sell new BMW");
        post2.setCreated(LocalDateTime.now().withNano(0));
        post2.setUser(user2);
        post2.setPrice(Set.of(priceHistory2));
        post2.setParticipates(Set.of(user1));
        post2.setCar(car2);
        postRepository.create(post2);

        Collection<Post> result = postRepository.findByName("BMW X5");
        assertThat(result).containsExactlyElementsOf(List.of(post2, post1));
    }

    @Test
    public void whenFindNewPostsThenGetThemFromDB() {
        PostRepository postRepository = new PostRepository(crudRepository);

        User user1 = new User();
        user1.setLogin("User1Login");
        user1.setPassword("User1Password");
        create(user1, sf);

        User user2 = new User();
        user2.setLogin("User2Login");
        user2.setPassword("User2Password");
        create(user2, sf);

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
        create(owner1, sf);

        Engine engine = new Engine();
        engine.setName("V8");
        create(engine, sf);

        Car car1 = new Car();
        car1.setName("BMW");
        car1.setEngine(engine);
        car1.setOwner(owner1);
        create(car1, sf);

        History history1 = new History();
        history1.setStartAt(LocalDateTime.now().withNano(0));
        history1.setEndAt(LocalDateTime.now().withNano(0));
        create(history1, sf);

        OwnerHistory ownerHistory1 = new OwnerHistory();
        ownerHistory1.setCar(car1);
        ownerHistory1.setOwner(owner1);
        ownerHistory1.setHistory(history1);
        create(ownerHistory1, sf);

        Owner owner2 = new Owner();
        owner2.setName("Bob");
        owner2.setUser(user2);
        create(owner2, sf);

        Car car2 = new Car();
        car2.setName("Volkswagen");
        car2.setEngine(engine);
        car2.setOwner(owner2);
        create(car2, sf);

        History history2 = new History();
        history2.setStartAt(LocalDateTime.now().withNano(0));
        history2.setEndAt(LocalDateTime.now().withNano(0));
        create(history2, sf);

        OwnerHistory ownerHistory2 = new OwnerHistory();
        ownerHistory2.setCar(car2);
        ownerHistory2.setOwner(owner2);
        ownerHistory2.setHistory(history2);
        create(ownerHistory2, sf);

        Post post1 = new Post();
        post1.setName("BMW X5");
        post1.setDescription("Sell new BMW");
        post1.setFileId(1);
        post1.setCreated(LocalDateTime.now().withNano(0));
        post1.setUser(user1);
        post1.setPrice(Set.of(priceHistory1));
        post1.setParticipates(Set.of(user2));
        post1.setCar(car1);
        postRepository.create(post1);

        Post post2 = new Post();
        post2.setName("Volkswagen");
        post2.setDescription("Sell new Volkswagen");
        post2.setCreated(LocalDateTime.of(2000, 1, 1, 0, 0));
        post2.setUser(user2);
        post2.setPrice(Set.of(priceHistory2));
        post2.setParticipates(Set.of(user1));
        post2.setCar(car2);
        postRepository.create(post2);

        Collection<Post> result = postRepository.findNew();
        assertThat(result).containsExactlyElementsOf(List.of(post1));
    }

    @Test
    public void whenFindPostsWithPhotoThenGetThemFromDB() {
        PostRepository postRepository = new PostRepository(crudRepository);

        User user1 = new User();
        user1.setLogin("User1Login");
        user1.setPassword("User1Password");
        create(user1, sf);

        User user2 = new User();
        user2.setLogin("User2Login");
        user2.setPassword("User2Password");
        create(user2, sf);

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
        create(owner1, sf);

        Engine engine = new Engine();
        engine.setName("V8");
        create(engine, sf);

        Car car1 = new Car();
        car1.setName("BMW");
        car1.setEngine(engine);
        car1.setOwner(owner1);
        create(car1, sf);

        History history1 = new History();
        history1.setStartAt(LocalDateTime.now().withNano(0));
        history1.setEndAt(LocalDateTime.now().withNano(0));
        create(history1, sf);

        OwnerHistory ownerHistory1 = new OwnerHistory();
        ownerHistory1.setCar(car1);
        ownerHistory1.setOwner(owner1);
        ownerHistory1.setHistory(history1);
        create(ownerHistory1, sf);

        Owner owner2 = new Owner();
        owner2.setName("Bob");
        owner2.setUser(user2);
        create(owner2, sf);

        Car car2 = new Car();
        car2.setName("Volkswagen");
        car2.setEngine(engine);
        car2.setOwner(owner2);
        create(car2, sf);

        History history2 = new History();
        history2.setStartAt(LocalDateTime.now().withNano(0));
        history2.setEndAt(LocalDateTime.now().withNano(0));
        create(history2, sf);

        OwnerHistory ownerHistory2 = new OwnerHistory();
        ownerHistory2.setCar(car2);
        ownerHistory2.setOwner(owner2);
        ownerHistory2.setHistory(history2);
        create(ownerHistory2, sf);

        Post post1 = new Post();
        post1.setName("BMW X5");
        post1.setDescription("Sell new BMW");
        post1.setCreated(LocalDateTime.now().withNano(0));
        post1.setUser(user1);
        post1.setPrice(Set.of(priceHistory1));
        post1.setParticipates(Set.of(user2));
        post1.setCar(car1);
        postRepository.create(post1);

        Post post2 = new Post();
        post2.setName("Volkswagen");
        post2.setDescription("Sell new Volkswagen");
        post2.setFileId(1);
        post2.setCreated(LocalDateTime.now().withNano(0));
        post2.setUser(user2);
        post2.setPrice(Set.of(priceHistory2));
        post2.setParticipates(Set.of(user1));
        post2.setCar(car2);
        postRepository.create(post2);

        Collection<Post> result = postRepository.findWithPhoto();
        assertThat(result).containsExactlyElementsOf(List.of(post2));
    }

    private <T> void create(T model, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.persist(model);
        session.getTransaction().commit();
        session.close();
    }
}