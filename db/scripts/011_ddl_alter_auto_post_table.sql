ALTER TABLE auto_post
ADD COLUMN car_id int not null unique references car(id),
ADD COLUMN name varchar not null,
ADD COLUMN file_id int;