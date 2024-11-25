package ua.com.foxmineded.universitycms.services.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
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

import ua.com.foxmineded.universitycms.config.SecurityConfig;
import ua.com.foxmineded.universitycms.config.TypeMapConfig;
import ua.com.foxmineded.universitycms.dao.CourseRepository;
import ua.com.foxmineded.universitycms.dao.LessonRepository;
import ua.com.foxmineded.universitycms.dao.RoomRepository;
import ua.com.foxmineded.universitycms.dao.TeacherRepository;
import ua.com.foxmineded.universitycms.entities.impl.Course;
import ua.com.foxmineded.universitycms.entities.impl.Lesson;
import ua.com.foxmineded.universitycms.entities.impl.Room;
import ua.com.foxmineded.universitycms.entities.impl.Teacher;
import ua.com.foxmineded.universitycms.services.CourseGeneratorService;
import ua.com.foxmineded.universitycms.services.LessonGeneratorService;
import ua.com.foxmineded.universitycms.services.RoomGeneratorService;
import ua.com.foxmineded.universitycms.services.TeacherGeneratorService;
import ua.com.foxmineded.universitycms.utils.CoursesNamesReader;
import ua.com.foxmineded.universitycms.utils.PersonNamesReader;
import ua.com.foxmineded.universitycms.utils.TeacherPhotoReader;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		LessonRepository.class, LessonGeneratorService.class, CoursesNamesReader.class, CourseRepository.class,
		CourseGeneratorService.class, TeacherPhotoReader.class, PersonNamesReader.class, TeacherRepository.class,
		TeacherGeneratorService.class, RoomRepository.class, RoomGeneratorService.class, SecurityConfig.class,
		UserDetailsService.class, TypeMapConfig.class }))
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
class LessonGeneratorServiceImplTest {
	static final int LESSONS_AMOUNT = 5200;

	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	LessonRepository lessonRepository;
	@Autowired
	CourseGeneratorService courseGeneratorService;
	@Autowired
	TeacherGeneratorService teacherGeneratorService;
	@Autowired
	RoomGeneratorService roomGeneratorService;
	@Autowired
	LessonGeneratorService lessonGeneratorService;

	@Test
	void testGenerateWithCoursesAndRooms_AskGenerateDataWithCoursesAndRooms_TheDataShouldBeValid() throws IOException {
		List<Teacher> teachers = teacherGeneratorService.generate();
		List<Course> courses = courseGeneratorService.generateWithTeachers(teachers);
		List<Room> rooms = roomGeneratorService.generate();
		assertDoesNotThrow(() -> {
			lessonGeneratorService.generateWithCoursesAndRooms(courses, rooms);
		});
		List<Lesson> lessonsAfterGeneration = lessonRepository.findAll();
		assertEquals(LESSONS_AMOUNT, lessonsAfterGeneration.size());
		for (int i = 0; i < lessonsAfterGeneration.size(); i++) {
			assertNotNull(lessonsAfterGeneration.get(i).getId());
			assertNotNull(lessonsAfterGeneration.get(i).getRoom());
			assertNotNull(lessonsAfterGeneration.get(i).getCourse());
		}
	}
}
