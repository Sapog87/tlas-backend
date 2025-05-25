-- liquibase formatted sql

-- changeset Sergey:1748088058981-1
ALTER TABLE route
    DROP COLUMN id;

-- changeset Sergey:1748088058981-2
ALTER TABLE route
    ADD id VARCHAR(255) NOT NULL PRIMARY KEY;

