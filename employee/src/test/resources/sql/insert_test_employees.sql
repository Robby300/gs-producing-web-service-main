
INSERT INTO employee_db.employee (position, name, uuid, salary) VALUES ('WORKER', 'first test employee', 'firstUUID', '55000');
INSERT INTO employee_db.employee (position, name, uuid, salary) VALUES('MANAGER', 'second test employee', 'secondUUID', '105000');
INSERT INTO employee_db.employee (position, name, uuid, salary) VALUES('DIRECTOR', 'third test employee', 'thirdUUID','155000');
INSERT INTO employee_db.employee (position, name, uuid, salary) VALUES('DIRECTOR', 'fourth test employee', 'fourthUUID','155000');

SELECT setval('employee_db.employee_employee_id_seq', (SELECT MAX(employee_id) FROM employee_db.employee));