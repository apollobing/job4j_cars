package ru.job4j.cars.repository;

import ru.job4j.cars.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> save(User user);

    Optional<User> findByEmailAndPassword(String login, String password);
}
