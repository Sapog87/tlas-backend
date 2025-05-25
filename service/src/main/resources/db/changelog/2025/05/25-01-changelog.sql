-- liquibase formatted sql

-- changeset Sergey:1748175619798-1
ALTER TABLE "user"
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

