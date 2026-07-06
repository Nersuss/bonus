--liquibase formatted sql
--changeset Priamonosov Maksim:1.0.1

ALTER TABLE users ADD COLUMN refresh_token VARCHAR(255);