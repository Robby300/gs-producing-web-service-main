create schema if not exists employee_db;

create table employee_db.employee
(
    employee_id       SERIAL,
    position varchar(255),
    name     varchar(255),
    uuid     varchar(255),
    salary   varchar(255),
    primary key (employee_id)
);

create table employee_db.task
(
    task_id          SERIAL,
    description varchar(255),
    primary key (task_id)
);

create table employee_db.employee_tasks
(
    employee_id int8 not null,
    task_id     int8 not null
);

create table employee_db.user_role
(
    user_id int8 not null,
    roles   varchar(255)
);
create table employee_db.usr
(
    user_id  int8 not null,
    password varchar(255),
    username varchar(255),
    primary key (user_id)
);


alter table if exists employee_tasks
    add constraint task_id_foreign foreign key (task_id) references employee_db.task;

alter table if exists employee_tasks
    add constraint employee_id_foreign foreign key (employee_id) references employee_db.employee;

alter table if exists user_role add constraint user_role_foreign_key foreign key (user_id) references employee_db.usr;