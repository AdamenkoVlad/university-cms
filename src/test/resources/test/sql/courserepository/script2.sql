INSERT INTO university.users (user_id, role) VALUES (10001, 'STUDENT');

INSERT INTO university.courses (course_id, specialization, course_name, credit_hours)
VALUES 
(10005, 'ART', 'Introduction to Programming', 3),
(10006, 'ART', 'Mechanics of Materials', 4);

INSERT INTO university.student_courses VALUES 
(10001, 10005),
(10001, 10006);