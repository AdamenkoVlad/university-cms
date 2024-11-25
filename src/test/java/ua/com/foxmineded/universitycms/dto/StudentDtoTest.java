package ua.com.foxmineded.universitycms.dto;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.com.foxmineded.universitycms.entities.impl.Student;
import ua.com.foxmineded.universitycms.enums.Gender;

@SpringBootTest(classes = ModelMapper.class)
class StudentDtoTest {
	@Autowired
	ModelMapper modelMapper;

	@Test
	void testMapEntityToDto() {
		Student student = Instancio.of(Student.class).ignore(field(Student::getId))
				.ignore(field(Student::getGroup)).ignore(field(Student::getCourses))
				.set(field(Student::getEmail), "gabrieljohnson@gmail.com")
				.set(field(Student::getTelephoneNumber), "1234526346").set(field(Student::getName), "Gabriel Johnson")
				.set(field(Student::getGender), Gender.M)
				.set(field(Student::getPassportNumber), String.valueOf(98702793))
				.set(field(Student::getLogin), "student")
				.set(field(Student::getCurrencyMark), "USD")
				.set(field(Student::getScholarshipAmount), BigDecimal.valueOf(2500))
				.set(field(Student::getBirthDate), LocalDate.of(2000, 12, 25))
				.set(field(Student::getAdmissionDate), LocalDate.of(2000, 12, 25))
				.create();
		StudentDto studentDto = modelMapper.map(student, StudentDto.class);

		assertEquals(student.getId(), studentDto.getId());
		assertEquals(student.getBirthDate(), studentDto.getBirthDate());
		assertEquals(student.getEmail(), studentDto.getEmail());
		assertEquals(student.getTelephoneNumber(), studentDto.getTelephoneNumber());
		assertEquals(student.getResidenceAddress(), studentDto.getResidenceAddress());
		assertEquals(student.getPassportNumber(), studentDto.getPassportNumber());
		assertEquals(student.getName(), studentDto.getName());
		assertEquals(student.getLogin(), studentDto.getLogin());
		assertEquals(student.getPassword(), studentDto.getPassword());
		assertEquals(student.getRole(), studentDto.getRole());
		assertEquals(student.getScholarshipAmount(), studentDto.getScholarshipAmount());
		assertEquals(student.getCurrencyMark(), studentDto.getCurrencyMark());
		assertEquals(student.getAdmissionDate(), studentDto.getAdmissionDate());
		assertEquals(student.getSpecialization(), studentDto.getSpecialization());
		assertEquals(student.getCurrentSemester(), studentDto.getCurrentSemester());
	}

	@Test
	void testMapDtoToEntity() {
		StudentDto studentDto = Instancio.create(StudentDto.class);
		Student student = modelMapper.map(studentDto, Student.class);

		assertEquals(studentDto.getId(), student.getId());
		assertEquals(studentDto.getBirthDate(), student.getBirthDate());
		assertEquals(studentDto.getEmail(), student.getEmail());
		assertEquals(studentDto.getTelephoneNumber(), student.getTelephoneNumber());
		assertEquals(studentDto.getResidenceAddress(), student.getResidenceAddress());
		assertEquals(studentDto.getPassportNumber(), student.getPassportNumber());
		assertEquals(studentDto.getName(), student.getName());
		assertEquals(studentDto.getLogin(), student.getLogin());
		assertEquals(studentDto.getPassword(), student.getPassword());
		assertEquals(studentDto.getRole(), student.getRole());
		assertEquals(studentDto.getScholarshipAmount(), student.getScholarshipAmount());
		assertEquals(studentDto.getCurrencyMark(), student.getCurrencyMark());
		assertEquals(studentDto.getAdmissionDate(), student.getAdmissionDate());
		assertEquals(studentDto.getSpecialization(), student.getSpecialization());
		assertEquals(studentDto.getCurrentSemester(), student.getCurrentSemester());
	}
}
