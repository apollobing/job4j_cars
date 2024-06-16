package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Owner;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateOwnerRepository implements OwnerRepository {

    private final CrudRepository crudRepository;

    public Owner create(Owner owner) {
        crudRepository.run(session -> session.persist(owner));
        return owner;
    }

    public Optional<Owner> findByUserId(int userId) {
        return crudRepository.optional(
                "from Owner o JOIN FETCH o.user where o.user.id = :fUserId", Owner.class,
                Map.of("fUserId", userId)
        );
    }
}
