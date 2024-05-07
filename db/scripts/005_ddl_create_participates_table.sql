create table participates (
   id serial primary key,
   auto_post_id     int not null references auto_post(id),
   auto_user_id     int not null references auto_user(id),
   unique (auto_post_id, auto_user_id)
);