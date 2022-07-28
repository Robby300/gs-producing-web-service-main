SELECT setval('employee_db.task_id_seq', (SELECT MAX(id) FROM employee_db.task));

INSERT INTO employee_db.task (description) VALUES ('first test task');
INSERT INTO employee_db.task (description) VALUES ('second test task');
INSERT INTO employee_db.task (description) VALUES ('third test task');
