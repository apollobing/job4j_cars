package ru.job4j.cars.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "files")
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @NonNull
    private String name;

    @NonNull
    private String path;
}