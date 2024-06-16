package ru.job4j.cars.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.job4j.cars.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Getter
@Setter
public class PostDto {

    private int id;

    private String name;

    private String vin;

    private int engineId;

    private int carBodyId;

    private int price;

    private String description;

    private String ownerName;

    private int ownershipYear;

    private LocalDateTime created = LocalDateTime.now(ZoneId.of("UTC")).withNano(0);

    private User user;
}
