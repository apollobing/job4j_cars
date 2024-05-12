create table history_owners
(
    id              serial          primary key,
    car_id          int             not null references car(id),
    owner_id        int             not null references owner(id),
    startAt         timestamp       not null,
    endAt           timestamp       not null,
    unique (car_id, owner_id)
);