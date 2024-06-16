create table car
(
    id              serial          primary key,
    name            varchar         not null,
    vin             varchar         not null unique,
    engine_id       int             not null references engine(id),
    car_body_id     int             not null references car_body(id),
    owner_id        int             not null references owner(id)
);