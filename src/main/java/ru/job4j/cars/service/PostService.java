package ru.job4j.cars.service;

import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

import java.util.Collection;
import java.util.Optional;

public interface PostService {

    Post create(PostDto postDto, FileDto image);

    void update(PostDto postDto, FileDto image);

    void markCarAsSold(Post post);

    void delete(Post post);

    Collection<Post> findAllOrderById();

    Optional<Post> findById(int postId);

    Optional<Post> findByVin(String vin);

    Collection<Post> findByName(String postName);

    Collection<Post> findNew();

    Collection<Post> findWithPhoto();

    void follow(Post post, User user);
}
