CREATE TABLE IF NOT EXISTS university.groups (
	group_id BIGSERIAL PRIMARY KEY,
	group_name VARCHAR(5) NOT NULL UNIQUE,
	specialization VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS university.users (
	user_id BIGSERIAL PRIMARY KEY,
	group_id BIGINT REFERENCES university.groups (group_id) ON DELETE CASCADE, 
	name VARCHAR(40),
	login VARCHAR(50) UNIQUE,
	password TEXT UNIQUE,
	role VARCHAR(20),
	birth_date DATE, 
	email TEXT UNIQUE,
	telephone_number VARCHAR(30) UNIQUE,
	residence_address VARCHAR(100),
	passport_number VARCHAR(8) UNIQUE,
	photo_uuid UUID,
	salary_amount TEXT,
	scholarship_amount NUMERIC(10, 2) DEFAULT  0,
	currency_mark VARCHAR(10) DEFAULT '$',
	employment_date DATE,
	admission_date DATE,
	employee_position VARCHAR(50),
	student_specialization VARCHAR(50),
	employee_working_shift VARCHAR(20),
	student_current_semester INTEGER,
	teacher_scientific_degree TEXT
);

CREATE TABLE IF NOT EXISTS university.courses (
	course_id BIGSERIAL PRIMARY KEY,
	specialization VARCHAR(50) NOT NULL,
	course_name VARCHAR(100) UNIQUE  NOT NULL,
	credit_hours INTEGER NOT NULL,
	class_room INTEGER NOT NULL,
	course_description TEXT
);

CREATE TABLE IF NOT EXISTS university.student_courses (
	user_id BIGINT REFERENCES university.users (user_id) ON DELETE CASCADE,
	course_id BIGINT REFERENCES university.courses (course_id) ON DELETE CASCADE,
	CONSTRAINT unique_constraint UNIQUE (user_id, course_id)
);

CREATE TABLE IF NOT EXISTS university.rooms (
	room_id BIGSERIAL PRIMARY KEY,
	room_number INTEGER UNIQUE NOT NULL,
	floor INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS university.lessons (
	lesson_id BIGSERIAL PRIMARY KEY,
	course_id BIGINT REFERENCES university.courses (course_id) ON DELETE CASCADE,
	room_id BIGINT REFERENCES university.rooms (room_id) ON DELETE CASCADE,
	lesson_date DATE NOT NULL,
	lesson_number  INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS university.avatars (
	id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	avatar_contents BYTEA,
	file_name TEXT, 
	content_type TEXT,
	user_role TEXT
);