--liquibase formatted sql
--changeset Siarhei_Kavaleu:db localFilePath:01.000.01/todos.sql
ALTER TABLE todos ALTER COLUMN expiration_date TYPE timestamp;