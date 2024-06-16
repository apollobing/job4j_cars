package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.EngineRepository;

import java.util.Collection;

@Service
public class SimpleEngineService implements EngineService {

    private final EngineRepository engineRepository;

    public SimpleEngineService(EngineRepository engineRepository) {
        this.engineRepository = engineRepository;
    }

    @Override
    public Collection<Engine> findAll() {
        return engineRepository.findAll();
    }
}
