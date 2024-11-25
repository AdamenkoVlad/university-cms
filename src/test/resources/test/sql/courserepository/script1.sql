INSERT INTO university.users (
    user_id, group_id, name, login, password, role, birth_date, email, 
    telephone_number, residence_address, passport_number, 
    salary_amount, scholarship_amount, currency_mark, employment_date, 
    admission_date, employee_position, student_specialization, 
    employee_working_shift, student_current_semester, teacher_scientific_degree,
    gender
) VALUES (
    10000, null, 'John Doe', 'john_doe', 'hashed_password', 'TEACHER', 
    '1990-01-01', 'john.doe@email.com', '1234567890', '123 Main St, City', 
    12345678, 50000.00, 0.00, '$', 
    '2022-01-01', '2022-01-15', 'Lecturer', NULL, 'FULL_TIME', 
    NULL, 'Ph.D. in Computer Science', 'M'
) ,(
    10001, null, 'Jane Smith', 'jane_smith', 'hashed_password', 'TEACHER',
    '1995-02-15', 'jane.smith@email.com', '9876543210', '456 Oak St, Town',
    98765431,  0.00, 0.00, '$',
    '2022-02-15', '2022-03-01', 'Junior Developer', 'Computer Science',
    'FULL_TIME', 3, NULL, 'M'
);
INSERT INTO university.courses (
    course_id, specialization, course_name, credit_hours, course_description, 
    teacher_id
) VALUES (
    10000, 'ART', 'Introduction to Art History', 3, 'An overview of art movements throughout history.'
    , 10000
), (
    10001, 'ART', 'Painting Techniques', 4, 'Explore various painting techniques and styles.', 10001
);
INSERT INTO university.rooms (room_id, room_number, floor) 
VALUES 
(10000, 101, 1), 
(10001, 202, 2);
INSERT INTO university.lessons (lesson_id, course_id, room_id, lesson_date, lesson_number)  
VALUES (
	10000, 10000, 10000, '2343-03-23', 2
),
(
	10001, 10001, 10001, '2323-03-12', 2
);