package ua.com.foxmineded.universitycms.entities.impl;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class StudentTest {
	@PersistenceContext
	EntityManager em;

	@Test
	void testToPersistStudent() {
		Student studentInput = Instancio.of(Student.class).ignore(field(Student::getId))
				.set(field(Student::getPassportNumber), "12341234")
				.ignore(field(Student::getGroup)).ignore(field(Student::getCourses)).create();
		assertNull(studentInput.getId());
		em.persist(studentInput);
		em.flush();
		em.clear();
		Student studentSaved = em.find(Student.class, studentInput.getId());
		assertNotNull(studentSaved.getId());
		assertNotNull(studentSaved.getBirthDate());
		assertNotNull(studentSaved.getEmail());
		assertNotNull(studentSaved.getTelephoneNumber());
		assertNotNull(studentSaved.getResidenceAddress());
		assertNotNull(studentSaved.getPassportNumber());
		assertNotNull(studentSaved.getName());
		assertNotNull(studentSaved.getLogin());
		assertNotNull(studentSaved.getPassword());
		assertNotNull(studentSaved.getRole());
		assertNotNull(studentSaved.getScholarshipAmount());
		assertNotNull(studentSaved.getCurrencyMark());
		assertNotNull(studentSaved.getAdmissionDate());
		assertNotNull(studentSaved.getSpecialization());
		assertNotNull(studentSaved.getCurrentSemester());
	}
}
