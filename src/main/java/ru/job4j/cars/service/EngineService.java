package ru.job4j.cars.service;

import ru.job4j.cars.model.Engine;

import java.util.Collection;

public interface EngineService {

    Collection<Engine> findAll();
}
