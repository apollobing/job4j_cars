package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.OwnerHistory;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateOwnerHistoryRepository implements OwnerHistoryRepository {

    private final CrudRepository crudRepository;

    @Override
    public OwnerHistory create(OwnerHistory ownerHistory) {
        crudRepository.run(session -> session.persist(ownerHistory));
        return ownerHistory;
    }

    @Override
    public Optional<OwnerHistory> findByCarIdAndOwnerId(int carId, int ownerId) {
        return crudRepository.optional(
                "from OwnerHistory h"
                        + " JOIN FETCH h.car c JOIN FETCH h.owner o JOIN FETCH h.history"
                        + " JOIN FETCH c.engine JOIN FETCH c.owner JOIN FETCH c.owner.user JOIN FETCH c.owners os"
                        + " JOIN FETCH os.user"
                        + " where c.id = :fCarId and o.id = :fOwnerId"
                        + " ORDER BY h.id DESC LIMIT 1", OwnerHistory.class,
                Map.of("fCarId", carId, "fOwnerId", ownerId)
        );
    }
}
