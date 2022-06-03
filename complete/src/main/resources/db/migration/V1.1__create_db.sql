create schema if not exists employee_db;

create table employee_db.employee
(
    id                int8 not null,
    position varchar(255),
    name              varchar(255),
    uuid              varchar(255),
    salary            varchar(255),
    primary key (id)
);

create table employee_db.task
(
    id          int8 not null,
    description varchar(255),
    primary key (id)
);

create table employee_db.employee_tasks
(
    employee_id int8 not null,
    task_id     int8 not null
);

create sequence if not exists employee_db.hibernate_sequence;

alter table if exists employee_tasks
    add constraint task_id_foreign foreign key (task_id) references employee_db.task;

alter table if exists employee_tasks
    add constraint employee_id_foreign foreign key (employee_id) references employee_db.employee;

