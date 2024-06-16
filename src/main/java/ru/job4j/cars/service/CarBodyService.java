package ru.job4j.cars.service;

import ru.job4j.cars.model.CarBody;

import java.util.Collection;

public interface CarBodyService {

    Collection<CarBody> findAll();
}
