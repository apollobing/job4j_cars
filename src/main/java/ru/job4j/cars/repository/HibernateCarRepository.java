package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateCarRepository implements CarRepository {

    private final CrudRepository crudRepository;

    public Car create(Car car) {
        crudRepository.run(session -> session.persist(car));
        return car;
    }

    public void update(Car car) {
        crudRepository.run(session -> session.merge(car));
    }

    @Override
    public Optional<Car> findByVin(String vin) {
        return crudRepository.optional(
                "FROM Car c"
                        + " JOIN FETCH c.engine JOIN FETCH c.price JOIN FETCH c.owner"
                        + " JOIN FETCH c.owner.user JOIN FETCH c.carBody"
                        + " JOIN FETCH c.owners o JOIN FETCH o.user WHERE c.vin = :fVin", Car.class,
                Map.of("fVin", vin)
        );
    }
}
