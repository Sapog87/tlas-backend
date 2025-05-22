-- liquibase formatted sql

-- changeset Sergey:1744831426913-1
CREATE SEQUENCE IF NOT EXISTS "user_id_gen" START WITH 1 INCREMENT BY 1;

-- changeset Sergey:1744831426913-2
CREATE SEQUENCE IF NOT EXISTS role_id_gen START WITH 1 INCREMENT BY 1;

-- changeset Sergey:1744831426913-3
CREATE SEQUENCE IF NOT EXISTS search_history_id_gen START WITH 1 INCREMENT BY 1;

-- changeset Sergey:1744831426913-4
CREATE TABLE "user_roles"
(
    user_id  BIGINT NOT NULL,
    roles_id BIGINT NOT NULL,
    CONSTRAINT "pk_user_roles" PRIMARY KEY (user_id, roles_id)
);

-- changeset Sergey:1744831426913-5
CREATE TABLE role
(
    id   BIGINT NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_role PRIMARY KEY (id)
);

-- changeset Sergey:1744831426913-6
CREATE TABLE search_history
(
    id               BIGINT       NOT NULL,
    user_id          BIGINT,
    from_yandex_code VARCHAR(255) NOT NULL,
    to_yandex_code   VARCHAR(255) NOT NULL,
    count            INTEGER      NOT NULL,
    last_searched    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_search_history PRIMARY KEY (id)
);

-- changeset Sergey:1744831426913-7
CREATE TABLE "user"
(
    id            BIGINT       NOT NULL,
    name          VARCHAR(255) NOT NULL,
    username      VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

-- changeset Sergey:1744831426913-8
ALTER TABLE "user"
    ADD CONSTRAINT uc_user_password_hash UNIQUE (password_hash);

-- changeset Sergey:1744831426913-9
ALTER TABLE "user"
    ADD CONSTRAINT uc_user_username UNIQUE (username);

-- changeset Sergey:1744831426913-10
ALTER TABLE search_history
    ADD CONSTRAINT FK_SEARCHHISTORY_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id);

-- changeset Sergey:1744831426913-11
ALTER TABLE "user_roles"
    ADD CONSTRAINT "fk_`usrol_on_role" FOREIGN KEY (roles_id) REFERENCES role (id);

-- changeset Sergey:1744831426913-12
ALTER TABLE "user_roles"
    ADD CONSTRAINT "fk_`usrol_on_user" FOREIGN KEY (user_id) REFERENCES "user" (id);

-- changeset Sergey:1744831426913-13
INSERT INTO role (id, name)
VALUES (nextval('role_id_gen'), 'USER');