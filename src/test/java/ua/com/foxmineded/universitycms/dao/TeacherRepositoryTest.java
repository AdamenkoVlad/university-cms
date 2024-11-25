package ua.com.foxmineded.universitycms.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxmineded.universitycms.entities.impl.Teacher;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		TeacherRepository.class }))
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = { "/test/sql/clear_tables.sql" })
class TeacherRepositoryTest {
	@Autowired
	TeacherRepository teacherRepository;

	@Test
	@Sql(scripts = { "/test/sql/teacherrepository/script0.sql" })
	void testFindByCourseName_AskFindByCourseName_ListShouldBeReturned() {
		String courseName = "Introduction to Art History";
		Long expectedTeacherId = 10000L;
		Teacher teacher = teacherRepository.findByCourseName(courseName).get();
		assertEquals(expectedTeacherId, teacher.getId());
	}

	@Test
	@Sql(scripts = { "/test/sql/teacherrepository/script0.sql" })
	void testFindByCourseId_AskFindByCourseId_ListShouldBeReturned() {
		Long courseId = 10000L;
		Long expectedTeacherId = 10000L;
		Teacher teacher = teacherRepository.findByCourseId(courseId).get();
		assertEquals(expectedTeacherId, teacher.getId());
	}

	@Test
	@Sql(scripts = { "/test/sql/teacherrepository/script0.sql" })
	void testFindByLessonId_AskFindByLessonId_ListShouldBeReturned() {
		Long lessonId = 10000L;
		Long expectedTeacherId = 10000L;
		Teacher teacher = teacherRepository.findByLessonId(lessonId).get();
		assertEquals(expectedTeacherId, teacher.getId());
	}
}
