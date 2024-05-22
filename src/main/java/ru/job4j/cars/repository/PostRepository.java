package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PostRepository {

    private final CrudRepository crudRepository;

    public Post create(Post post) {
        crudRepository.run(session -> session.persist(post));
        return post;
    }

    public void update(Post post) {
        crudRepository.run(session -> session.merge(post));
    }

    public void delete(Post post) {
        crudRepository.run(session -> session.remove(post));
    }

    public Collection<Post> findAllOrderById() {
        return crudRepository.query(
                "FROM Post p"
                        + " JOIN FETCH p.user JOIN FETCH p.price JOIN FETCH p.participates JOIN FETCH p.car c"
                        + " JOIN FETCH c.engine JOIN FETCH c.owner JOIN FETCH c.owner.user JOIN FETCH c.owners o"
                        + " JOIN FETCH o.user ORDER BY p.id DESC", Post.class);
    }

    public Optional<Post> findById(int postId) {
        return crudRepository.optional(
                "FROM Post p"
                        + " JOIN FETCH p.user JOIN FETCH p.price JOIN FETCH p.participates JOIN FETCH p.car c"
                        + " JOIN FETCH c.engine JOIN FETCH c.owner JOIN FETCH c.owner.user JOIN FETCH c.owners o"
                        + " JOIN FETCH o.user WHERE p.id = :fId", Post.class,
                Map.of("fId", postId)
        );
    }

    public Collection<Post> findByName(String postName) {
        return crudRepository.query(
                "FROM Post p"
                        + " JOIN FETCH p.user JOIN FETCH p.price JOIN FETCH p.participates JOIN FETCH p.car c"
                        + " JOIN FETCH c.engine JOIN FETCH c.owner JOIN FETCH c.owner.user JOIN FETCH c.owners o"
                        + " JOIN FETCH o.user WHERE p.name = :fPostName ORDER BY p.id DESC", Post.class,
                Map.of("fPostName", postName)
        );
    }

    public Collection<Post> findNew() {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        return crudRepository.query(
                "FROM Post p"
                        + " JOIN FETCH p.user JOIN FETCH p.price JOIN FETCH p.participates JOIN FETCH p.car c"
                        + " JOIN FETCH c.engine JOIN FETCH c.owner JOIN FETCH c.owner.user JOIN FETCH c.owners o"
                        + " JOIN FETCH o.user WHERE p.created >= :fToday ORDER BY p.id DESC", Post.class,
                Map.of("fToday", today)
        );
    }

    public Collection<Post> findWithPhoto() {
        return crudRepository.query(
                "FROM Post p"
                        + " JOIN FETCH p.user JOIN FETCH p.price JOIN FETCH p.participates JOIN FETCH p.car c"
                        + " JOIN FETCH c.engine JOIN FETCH c.owner JOIN FETCH c.owner.user JOIN FETCH c.owners o"
                        + " JOIN FETCH o.user WHERE p.fileId > 0 ORDER BY p.id DESC", Post.class
        );
    }
}