package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.OwnerHistory;
import ru.job4j.cars.repository.OwnerHistoryRepository;

import java.util.Optional;

@Service
public class SimpleOwnerHistoryService implements OwnerHistoryService {

    private final OwnerHistoryRepository ownerHistoryRepository;

    public SimpleOwnerHistoryService(OwnerHistoryRepository ownerHistoryRepository) {
        this.ownerHistoryRepository = ownerHistoryRepository;
    }

    @Override
    public OwnerHistory create(OwnerHistory ownerHistory) {
        return null;
    }

    @Override
    public Optional<OwnerHistory> findByCarIdAndOwnerId(int carId, int ownerId) {
        return ownerHistoryRepository.findByCarIdAndOwnerId(carId, ownerId);
    }
}
