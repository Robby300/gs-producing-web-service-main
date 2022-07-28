
INSERT INTO employee_db.task (description) VALUES ('first test task');
INSERT INTO employee_db.task (description) VALUES ('second test task');
INSERT INTO employee_db.task (description) VALUES ('third test task');
-- INSERT INTO employee_db.task (description) VALUES ('fourth test task');

SELECT setval('employee_db.task_task_id_seq', (SELECT MAX(task_id) FROM employee_db.task))