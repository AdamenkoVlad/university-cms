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
class CourseTest {
	@PersistenceContext
	EntityManager em;

	@Test
	void testToPersistCourse() {
		Course courseInput = Instancio.of(Course.class).ignore(field(Course::getId)).ignore(field(Course::getStudents))
				.ignore(field(Course::getLessons)).ignore(field(Course::getTeacher)).create();
		assertNull(courseInput.getId());
		em.persist(courseInput);
		em.flush();
		em.clear();
		Course courseSaved = em.find(Course.class, courseInput.getId());
		assertNotNull(courseSaved.getId());
		assertNotNull(courseSaved.getSpecialization());
		assertNotNull(courseSaved.getCourseName());
		assertNotNull(courseSaved.getCourseDescription());
	}
}
