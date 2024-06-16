package ru.job4j.cars.repository;

import ru.job4j.cars.model.History;

public interface HistoryRepository {

    History create(History history);

    void update(History history);
}
