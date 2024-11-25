package ua.com.foxmineded.universitycms.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.instancio.Instancio;
import static org.instancio.Select.field;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxmineded.universitycms.config.AppConfig;
import ua.com.foxmineded.universitycms.config.SecurityConfig;
import ua.com.foxmineded.universitycms.config.TypeMapConfig;
import ua.com.foxmineded.universitycms.dao.CourseRepository;
import ua.com.foxmineded.universitycms.dao.LessonRepository;
import ua.com.foxmineded.universitycms.dao.StudentRepository;
import ua.com.foxmineded.universitycms.dao.TeacherRepository;
import ua.com.foxmineded.universitycms.dto.CourseDto;
import ua.com.foxmineded.universitycms.entities.impl.Course;
import ua.com.foxmineded.universitycms.entities.impl.Lesson;
import ua.com.foxmineded.universitycms.entities.impl.Student;
import ua.com.foxmineded.universitycms.entities.impl.Teacher;
import ua.com.foxmineded.universitycms.enums.Specialization;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.CourseService;

@WithMockUser(roles = { "ADMINISTRATOR", "TEACHER", "STUDENT" })
@SpringBootTest(classes = { CourseServiceImpl.class, TypeMapConfig.class, SecurityConfig.class, AppConfig.class })
@ActiveProfiles("test")
class CourseServiceImplTest {
	@MockBean
	CourseRepository courseRepository;
	@MockBean
	StudentRepository studentRepository;
	@MockBean
	TeacherRepository teacherRepository;
	@MockBean
	UserDetailsService userDetailsService;
	@MockBean
	LessonRepository lessonRepository;
	@Autowired
	CourseService courseService;

	@Test
	void testAddStudentToCourseById_AskAddIfStudentInvalid_ShouldAriseException() {
		Long studentId = 10000L;
		Long courseId = 10000L;
		String message = "The student with id = %d was not found".formatted(studentId);
		when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.addStudentToCourseById(studentId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddStudentToCourseById_AskAddIfCourseInvalid_ShouldAriseException() {
		Long studentId = 10000L;
		Long courseId = 10000L;
		Student student = Instancio.of(Student.class).set(field(Student::getId), studentId).create();
		String message = "The course with id = %d was not found".formatted(studentId);
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.addStudentToCourseById(studentId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddStudentToCourseById_AskAddIfSpecializationInvalid_ShouldAriseException() {
		Long studentId = 10000L;
		Long courseId = 10000L;
		Student student = Instancio.of(Student.class).set(field(Student::getId), studentId)
				.set(field(Student::getSpecialization), Specialization.ART).create();
		Course course = Instancio.of(Course.class).set(field(Course::getId), courseId)
				.set(field(Course::getSpecialization), Specialization.COMPUTER_SCIENCE).create();
		String message = "The student and the course have the different specialization";
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.addStudentToCourseById(studentId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddStudentToCourseById_AskAddIfStudentAlreadyAttends7Courses_ShouldAriseException() {
		Long studentId = 10000L;
		Long courseId = 10000L;
		Student student = Instancio.of(Student.class).set(field(Student::getId), studentId)
				.set(field(Student::getSpecialization), Specialization.ART)
				.set(field(Student::getCourses), Instancio.ofList(Course.class).size(7).create()).create();
		Course course = Instancio.of(Course.class).set(field(Course::getId), courseId)
				.set(field(Course::getSpecialization), Specialization.ART).create();
		String message = "The addition didn't succeed, student attends too much courses (%d)"
				.formatted(student.getCourses().size());
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.addStudentToCourseById(studentId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddStudentToCourseById_AskAddIfStudentAlreadyAttendsTheCourse_ShouldAriseException() {
		Long studentId = 10000L;
		Long courseId = 10000L;
		Course course = Instancio.of(Course.class).set(field(Course::getId), courseId)
				.set(field(Course::getSpecialization), Specialization.ART).create();
		Student student = Instancio.of(Student.class).set(field(Student::getId), studentId)
				.set(field(Student::getSpecialization), Specialization.ART)
				.set(field(Student::getCourses), Instancio.ofList(Course.class).size(4).create()).create();
		student.getCourses().add(course);
		String message = "The student already attends the course";
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.addStudentToCourseById(studentId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddStudentToCourseById_AskAddIfStudentDoesNotHaveGroup_ShouldAriseException() {
		Long studentId = 10000L;
		Long courseId = 10000L;
		Course course = Instancio.of(Course.class).set(field(Course::getId), courseId).create();
		Student student = Instancio.of(Student.class).set(field(Student::getId), studentId)
				.ignore(field(Student::getGroup)).create();
		student.getCourses().add(course);
		String message = "To add student to course the student must be in a group";
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.addStudentToCourseById(studentId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddStudentToCourseById_AskAddValideStudentToValideCourse_AdditionSucceed() {
		Long studentId = 10000L;
		Long courseId = 10000L;
		Course course = Instancio.of(Course.class).set(field(Course::getId), courseId)
				.set(field(Course::getSpecialization), Specialization.ART).create();
		Student student = Instancio.of(Student.class).set(field(Student::getId), studentId)
				.set(field(Student::getSpecialization), Specialization.ART)
				.set(field(Student::getCourses), Instancio.ofList(Course.class).size(4).create()).create();
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		assertDoesNotThrow(() -> {
			courseService.addStudentToCourseById(studentId, courseId);
		});
	}

	@Test
	void testDeleteStudentFromCourseById_AskDeleteIfStudentInvalid_ShouldAriseException() {
		Long studentId = 10000L;
		Long courseId = 10000L;
		String message = "The student with id = %d was not found".formatted(studentId);
		when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.deleteStudentFromCourseById(studentId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testDeleteStudentFromCourseById_AskDeleteIfCourseInvalid_ShouldAriseException() {
		Long studentId = 10000L;
		Long courseId = 10000L;
		Student student = Instancio.of(Student.class).set(field(Student::getId), studentId).create();
		String message = "The course with id = %d was not found".formatted(courseId);
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.deleteStudentFromCourseById(studentId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testDeleteStudentFromCourseById_AskDeleteIfCourseDoesNotContainTheStudent_ShouldAriseException() {
		Long studentId = 10000L;
		Long courseId = 10000L;
		Student student = Instancio.of(Student.class).set(field(Student::getId), studentId).create();
		Course course = Instancio.of(Course.class).create();
		String message = "The course didn't contain the student";
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.deleteStudentFromCourseById(studentId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testDeleteStudentFromCourseById_AskDeleteValidStudentFromValidCourse_DeletionSucceed() {
		Long studentId = 10000L;
		Long courseId = 10000L;
		Student student = Instancio.of(Student.class).set(field(Student::getId), studentId).create();
		Course course = Instancio.of(Course.class).create();
		course.getStudents().add(student);
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		assertDoesNotThrow(() -> {
			courseService.deleteStudentFromCourseById(studentId, courseId);
		});
	}

	@Test
	void testAddTeacherToCourseById_AskAddInvalidTeacher_ShouldAriseException() {
		Long teacherId = 10000L;
		Long courseId = 10000L;
		String message = "The teacher with id = %d was not found".formatted(teacherId);
		when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.addTeacherToCourseById(teacherId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddTeacherToCourseById_AskAddIfCourseInvalid_ShouldAriseException() {
		Long teacherId = 10000L;
		Long courseId = 10000L;
		Teacher teacher = Instancio.of(Teacher.class).set(field(Teacher::getId), teacherId).create();
		String message = "The course with id = %d was not found".formatted(courseId);
		when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
		when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.addTeacherToCourseById(teacherId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddTeacherToCourseById_AskAddIfCourseAlreadyHasTeacher_ShouldAriseException() {
		Long teacherId = 10000L;
		Long courseId = 10000L;
		Teacher teacher = Instancio.of(Teacher.class).set(field(Teacher::getId), teacherId).create();
		Course course = Instancio.of(Course.class).set(field(Course::getId), courseId).create();
		String message = "The course already has a teacher";
		when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.addTeacherToCourseById(teacherId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddTeacherToCourseById_AskAddValidTeacherToValidCourse_AdditionSucceed() {
		Long teacherId = 10000L;
		Long courseId = 10000L;
		Teacher teacher = Instancio.of(Teacher.class).set(field(Teacher::getId), teacherId).create();
		Course course = Instancio.of(Course.class).set(field(Course::getId), courseId).ignore(field(Course::getTeacher))
				.create();
		when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		assertDoesNotThrow(() -> {
			courseService.addTeacherToCourseById(teacherId, courseId);
		});
	}

	@Test
	void testDeleteTeacherFromCourseById_AskDeleteIfInvalidCourse_ShouldAriseException() {
		Long teacherId = 10000L;
		Long courseId = 10000L;
		Teacher teacher = Instancio.of(Teacher.class).set(field(Teacher::getId), teacherId).create();
		String message = "The course with id = %d was not found".formatted(courseId);
		when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
		when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.deleteTeacherFromCourseById(courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testDeleteTeacherFromCourseById_AskDeleteIfCoureDoesNotHaveAnyTeacher_ShouldAriseException() {
		Long teacherId = 10000L;
		Long courseId = 10000L;
		Teacher teacher = Instancio.of(Teacher.class).set(field(Teacher::getId), teacherId).create();
		Course course = Instancio.of(Course.class).set(field(Course::getId), courseId).ignore(field(Course::getTeacher))
				.create();
		String message = "The current course does not have a teacher";
		when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.deleteTeacherFromCourseById(courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testDeleteTeacherFromCourseById_AskDeleteValidTeacherFromValidCourse_ShouldAriseException() {
		Long teacherId = 10000L;
		Long courseId = 10000L;
		Teacher teacher = Instancio.of(Teacher.class).set(field(Teacher::getId), teacherId).create();
		Course course = Instancio.of(Course.class).set(field(Course::getId), courseId)
				.set(field(Course::getTeacher), teacher).create();
		when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		assertDoesNotThrow(() -> {
			courseService.deleteTeacherFromCourseById(courseId);
		});
	}

	@Test
	void testDeleteById_AskDeleteByIdIfCourseIdInvalid_ExceptionShouldArise() {
		Long courseId = 10000L;
		String message = "The course with id = %d was not found".formatted(courseId);
		when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.deleteById(courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testDeleteById_AskDeleteById_DeletionShouldSucceed() {
		Long courseId = 10000L;
		Course course = Instancio.of(Course.class).set(field(Course::getId), courseId).create();
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		assertDoesNotThrow(() -> {
			courseService.deleteById(courseId);
		});
	}

	@Test
	void testSave_AskSaveCourseIfCourseNameAlreadyExists_ExceptionShouldBeThrown() {
		CourseDto course = Instancio.of(CourseDto.class).set(field(CourseDto::getCourseName), "Course").create();
		Course returnedCourse = Instancio.of(Course.class).set(field(Course::getCourseName), course.getCourseName())
				.generate(field(Course::getCreditHours), gen -> gen.ints().min(0)).create();
		when(courseRepository.findByCourseName(course.getCourseName())).thenReturn(Optional.of(returnedCourse));
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			courseService.save(course);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().stream().count());
	}

	@Test
	void testSave_AskSaveCourseIfCourseDescriptionAlreadyExists_ExceptionShouldBeThrown() {
		CourseDto course = Instancio.of(CourseDto.class).set(field(CourseDto::getCourseName), "Course")
				.set(field(CourseDto::getCourseDescription), "description").create();
		Course returnedCourse = Instancio.of(Course.class).set(field(Course::getCourseName), course.getCourseName())
				.set(field(Course::getCourseDescription), course.getCourseDescription())
				.generate(field(Course::getCreditHours), gen -> gen.ints().min(0)).create();
		when(courseRepository.findByCourseDescription(course.getCourseDescription()))
				.thenReturn(Optional.of(returnedCourse));
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			courseService.save(course);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().stream().count());
	}

	@Test
	void testAddLessonToCourse_AskAddIfLessonDoesNotExists_ExceptionShouldBeThrown() {
		Long lessonId = 10001L;
		Long courseId = 10002L;
		String message = "The lesson with id = %d was not found".formatted(lessonId);
		when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.addLessonToCourse(lessonId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddLessonToCourse_AskAddIfCourseDoesNotExists_ExceptionShouldBeThrown() {
		Long lessonId = 10001L;
		Long courseId = 10002L;
		String message = "The course with id = %d was not found".formatted(courseId);
		Lesson lesson = Instancio.create(Lesson.class);
		when(lessonRepository.findById(anyLong())).thenReturn(Optional.of(lesson));
		when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.addLessonToCourse(lessonId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddLessonToCourse_AskAddIfCourseAlreadyHasSamelesson_ExceptionShouldBeThrown() {
		Long lessonId = 10001L;
		Long courseId = 10002L;
		String message = "The course with id = %d already has the lesson with id = %d".formatted(courseId, lessonId);
		Lesson lesson = Instancio.create(Lesson.class);
		Course course = Instancio.create(Course.class);
		course.getLessons().add(lesson);
		when(lessonRepository.findById(anyLong())).thenReturn(Optional.of(lesson));
		when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.addLessonToCourse(lessonId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testDeleteLessonFromCourse_AskDeleteIfLessonDoesNotExists_ExceptionShouldBeThrown() {
		Long lessonId = 10001L;
		Long courseId = 10002L;
		String message = "The lesson with id = %d was not found".formatted(lessonId);
		when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.deleteLessonFromCourse(lessonId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testDeleteLessonFromCourse_AskDeleteIfCourseDoesNotExists_ExceptionShouldBeThrown() {
		Long lessonId = 10001L;
		Long courseId = 10002L;
		String message = "The course with id = %d was not found".formatted(courseId);
		Lesson lesson = Instancio.create(Lesson.class);
		when(lessonRepository.findById(anyLong())).thenReturn(Optional.of(lesson));
		when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.deleteLessonFromCourse(lessonId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testDeleteLessonFromCourse_AskDeleteIfDoesNotHaveTheLesson_ExceptionShouldBeThrown() {
		Long lessonId = 10001L;
		Long courseId = 10002L;
		String message = "The course with id = %d does not have the lesson with id = %d".formatted(courseId, lessonId);
		Lesson lesson = Instancio.create(Lesson.class);
		Course course = Instancio.create(Course.class);
		when(lessonRepository.findById(anyLong())).thenReturn(Optional.of(lesson));
		when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			courseService.deleteLessonFromCourse(lessonId, courseId);
		});
		assertEquals(message, throwable.getMessage());
	}
}
