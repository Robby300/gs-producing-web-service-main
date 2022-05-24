create schema if not exists employee_db;

create sequence if not exists hibernate_sequence;

create table employee
(
    id                int8 not null,
    employee_position varchar(255),
    name              varchar(255),
    salary            int4 not null,
    primary key (id)
);