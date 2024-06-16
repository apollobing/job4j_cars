package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.File;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateFileRepository implements FileRepository {

    private final CrudRepository crudRepository;

    @Override
    public File save(File file) {
        crudRepository.run(session -> session.persist(file));
        return file;
    }

    @Override
    public Optional<File> findById(int fileId) {
        return crudRepository.optional(
                "FROM File WHERE id = :fId", File.class,
                Map.of("fId", fileId)
        );
    }

    @Override
    public void deleteById(int id) {
        crudRepository.run(
                "DELETE File WHERE id = :fId",
                Map.of("fId", id)
        );
    }
}