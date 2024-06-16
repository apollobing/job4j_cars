package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernatePostRepository implements PostRepository {

    private final CrudRepository crudRepository;

    @Override
    public Post create(Post post) {
        crudRepository.run(session -> session.persist(post));
        return post;
    }

    @Override
    public void update(Post post) {
        crudRepository.run(session -> session.merge(post));
    }

    @Override
    public void markCarAsSold(Post post) {
        crudRepository.run(
                "UPDATE Post SET sold = (sold = false) WHERE id = :fId",
                Map.of("fId", post.getId())
        );
    }

    @Override
    public void delete(Post post) {
        crudRepository.run(session -> session.remove(post));
    }

    @Override
    public List<Post> findAllOrderById() {
        return crudRepository.query(
                "FROM Post p"
                        + " JOIN FETCH p.user JOIN FETCH p.participates JOIN FETCH p.car c JOIN FETCH c.engine"
                        + " JOIN FETCH c.price JOIN FETCH c.owner JOIN FETCH c.owner.user JOIN FETCH c.carBody"
                        + " JOIN FETCH c.owners o JOIN FETCH o.user ORDER BY p.id DESC", Post.class);
    }

    @Override
    public Optional<Post> findById(int postId) {
        return crudRepository.optional(
                "FROM Post p"
                        + " JOIN FETCH p.user JOIN FETCH p.participates JOIN FETCH p.car c JOIN FETCH c.engine"
                        + " JOIN FETCH c.price JOIN FETCH c.owner JOIN FETCH c.owner.user JOIN FETCH c.carBody"
                        + " JOIN FETCH c.owners o JOIN FETCH o.user WHERE p.id = :fId", Post.class,
                Map.of("fId", postId)
        );
    }

    @Override
    public Optional<Post> findByVin(String vin) {
        return crudRepository.optional(
                "FROM Post p"
                        + " JOIN FETCH p.user JOIN FETCH p.participates JOIN FETCH p.car c JOIN FETCH c.engine"
                        + " JOIN FETCH c.price JOIN FETCH c.owner JOIN FETCH c.owner.user JOIN FETCH c.carBody"
                        + " JOIN FETCH c.owners o JOIN FETCH o.user WHERE c.vin = :fVin", Post.class,
                Map.of("fVin", vin)
        );
    }

    @Override
    public List<Post> findByName(String carName) {
        return crudRepository.query(
                "FROM Post p"
                        + " JOIN FETCH p.user JOIN FETCH p.participates JOIN FETCH p.car c JOIN FETCH c.engine"
                        + " JOIN FETCH c.price JOIN FETCH c.owner JOIN FETCH c.owner.user JOIN FETCH c.carBody"
                        + " JOIN FETCH c.owners o JOIN FETCH o.user WHERE LOWER(c.name) LIKE LOWER(:fCarName)"
                        + " ORDER BY p.id DESC", Post.class,
                Map.of("fCarName", "%" + carName + "%")
        );
    }

    @Override
    public List<Post> findNew() {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        return crudRepository.query(
                "FROM Post p"
                        + " JOIN FETCH p.user JOIN FETCH p.participates JOIN FETCH p.car c JOIN FETCH c.engine"
                        + " JOIN FETCH c.price JOIN FETCH c.owner JOIN FETCH c.owner.user JOIN FETCH c.carBody"
                        + " JOIN FETCH c.owners o JOIN FETCH o.user WHERE p.created >= :fToday"
                        + " ORDER BY p.id DESC", Post.class,
                Map.of("fToday", today)
        );
    }

    @Override
    public List<Post> findWithPhoto() {
        return crudRepository.query(
                "FROM Post p"
                        + " JOIN FETCH p.user JOIN FETCH p.participates JOIN FETCH p.car c JOIN FETCH c.engine"
                        + " JOIN FETCH c.price JOIN FETCH c.owner JOIN FETCH c.owner.user JOIN FETCH c.carBody"
                        + " JOIN FETCH c.owners o JOIN FETCH o.user WHERE p.fileId > 1 ORDER BY p.id DESC", Post.class
        );
    }
}