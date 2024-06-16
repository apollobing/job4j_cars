package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.User;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateUserRepository implements UserRepository {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateUserRepository.class.getName());

    private final CrudRepository crudRepository;

    @Override
    public Optional<User> save(User user) {
        try {
            crudRepository.run(session -> session.persist(user));
            return Optional.of(user);
        } catch (Exception e) {
            LOG.error("DB already has user with the same email", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmailAndPassword(String login, String password) {
        return crudRepository.optional(
                "FROM User WHERE login = :fLogin AND password = :fPassword", User.class,
                Map.of("fLogin", login, "fPassword", password)
        );
    }
}
