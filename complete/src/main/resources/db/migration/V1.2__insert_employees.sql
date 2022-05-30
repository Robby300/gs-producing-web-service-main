
-- insert all test employees
INSERT INTO employee_db.employee (id, employee_position, name, salary)
VALUES (1, 'WORKER', 'Ivan', 55000),
       (2, 'MANAGER', 'Sergey', 105000),
       (3, 'DIRECTOR', 'Aleksandr', 155000),
       (4, 'WORKER', 'Igor', 55000),
       (5, 'MANAGER', 'Aleksey', 105000),
       (6,  'DIRECTOR', 'Petr', 155000),
       (7, 'WORKER', 'Vasiliy', 55000),
       (8, 'MANAGER', 'Ilya', 105000),
       (9, 'DIRECTOR', 'Anatoliy', 155000),
       (10, 'WORKER', 'Andrey', 55000),
       (11, 'MANAGER', 'Boris', 105000),
       (12,  'DIRECTOR', 'Abdulla', 155000);

-- insert all test tasks
INSERT INTO task (id, description)
values (1, 'first task'),
       (2, 'second task'),
       (3, 'third task'),
       (4, 'fourth task'),
       (5, 'fifth task'),
       (6, 'sixth task'),
       (7, 'seventh task'),
       (8, 'eighth task'),
       (9, 'ninth task'),
       (10, 'tenth task'),
       (11, 'eleventh task'),
       (12, 'twelfth task'),
       (13, 'thirteenth task'),
       (14, 'fourteenth task'),
       (15, 'fifteenth task');

-- insert data in the bridge table
INSERT INTO employee_db.employee_tasks (employee_id, task_id)
values (1 , 1),
       (1 , 2),
       (1 , 3),
       (1 , 4),
       (2 , 1),
       (2 , 2),
       (2 , 3),
       (2 , 4),
       (3 , 5),
       (3 , 6);