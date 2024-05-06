package ru.job4j.cars.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "price_history")
@Data
public class PriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "before")
    private int priceBefore;

    @Column(name = "after")
    private int priceAfter;

    private LocalDateTime created;
}