package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.CarBody;
import ru.job4j.cars.repository.CarBodyRepository;

import java.util.Collection;

@Service
public class SimpleCarBodyService implements CarBodyService {

    private final CarBodyRepository carBodyRepository;

    public SimpleCarBodyService(CarBodyRepository carBodyRepository) {
        this.carBodyRepository = carBodyRepository;
    }

    @Override
    public Collection<CarBody> findAll() {
        return carBodyRepository.findAll();
    }
}
