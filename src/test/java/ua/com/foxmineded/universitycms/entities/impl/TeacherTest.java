package ua.com.foxmineded.universitycms.entities.impl;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ua.com.foxmineded.universitycms.enums.Gender;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class TeacherTest {
	@PersistenceContext
	EntityManager em;

	@Test
	void testToPersistTeacher() {
		Teacher teacherInput = Instancio.of(Teacher.class).ignore(field(Teacher::getId))
				.ignore(field(Teacher::getCourses)).set(field(Teacher::getName), "Lucas Williams")
				.set(field(Teacher::getGender), Gender.M).set(field(Teacher::getEmail), "lucaswilliams@gmail.com")
				.set(field(Teacher::getTelephoneNumber), "1263573352")
				.set(field(Teacher::getPassportNumber), String.valueOf(89056789))
				.set(field(Teacher::getLogin), "teacher").set(field(Teacher::getCurrencyMark), "USD")
				.set(field(Teacher::getSalaryAmount), "2500")
				.set(field(Teacher::getBirthDate), LocalDate.of(2000, 12, 25))
				.set(field(Teacher::getEmploymentDate), LocalDate.of(2000, 12, 25)).create();
		assertNull(teacherInput.getId());
		em.persist(teacherInput);
		em.flush();
		em.clear();
		Teacher teacherSaved = em.find(Teacher.class, teacherInput.getId());
		assertNotNull(teacherSaved.getId());
		assertNotNull(teacherSaved.getBirthDate());
		assertNotNull(teacherSaved.getEmail());
		assertNotNull(teacherSaved.getTelephoneNumber());
		assertNotNull(teacherSaved.getResidenceAddress());
		assertNotNull(teacherSaved.getPassportNumber());
		assertNotNull(teacherSaved.getName());
		assertNotNull(teacherSaved.getLogin());
		assertNotNull(teacherSaved.getPassword());
		assertNotNull(teacherSaved.getRole());
		assertNotNull(teacherSaved.getSalaryAmount());
		assertNotNull(teacherSaved.getCurrencyMark());
		assertNotNull(teacherSaved.getEmploymentDate());
		assertNotNull(teacherSaved.getPosition());
		assertNotNull(teacherSaved.getWorkingShift());
		assertNotNull(teacherSaved.getScientificDegree());
	}
}
