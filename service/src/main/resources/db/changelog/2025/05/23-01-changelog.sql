-- liquibase formatted sql

-- changeset Sergey:1748027476214-1
CREATE TABLE route
(
    id         UUID NOT NULL,
    value      JSONB,
    user_id    BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_route PRIMARY KEY (id)
);

-- changeset Sergey:1748027476214-2
ALTER TABLE route
    ADD CONSTRAINT FK_ROUTE_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id);

