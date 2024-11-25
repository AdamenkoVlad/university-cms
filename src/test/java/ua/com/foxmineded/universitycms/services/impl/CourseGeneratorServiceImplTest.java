package ua.com.foxmineded.universitycms.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import ua.com.foxmineded.universitycms.config.SecurityConfig;
import ua.com.foxmineded.universitycms.config.TypeMapConfig;
import ua.com.foxmineded.universitycms.dao.CourseRepository;
import ua.com.foxmineded.universitycms.dao.GroupRepository;
import ua.com.foxmineded.universitycms.dao.StudentRepository;
import ua.com.foxmineded.universitycms.dao.TeacherRepository;
import ua.com.foxmineded.universitycms.entities.impl.Course;
import ua.com.foxmineded.universitycms.entities.impl.Group;
import ua.com.foxmineded.universitycms.entities.impl.Student;
import ua.com.foxmineded.universitycms.entities.impl.Teacher;
import ua.com.foxmineded.universitycms.services.CourseGeneratorService;
import ua.com.foxmineded.universitycms.services.GroupGeneratorService;
import ua.com.foxmineded.universitycms.services.StudentGeneratorService;
import ua.com.foxmineded.universitycms.services.TeacherGeneratorService;
import ua.com.foxmineded.universitycms.utils.CoursesNamesReader;
import ua.com.foxmineded.universitycms.utils.PersonNamesReader;
import ua.com.foxmineded.universitycms.utils.StudentPhotoReader;
import ua.com.foxmineded.universitycms.utils.TeacherPhotoReader;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		CoursesNamesReader.class, CourseRepository.class, CourseGeneratorService.class, TeacherGeneratorService.class,
		TeacherPhotoReader.class, PersonNamesReader.class, TeacherRepository.class, StudentGeneratorService.class,
		StudentRepository.class, StudentPhotoReader.class, StudentGeneratorService.class, GroupRepository.class,
		GroupGeneratorService.class, SecurityConfig.class, TypeMapConfig.class, UserDetailsService.class

}))
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = { "/test/sql/clear_tables.sql" })
class CourseGeneratorServiceImplTest {
	final static int MAX_COURSES_AMOUNT = 7;
	final static int MIN_COURSES_AMOUNT = 3;

	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	CourseRepository courseRepository;
	@Autowired
	StudentRepository studentRepository;
	@Autowired
	CourseGeneratorService courseGeneratorService;
	@Autowired
	TeacherGeneratorService teacherGeneratorService;
	@Autowired
	StudentGeneratorService studentGeneratorService;
	@Autowired
	GroupGeneratorService groupGeneratorService;

	@Test
	void testGenerateWithTeachers_AskGenerateCoursesWithTeachers_DataShouldBeValid() throws IOException {
		int expectedCoursesAmount = 40;
		List<Teacher> teachers = teacherGeneratorService.generate();
		assertDoesNotThrow(() -> {
			courseGeneratorService.generateWithTeachers(teachers);
		});
		List<Course> courses = courseRepository.findAll();
		assertEquals(expectedCoursesAmount, courses.size());
		for (int i = 0; i < courses.size(); i++) {
			assertNotNull(courses.get(i).getId());
			assertNotNull(courses.get(i).getTeacher());
		}
	}

	@Test
	void testAllocateStudentsToCourses_AskAllocateStudentsToCourses_AllocationShouldBeValid() throws IOException {
		List<Teacher> teachers = teacherGeneratorService.generate();
		List<Course> courses = courseGeneratorService.generateWithTeachers(teachers);
		List<Group> groups = groupGeneratorService.generate();
		List<Student> students = studentGeneratorService.generateWithGroups(groups);
		assertDoesNotThrow(() -> {
			courseGeneratorService.allocateStudentsToCourses(students, courses);
		});
		List<Student> studentsAfterAllocation = studentRepository.findAll();
		for (int i = 0; i < studentsAfterAllocation.size(); i++) {
			Student student = studentsAfterAllocation.get(i);
			int coursesAmount = student.getCourses().size();
			for (int j = 0; j < coursesAmount; j++) {
				assertEquals(student.getCourses().get(j).getSpecialization(), student.getSpecialization());
				assertEquals(coursesAmount, new LinkedHashSet<Course>(student.getCourses()).size());
			}
		}
	}
}
