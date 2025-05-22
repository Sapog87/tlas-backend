-- liquibase formatted sql

-- changeset Sergey:1744877064500-1
ALTER TABLE search_history
    ADD is_favorite BOOLEAN;

-- changeset Sergey:1744877064500-2
ALTER TABLE search_history
    ALTER COLUMN is_favorite SET NOT NULL;

