
-- insert all test employees
INSERT INTO employee_db.employee (id, position, name, uuid, salary) VALUES (1, 'WORKER', 'Ivan', 'dfhssfhtr', '55000');
INSERT INTO employee_db.employee (id, position, name, uuid, salary) VALUES(2, 'MANAGER', 'Sergey', 'rsdhteh', '105000');
INSERT INTO employee_db.employee (id, position, name, uuid, salary) VALUES(3, 'DIRECTOR', 'Aleksandr', 'ragraskbf','155000');
INSERT INTO employee_db.employee (id, position, name, uuid, salary) VALUES(4, 'WORKER', 'Igor', 'ewirkn348', '55000');
INSERT INTO employee_db.employee (id, position, name, uuid, salary) VALUES(5, 'MANAGER', 'Aleksey', 'dsgwreiyb7', '105000');
INSERT INTO employee_db.employee (id, position, name, uuid, salary) VALUES(6,  'DIRECTOR', 'Petr', 'rdjiunywat', '155000');
INSERT INTO employee_db.employee (id, position, name, uuid, salary) VALUES(7, 'WORKER', 'Vasiliy', 'zseuayoewo5979', '55000');
INSERT INTO employee_db.employee (id, position, name, uuid, salary) VALUES(8, 'MANAGER', 'Ilya', 'elarnvds4', '105000');
INSERT INTO employee_db.employee (id, position, name, uuid, salary) VALUES(9, 'DIRECTOR', 'Anatoliy', 'adskbugfybure', '155000');
INSERT INTO employee_db.employee (id, position, name, uuid, salary) VALUES(10, 'WORKER', 'Andrey', 'adfuker748', '55000');
INSERT INTO employee_db.employee (id, position, name, uuid, salary) VALUES(11, 'MANAGER', 'Boris', 'cxzfbdhzkbfs50-', '105000');
INSERT INTO employee_db.employee (id, position, name, uuid, salary) VALUES(12,  'DIRECTOR', 'Abdulla', 'fshrenkgs', '155000');

-- insert all test tasks
INSERT INTO employee_db.task (id, description) VALUES (1, 'first task');
INSERT INTO employee_db.task (id, description) VALUES (2, 'second task');
INSERT INTO employee_db.task (id, description) VALUES (3, 'third task');
INSERT INTO employee_db.task (id, description) VALUES (4, 'fourth task');
INSERT INTO employee_db.task (id, description) VALUES (5, 'fifth task');
INSERT INTO employee_db.task (id, description) VALUES (6, 'sixth task');
INSERT INTO employee_db.task (id, description) VALUES (7, 'seventh task');
INSERT INTO employee_db.task (id, description) VALUES (8, 'eighth task');
INSERT INTO employee_db.task (id, description) VALUES (9, 'ninth task');
INSERT INTO employee_db.task (id, description) VALUES (10, 'tenth task');
INSERT INTO employee_db.task (id, description) VALUES (11, 'eleventh task');
INSERT INTO employee_db.task (id, description) VALUES (12, 'twelfth task');
INSERT INTO employee_db.task (id, description) VALUES (13, 'thirteenth task');
INSERT INTO employee_db.task (id, description) VALUES (14, 'fourteenth task');
INSERT INTO employee_db.task (id, description) VALUES (15, 'fifteenth task');

-- insert data in the bridge table
INSERT INTO employee_db.employee_tasks (employee_id, task_id) VALUES (1 , 1);
INSERT INTO employee_db.employee_tasks (employee_id, task_id) VALUES (1 , 2);
INSERT INTO employee_db.employee_tasks (employee_id, task_id) VALUES (1 , 3);
INSERT INTO employee_db.employee_tasks (employee_id, task_id) VALUES (1 , 4);
INSERT INTO employee_db.employee_tasks (employee_id, task_id) VALUES (2 , 1);
INSERT INTO employee_db.employee_tasks (employee_id, task_id) VALUES (2 , 2);
INSERT INTO employee_db.employee_tasks (employee_id, task_id) VALUES (2 , 3);
INSERT INTO employee_db.employee_tasks (employee_id, task_id) VALUES (2 , 4);
INSERT INTO employee_db.employee_tasks (employee_id, task_id) VALUES (3 , 5);
INSERT INTO employee_db.employee_tasks (employee_id, task_id) VALUES (3 , 6);

INSERT INTO employee_db.usr (user_id, username, password)
VALUES (1, 'user', '$2a$10$2Cs7hzpo8ahCxowd4tQgXuWAOi6p/TjHMwCrl2XqZxnm7W1p6qf/C');