create table price_history
(
   id               serial primary key,
   before           bigint not null,
   after            bigint not null,
   created          timestamp without time zone default now(),
   car_id           int REFERENCES car(id)
);