--liquibase formatted sql
--changeset Siarhei_Kavaleu:db localFilePath:01.000.00/todos.sql
CREATE TABLE todos
(
    id                  UUID                            NOT NULL,
    description         VARCHAR(255)                    NOT NULL,
    expiration_date     DATE                            NOT NULL,
    is_done             BOOLEAN                         NOT NULL,
    CONSTRAINT pk_todos PRIMARY KEY (id)
);