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
import ua.com.foxmineded.universitycms.entities.impl.Student;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		StudentRepository.class }))
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = { "/test/sql/clear_tables.sql" })
class StudentRepositoryTest {

	@Autowired
	StudentRepository studentRepository;

	@Test
	@Sql(scripts = { "/test/sql/studentrepository/script1.sql" })
	void testFindAllByGroupName_AskFindAllByGroupName_ShouldBeReturnedStudentList() {
		Long expectedStudentId0 = 10000L;
		Long expectedStudentId1 = 10001L;
		String groupName = "AA-11";
		int expectedPageSize = 2;
		Page<Student> actualStudentsPage = studentRepository.findAllByGroupName(groupName, Pageable.ofSize(10));
		assertEquals(expectedPageSize, actualStudentsPage.getNumberOfElements());
		assertEquals(expectedStudentId0, actualStudentsPage.getContent().get(0).getId());
		assertEquals(expectedStudentId1, actualStudentsPage.getContent().get(1).getId());
	}

	@Test
	@Sql(scripts = { "/test/sql/studentrepository/script2.sql" })
	void testFindAllByCourseName_AskFindAllByCourseName_ShouldBeReturnedStudentList() {
		Long expectedStudentId0 = 10000L;
		Long expectedStudentId1 = 10001L;
		String courseName = "courseName1";
		int expectedPageSize = 2;
		Page<Student> actualStudentsPage = studentRepository.findAllByCourseName(courseName, Pageable.ofSize(10));
		assertEquals(expectedPageSize, actualStudentsPage.getNumberOfElements());
		assertEquals(expectedStudentId0, actualStudentsPage.getContent().get(0).getId());
		assertEquals(expectedStudentId1, actualStudentsPage.getContent().get(1).getId());
	}

	@Test
	@Sql(scripts = { "/test/sql/studentrepository/script3.sql" })
	void testFindAllByTeacherName_AskFindAllByTeacherName_ShouldBeReturnedStudentList() {
		Long expectedStudentId0 = 10002L;
		Long expectedStudentId1 = 10003L;
		String teacherName = "John Doe";
		int expectedPageSize = 2;
		Page<Student> actualStudentsPage = studentRepository.findAllByTeacherName(teacherName, Pageable.ofSize(10));
		assertEquals(expectedPageSize, actualStudentsPage.getNumberOfElements());
		assertEquals(expectedStudentId0, actualStudentsPage.getContent().get(0).getId());
		assertEquals(expectedStudentId1, actualStudentsPage.getContent().get(1).getId());
	}
}
