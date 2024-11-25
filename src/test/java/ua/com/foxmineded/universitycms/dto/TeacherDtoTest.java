package ua.com.foxmineded.universitycms.dto;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.com.foxmineded.universitycms.entities.impl.Teacher;
import ua.com.foxmineded.universitycms.enums.Gender;

@SpringBootTest(classes = ModelMapper.class)
class TeacherDtoTest {
	@Autowired
	ModelMapper modelMapper;

	@Test
	void testMapEntityToDto() {
		Teacher teacher = Instancio.of(Teacher.class).ignore(field(Teacher::getId))
				.ignore(field(Teacher::getCourses)).set(field(Teacher::getName), "Lucas Williams")
				.set(field(Teacher::getGender), Gender.M).set(field(Teacher::getEmail), "lucaswilliams@gmail.com")
				.set(field(Teacher::getTelephoneNumber), "1263573352")
				.set(field(Teacher::getPassportNumber), String.valueOf(89056789))
				.set(field(Teacher::getLogin), "teacher")
				.set(field(Teacher::getCurrencyMark), "USD")
				.set(field(Teacher::getSalaryAmount), "2500")
				.set(field(Teacher::getBirthDate), LocalDate.of(2000, 12, 25))
				.set(field(Teacher::getEmploymentDate), LocalDate.of(2000, 12, 25))
				.create();
		TeacherDto teacherDto = modelMapper.map(teacher, TeacherDto.class);

		assertEquals(teacher.getId(), teacherDto.getId());
		assertEquals(teacher.getBirthDate(), teacherDto.getBirthDate());
		assertEquals(teacher.getEmail(), teacherDto.getEmail());
		assertEquals(teacher.getTelephoneNumber(), teacherDto.getTelephoneNumber());
		assertEquals(teacher.getResidenceAddress(), teacherDto.getResidenceAddress());
		assertEquals(teacher.getPassportNumber(), teacherDto.getPassportNumber());
		assertEquals(teacher.getName(), teacherDto.getName());
		assertEquals(teacher.getLogin(), teacherDto.getLogin());
		assertEquals(teacher.getPassword(), teacherDto.getPassword());
		assertEquals(teacher.getRole(), teacherDto.getRole());
		assertEquals(teacher.getSalaryAmount(), teacherDto.getSalaryAmount());
		assertEquals(teacher.getCurrencyMark(), teacherDto.getCurrencyMark());
		assertEquals(teacher.getEmploymentDate(), teacherDto.getEmploymentDate());
		assertEquals(teacher.getPosition(), teacherDto.getPosition());
		assertEquals(teacher.getWorkingShift(), teacherDto.getWorkingShift());
		assertEquals(teacher.getScientificDegree(), teacherDto.getScientificDegree());
	}

	@Test
	void testMapDtoToEntity() {
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		Teacher teacher = modelMapper.map(teacherDto, Teacher.class);

		assertEquals(teacherDto.getId(), teacher.getId());
		assertEquals(teacherDto.getBirthDate(), teacher.getBirthDate());
		assertEquals(teacherDto.getEmail(), teacher.getEmail());
		assertEquals(teacherDto.getTelephoneNumber(), teacher.getTelephoneNumber());
		assertEquals(teacherDto.getResidenceAddress(), teacher.getResidenceAddress());
		assertEquals(teacherDto.getPassportNumber(), teacher.getPassportNumber());
		assertEquals(teacherDto.getName(), teacher.getName());
		assertEquals(teacherDto.getLogin(), teacher.getLogin());
		assertEquals(teacherDto.getPassword(), teacher.getPassword());
		assertEquals(teacherDto.getRole(), teacher.getRole());
		assertEquals(teacherDto.getSalaryAmount(), teacher.getSalaryAmount());
		assertEquals(teacherDto.getCurrencyMark(), teacher.getCurrencyMark());
		assertEquals(teacherDto.getEmploymentDate(), teacher.getEmploymentDate());
		assertEquals(teacherDto.getPosition(), teacher.getPosition());
		assertEquals(teacherDto.getWorkingShift(), teacher.getWorkingShift());
		assertEquals(teacherDto.getScientificDegree(), teacher.getScientificDegree());
	}
}
