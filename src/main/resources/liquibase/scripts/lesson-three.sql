-- liquibase formatted sql

-- changeset jrembo:1
create index students_name_index on students (name);
-- changeset jrembo:2
create index faculties_nameColor_index on faculties (name, color);