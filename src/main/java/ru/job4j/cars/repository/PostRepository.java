package ru.job4j.cars.repository;

import ru.job4j.cars.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post create(Post post);

    void update(Post post);

    void markCarAsSold(Post post);

    void delete(Post post);

    List<Post> findAllOrderById();

    Optional<Post> findById(int postId);

    Optional<Post> findByVin(String vin);

    List<Post> findByName(String postName);

    List<Post> findNew();

    List<Post> findWithPhoto();
}
