--liquibase formatted sql
--changeset Priamonosov Maksim:1.0.1

CREATE INDEX email_idx ON users (email);
CREATE INDEX card_number_idx ON users (card_number);