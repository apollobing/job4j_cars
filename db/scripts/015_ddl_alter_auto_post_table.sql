ALTER TABLE auto_post
ADD FOREIGN KEY (file_id) REFERENCES files(id);