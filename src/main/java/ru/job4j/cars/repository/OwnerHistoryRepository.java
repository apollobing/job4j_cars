package ru.job4j.cars.repository;

import ru.job4j.cars.model.OwnerHistory;

import java.util.Optional;

public interface OwnerHistoryRepository {

    OwnerHistory create(OwnerHistory ownerHistory);

    Optional<OwnerHistory> findByCarIdAndOwnerId(int carId, int ownerId);
}
