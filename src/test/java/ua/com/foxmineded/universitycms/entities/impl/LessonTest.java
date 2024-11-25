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
class LessonTest {
	@PersistenceContext
	EntityManager em;

	@Test
	void testToPersistLesson() {
		Lesson lessonInput = Instancio.of(Lesson.class).ignore(field(Lesson::getId)).ignore(field(Lesson::getRoom))
				.ignore(field(Lesson::getCourse)).create();
		assertNull(lessonInput.getId());
		em.persist(lessonInput);
		em.flush();
		em.clear();
		Lesson lessonSaved = em.find(Lesson.class, lessonInput.getId());
		assertNotNull(lessonSaved.getId());
		assertNotNull(lessonSaved.getLessonDate());
		assertNotNull(lessonSaved.getLessonNumber());
	}
}
