INSERT INTO university.rooms (room_id, room_number, floor) VALUES
(10000, 101, 1),
(10001, 202, 2);

INSERT INTO university.lessons (lesson_id, course_id, room_id, lesson_date, lesson_number) VALUES
(10000, NULL, 10000, '2023-01-10', 1),
(10001, NULL, 10000, '2023-01-10', 2),
(10002, NULL, 10001, '2023-01-12', 3);