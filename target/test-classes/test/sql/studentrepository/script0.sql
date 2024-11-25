INSERT INTO university.users (
    user_id, birth_date, email, telephone_number, residence_address,
    passport_number, name, login, password, role,
    scholarship_amount, currency_mark, admission_date, student_specialization,
    student_current_semester, gender
) VALUES
(
    10000, '2000-01-01', 'email1@example.com', '123456789', 'Address 1',
    123456, 'Nome1', 'login1', 'password1', 'STUDENT',
    1000.00, 'USD', '2000-01-01', 'ECONOMICS',
    1, 'M'
),
(
    10001, '2000-02-02', 'email2@example.com', '987654321', 'Address 2',
    789012,'Nome2', 'login2', 'password2', 'STUDENT',
    1500.00, 'EUR', '2000-02-02', 'COMPUTER_SCIENCE',
    2, 'M'
),
(
    10002, '2000-03-03', 'email3@example.com', '555555555', 'Address 3',
    456789,  'Nome3', 'login3', 'password3', 'STUDENT',
    2000.00, 'GBP', '2000-03-03', 'ECONOMICS',
    3, 'M'
);
