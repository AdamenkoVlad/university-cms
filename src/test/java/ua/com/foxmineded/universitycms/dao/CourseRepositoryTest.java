package ua.com.foxmineded.universitycms.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import ua.com.foxmineded.universitycms.entities.impl.Course;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		CourseRepository.class }))
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = { "/test/sql/clear_tables.sql" })
class CourseRepositoryTest {
	@Autowired
	CourseRepository courseRepository;

	@Test
	@Sql(scripts = "/test/sql/courserepository/script0.sql")
	void testFindAllByTeacherId_AskFindAllCoursesByTeacherId_CoursesListShouldBeReturned() {
		Long teacherId = 10001L;
		Long courseId = 10001L;
		int expectedPageSize = 1;
		Page<Course> courses = courseRepository.findAllByTeacherId(teacherId, Pageable.ofSize(10));
		assertEquals(expectedPageSize, courses.getContent().size());
		assertEquals(courseId, courses.getContent().get(0).getId());
	}

	@Test
	@Sql(scripts = "/test/sql/courserepository/script0.sql")
	void testFindAllByTeacherName_AskFindAllCoursesByTeacherName_CoursesListShouldBeReturned() {
		String teacherName = "Jane Smith";
		Long courseId = 10001L;
		int expectedCoursesAmount = 1;
		Page<Course> courses = courseRepository.findAllByTeacherName(teacherName, Pageable.ofSize(10));
		assertEquals(expectedCoursesAmount, courses.getContent().size());
		assertEquals(courseId, courses.getContent().get(0).getId());
	}

	@Test
	@Sql(scripts = "/test/sql/courserepository/script1.sql")
	void testFindAllByRoomId_AskFindAllCoursesWhichOccurInRoom_CoursesListShouldBeReturne() {
		Long roomId = 10000L;
		Long courseId = 10000L;
		int expectedCoursesAmount = 1;
		Page<Course> courses = courseRepository.findAllByRoomId(roomId, Pageable.ofSize(10));
		assertEquals(expectedCoursesAmount, courses.getContent().size());
		assertEquals(courseId, courses.getContent().get(0).getId());
	}

	@Test
	@Sql(scripts = "/test/sql/courserepository/script1.sql")
	void testFindAllByRoomNumber_AskFindAllByRoomNumber_CoursesListShouldBeReturne() {
		int roomNumber = 101;
		Long courseId = 10000L;
		int expectedCoursesAmount = 1;
		Page<Course> courses = courseRepository.findAllByRoomNumber(roomNumber, Pageable.ofSize(10));
		assertEquals(expectedCoursesAmount, courses.getContent().size());
		assertEquals(courseId, courses.getContent().get(0).getId());
	}
	
	@Test
	@Sql(scripts = "/test/sql/courserepository/script2.sql")
	void testFindAllByStudentId_AskFindAllByStudentId_CoursesListShouldBeReturne() {
		Long expectedCourseId0 = 10005L;
		Long expectedCourseId1 = 10006L;
		Long studentId = 10001L;
		int expectedCoursesAmount = 2;
		Page<Course> courses = courseRepository.findAllByStudentId(studentId, Pageable.ofSize(10));
		assertEquals(expectedCoursesAmount, courses.getContent().size());
		assertEquals(expectedCourseId0, courses.getContent().get(0).getId());
		assertEquals(expectedCourseId1, courses.getContent().get(1).getId());
	}
}
