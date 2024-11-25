INSERT INTO university.users (
    user_id, birth_date, email, telephone_number, residence_address, passport_number, 
    name, login, password, role, salary_amount, currency_mark, 
    employment_date, employee_position, employee_working_shift, teacher_scientific_degree, gender
) VALUES (
    10000, '1990-01-01', 'teacher@example.com', '123456789', '123 Main St', 123456, 
    'John Doe', 'john_doe', 'password123', 'TEACHER', 50000.00, 'USD', 
    '2023-01-01', 'Professor', 'DAY_SHIFT', 'Ph.D.', 'M'
),
(   10001, '1985-05-15', 'another_teacher@example.com', '987654321', '456 Oak St',
    789012, 'Jane Smith', 'jane_smith', 'securepass', 'TEACHER',
    40000.00, 'USD', '2023-02-01', 'Assistant Professor', 'NIGHT_SHIFT', 'M.Sc.', 'M'
);

INSERT INTO university.courses (
    course_id, specialization, course_name, credit_hours, course_description,
    teacher_id
) VALUES (
    10000, 'COMPUTER_SCIENCE', 'courseName1', 3, 'course description1', 10000
),
( 
    10001, 'MEDICINE', 'courseName2', 4, 'course description2', 10001
);

INSERT INTO university.users (
    user_id, birth_date, email, telephone_number, residence_address,
    passport_number, name, login, password, role,
    scholarship_amount, currency_mark, admission_date, student_specialization,
    student_current_semester, gender
) VALUES (
    10002, '2000-02-02', 'email2@example.com', '987954321', 'Address 2',
    789312,  'Nome2', 'login2', 'password2', 'STUDENT',
    1500.00, 'EUR', '2000-02-02', 'COMPUTER_SCIENCE',
    2, 'M'
),
(
    10003, '2000-03-03', 'email3@example.com', '555555555', 'Address 3',
    456789, 'Nome3', 'login3', 'password3', 'STUDENT',
    2000.00, 'GBP', '2000-03-03', 'COMPUTER_SCIENCE', 
    3, 'M'
),
(
    10004, '2000-04-04', 'email4@example.com', '111111111', 'Address 4',
    987654,  'Nome4', 'login4', 'password4', 'STUDENT',
    2500.00, 'CAD', '2000-04-04', 'MEDICINE',
    4, 'M'
),
(
    10005, '2000-05-05', 'email5@example.com', '222222222', 'Address 5',
    654321,  'Nome5', 'login5', 'password5', 'STUDENT',
    3000.00, 'AUD', '2000-05-05', 'MEDICINE', 
    5, 'M'
);

INSERT INTO university.student_courses (
    user_id, course_id
) VALUES 
    (10002, 10000),
    (10003, 10000),
    (10004, 10001),
    (10005, 10001);

