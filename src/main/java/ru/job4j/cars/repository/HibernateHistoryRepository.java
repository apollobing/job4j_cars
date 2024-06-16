package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.History;

@Repository
@AllArgsConstructor
public class HibernateHistoryRepository implements HistoryRepository {

    private final CrudRepository crudRepository;

    public History create(History history) {
        crudRepository.run(session -> session.persist(history));
        return history;
    }

    public void update(History history) {
        crudRepository.run(session -> session.merge(history));
    }
}
