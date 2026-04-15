--liquibase formatted sql
--changeset Priamonosov Maksim:1.0.0

CREATE TABLE public.users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    balance INTEGER NOT NULL,
    card_number VARCHAR(255) NOT NULL,
    CONSTRAINT card_number UNIQUE(card_number),
    CONSTRAINT email UNIQUE(email)
);