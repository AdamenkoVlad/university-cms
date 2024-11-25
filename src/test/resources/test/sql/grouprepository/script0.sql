INSERT INTO university.groups (group_id, group_name, specialization) 
VALUES
(10000, 'AA-11', 'COMPUTER_SCIENCE'),
(10001, 'BB-22', 'MEDICINE'),
(10002, 'CC-33', 'ART');

INSERT INTO university.users (
    user_id, birth_date, email, telephone_number, residence_address,
    passport_number,  name, login, password, role,
    scholarship_amount, currency_mark, admission_date, student_specialization,
    student_current_semester, group_id, gender
) VALUES (
    10000, '2000-02-02', 'email2@example.com', '987654321', 'Address 2',
    789012,  'Nome2', 'login2', 'password2', 'STUDENT',
    1500.00, 'EUR', '2000-02-02', 'COMPUTER_SCIENCE',
    2, 10000, 'M'
),
(
    10001, '2000-03-03', 'email3@example.com', '555555555', 'Address 3',
    456789,  'Nome3', 'login3', 'password3', 'STUDENT',
    2000.00, 'GBP', '2000-03-03', 'COMPUTER_SCIENCE',
    3, 10000, 'M'
),
(
    10002, '2000-04-04', 'email4@example.com', '111111111', 'Address 4',
    987654,  'Nome4', 'login4', 'password4', 'STUDENT',
    2500.00, 'CAD', '2000-04-04', 'COMPUTER_SCIENCE',
    4, 10000, 'M'
),
(
    10003, '2000-05-05', 'email5@example.com', '222222222', 'Address 5',
    654321,  'Nome5', 'login5', 'password5', 'STUDENT',
    3000.00, 'AUD', '2000-05-05', 'MEDICINE',
    5, 10001, 'M'
),
(
    10004, '2000-05-05', 'email6@example.com', '242222222', 'Address 5',
    654322,  'Nome5', 'login6', 'password6', 'STUDENT',
    3000.00, 'AUD', '2000-05-05', 'MEDICINE',
    5, 10001, 'M'
),
(
    10005, '2000-05-05', 'email7@example.com', '222225222', 'Address 5',
    654323, 'Nome5', 'login7', 'password7', 'STUDENT',
    3000.00, 'AUD', '2000-05-05', 'ART',
    5, 10002, 'M'
);