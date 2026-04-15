--liquibase formatted sql
--changeset Priamonosov Maksim:1.0.0

CREATE TABLE public.operations (
    id SERIAL PRIMARY KEY,
    id_user INTEGER,
    sum INTEGER NOT NULL,
    date TIMESTAMP NOT NULL,
    FOREIGN KEY (id_user) REFERENCES public.users (id)
);