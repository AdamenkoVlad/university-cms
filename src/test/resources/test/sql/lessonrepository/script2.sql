INSERT INTO university.users (user_id, name, role) VALUES (10010, 'Student', 'STUDENT');

INSERT INTO university.courses (course_id, specialization, course_name, credit_hours, course_description) VALUES
(10000, 'ART', 'Introduction to Programming', 3, 'An introductory course on programming.');

INSERT INTO university.student_courses (user_id, course_id) VALUES (10010, 10000);

INSERT INTO university.lessons (lesson_id, course_id, lesson_date, lesson_number) VALUES
(10000, 10000, '2023-01-10', 1),
(10001, 10000, '2023-01-11', 2);

