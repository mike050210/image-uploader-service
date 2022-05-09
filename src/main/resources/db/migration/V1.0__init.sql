create table if not exists image
(
    id               SERIAL        PRIMARY KEY,
    name             VARCHAR(20),
    image_size       NUMERIC,
    file_extension   VARCHAR(40) ,
    updated_at       TIMESTAMP      DEFAULT NOW()
--    bitmap_id        VARCHAR(128)
);