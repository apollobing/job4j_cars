package ru.job4j.cars.repository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.CarBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Transactional
@AllArgsConstructor
public class HibernateCarBodyRepository implements CarBodyRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<CarBody> findById(int id) {
        return crudRepository.optional(
                "FROM CarBody WHERE id = :fId", CarBody.class,
                Map.of("fId", id)
        );
    }

    @Override
    public List<CarBody> findAll() {
        return crudRepository.query("FROM CarBody", CarBody.class);
    }
}
