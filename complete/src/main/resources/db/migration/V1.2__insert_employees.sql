
-- insert all test employees
INSERT INTO employee_db.employee (id, position, name, uuid, salary)
VALUES (1, 'WORKER', 'Ivan', 'dfhssfhtr', '55000'),
       (2, 'MANAGER', 'Sergey', 'rsdhteh', '105000'),
       (3, 'DIRECTOR', 'Aleksandr', 'ragraskbf','155000'),
       (4, 'WORKER', 'Igor', 'ewirkn348', '55000'),
       (5, 'MANAGER', 'Aleksey', 'dsgwreiyb7', '105000'),
       (6,  'DIRECTOR', 'Petr', 'rdjiunywat', '155000'),
       (7, 'WORKER', 'Vasiliy', 'zseuayoewo5979', '55000'),
       (8, 'MANAGER', 'Ilya', 'elarnvds4', '105000'),
       (9, 'DIRECTOR', 'Anatoliy', 'adskbugfybure', '155000'),
       (10, 'WORKER', 'Andrey', 'adfuker748', '55000'),
       (11, 'MANAGER', 'Boris', 'cxzfbdhzkbfs50-', '105000'),
       (12,  'DIRECTOR', 'Abdulla', 'fshrenkgs', '155000');

-- insert all test tasks
INSERT INTO employee_db.task (id, description)
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

INSERT INTO employee_db.usr (user_id, username, password)
values (1, 'user', '$2a$10$2Cs7hzpo8ahCxowd4tQgXuWAOi6p/TjHMwCrl2XqZxnm7W1p6qf/C');