package ru.job4j.cars.repository;

import ru.job4j.cars.model.Owner;

import java.util.Optional;

public interface OwnerRepository {

    Owner create(Owner owner);

    Optional<Owner> findByUserId(int userId);
}
