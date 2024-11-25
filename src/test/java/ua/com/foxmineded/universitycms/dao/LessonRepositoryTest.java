package ua.com.foxmineded.universitycms.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxmineded.universitycms.entities.impl.Lesson;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		LessonRepository.class }))
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = { "/test/sql/clear_tables.sql" })
class LessonRepositoryTest {
	@Autowired
	LessonRepository lessonRepository;

	@Test
	@Sql(scripts = { "/test/sql/lessonrepository/script0.sql" })
	void testFindAllByCourseId_AskFindAllByCourseId_ListShouldBeReturned() {
		Long expectedLessonId0 = 10000L;
		Long expectedLessonId1 = 10001L;
		Long courseId = 10000L;
		int expectedAmount = 2;
		Page<Lesson> lessons = lessonRepository.findAllByCourseId(courseId, Pageable.ofSize(2));
		assertEquals(expectedAmount, lessons.getSize());
		assertEquals(expectedLessonId0, lessons.getContent().get(0).getId());
		assertEquals(expectedLessonId1, lessons.getContent().get(1).getId());
	}

	@Test
	@Sql(scripts = { "/test/sql/lessonrepository/script0.sql" })
	void testFindAllByCourseName_AskFindAllByCourseName_ListShouldBeReturned() {
		Long expectedLessonId0 = 10000L;
		Long expectedLessonId1 = 10001L;
		String courseName = "Introduction to Programming";
		int expectedAmount = 2;
		Page<Lesson> lessons = lessonRepository.findAllByCourseName(courseName, Pageable.ofSize(2));
		assertEquals(expectedAmount, lessons.getSize());
		assertEquals(expectedLessonId0, lessons.getContent().get(0).getId());
		assertEquals(expectedLessonId1, lessons.getContent().get(1).getId());
	}

	@Test
	@Sql(scripts = { "/test/sql/lessonrepository/script1.sql" })
	void testFindAllByRoomId_AskFindAllByRoomId_ListShouldBeReturned() {
		Long expectedLessonId0 = 10000L;
		Long expectedLessonId1 = 10001L;
		Long roomId = 10000L;
		int expectedAmount = 2;
		Page<Lesson> lessons = lessonRepository.findAllByRoomId(roomId, Pageable.ofSize(2));
		assertEquals(expectedAmount, lessons.getSize());
		assertEquals(expectedLessonId0, lessons.getContent().get(0).getId());
		assertEquals(expectedLessonId1, lessons.getContent().get(1).getId());
	}

	@Test
	@Sql(scripts = { "/test/sql/lessonrepository/script2.sql" })
	void testFindAllByStudentId_AskFindAllByStudentId_ListShouldBeReturned() {
		Long expectedLessonId0 = 10000L;
		Long expectedLessonId1 = 10001L;
		Long studentId = 10010L;
		int expectedAmount = 2;
		List<Lesson> lessons = lessonRepository.findAllByStudentId(studentId);
		assertEquals(expectedAmount, lessons.size());
		assertEquals(expectedLessonId0, lessons.get(0).getId());
		assertEquals(expectedLessonId1, lessons.get(1).getId());
	}

	@Test
	@Sql(scripts = { "/test/sql/lessonrepository/script3.sql" })
	void testFindAllByTeacherId_AskFindAllByTeacherId_ListShouldBeReturned() {
		Long expectedLessonId0 = 10000L;
		Long expectedLessonId1 = 10001L;
		Long teacherId = 10010L;
		int expectedAmount = 2;
		List<Lesson> lessons = lessonRepository.findAllByTeacherId(teacherId);
		assertEquals(expectedAmount, lessons.size());
		assertEquals(expectedLessonId0, lessons.get(0).getId());
		assertEquals(expectedLessonId1, lessons.get(1).getId());
	}

}
