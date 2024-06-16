package ru.job4j.cars.service;

import ru.job4j.cars.model.OwnerHistory;

import java.util.Optional;

public interface OwnerHistoryService {

    OwnerHistory create(OwnerHistory ownerHistory);

    Optional<OwnerHistory> findByCarIdAndOwnerId(int carId, int ownerId);
}
