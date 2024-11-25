package ua.com.foxmineded.universitycms.services.impl;

import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
	private static final int MAX_COURSES = 7;
	private static final int MIN_COURSES = 4;

	private final ModelMapper modelMapper;
	private final CourseRepository courseRepository;
	private final StudentRepository studentRepository;
	private final TeacherRepository teacherRepository;
	private final LessonRepository lessonRepository;

	@Override
	public CourseDto findById(Long courseId) throws ServiceException {
		return courseRepository.findById(courseId).map(course -> modelMapper.map(course, CourseDto.class))
				.orElseThrow(() -> {
					String message = "The course with id = %d was not found".formatted(courseId);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public CourseDto findByCourseName(String courseName) throws ServiceException {
		return courseRepository.findByCourseName(courseName).map(course -> modelMapper.map(course, CourseDto.class))
				.orElseThrow(() -> {
					String message = "The course with coure name = %s was not found".formatted(courseName);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public Page<CourseDto> findAllBySpecialization(String specialization, Pageable pageable) throws ServiceException {
		try {
			return courseRepository
					.findAllBySpecialization(Specialization.valueOf(specialization.toUpperCase()), pageable)
					.map(v -> modelMapper.map(v, CourseDto.class));
		} catch (IllegalArgumentException e) {
			String message = "Invalid specialization %s".formatted(specialization);
			log.error(message);
			throw new ServiceException(message);
		}
	}

	@Override
	public Page<CourseDto> findAll(Pageable pageable) {
		return courseRepository.findAll(pageable).map(v -> modelMapper.map(v, CourseDto.class));
	}

	@Override
	public Page<CourseDto> findAllByTeacherId(Long teacherId, Pageable pageable) {
		return courseRepository.findAllByTeacherId(teacherId, pageable).map(v -> modelMapper.map(v, CourseDto.class));
	}

	@Override
	public Page<CourseDto> findAllByTeacherName(String teacherName, Pageable pageable) {
		return courseRepository.findAllByTeacherName(teacherName, pageable)
				.map(v -> modelMapper.map(v, CourseDto.class));
	}

	@Override
	public Page<CourseDto> findAllByRoomId(Long roomId, Pageable pageable) {
		return courseRepository.findAllByRoomId(roomId, pageable).map(v -> modelMapper.map(v, CourseDto.class));
	}

	@Override
	public Page<CourseDto> findAllByRoomNumber(int roomNumber, Pageable pageable) {
		return courseRepository.findAllByRoomNumber(roomNumber, pageable).map(v -> modelMapper.map(v, CourseDto.class));
	}

	@Override
	public Page<CourseDto> findAllByStudentId(Long studentId, Pageable pageable) {
		return courseRepository.findAllByStudentId(studentId, pageable).map(v -> modelMapper.map(v, CourseDto.class));
	}

	@Override
	public void addStudentToCourseById(Long studentId, Long courseId) throws ServiceException {
		Student student = studentRepository.findById(studentId).orElseThrow(() -> {
			String message = "The student with id = %d was not found".formatted(studentId);
			log.error(message);
			return new ServiceException(message);
		});
		Course course = courseRepository.findById(courseId).orElseThrow(() -> {
			String message = "The course with id = %d was not found".formatted(courseId);
			log.error(message);
			return new ServiceException(message);
		});
		if (Objects.isNull(student.getGroup())) {
			String message = "To add student to course the student must be in a group";
			log.error(message);
			throw new ServiceException(message);
		}
		if (!Objects.equals(student.getSpecialization(), course.getSpecialization())) {
			String message = "The student and the course have the different specialization";
			log.error(message);
			throw new ServiceException(message);
		}
		if (student.getCourses().size() == MAX_COURSES) {
			String message = "The addition didn't succeed, student attends too much courses (%d)"
					.formatted(student.getCourses().size());
			log.error(message);
			throw new ServiceException(message);
		}
		if (student.getCourses().contains(course)) {
			String message = "The student already attends the course";
			log.error(message);
			throw new ServiceException(message);
		}
		student.getCourses().add(course);
		String message = "The student with id = %d was added to the course with id = %d".formatted(studentId, courseId);
		log.info(message);
	}

	@Override
	public void deleteStudentFromCourseById(Long studentId, Long courseId) throws ServiceException {
		Student student = studentRepository.findById(studentId).orElseThrow(() -> {
			String message = "The student with id = %d was not found".formatted(studentId);
			log.error(message);
			return new ServiceException(message);
		});
		Course course = courseRepository.findById(courseId).orElseThrow(() -> {
			String message = "The course with id = %d was not found".formatted(courseId);
			log.error(message);
			return new ServiceException(message);
		});
		if (!course.getStudents().contains(student)) {
			String message = "The course didn't contain the student";
			log.error(message);
			throw new ServiceException(message);
		}
		course.getStudents().remove(student);
		student.getCourses().remove(course);

		log.info("The student with id = %d was deleted from the course with id = %d".formatted(studentId, courseId));

		if (course.getStudents().size() < MIN_COURSES) {
			String message = "The student with id = %d attends too few courses %d".formatted(studentId,
					student.getCourses().size());
			log.warn(message);
		}
	}

	@Override
	public void addLessonToCourse(Long lessonId, Long courseId) throws ServiceException {
		Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> {
			String message = "The lesson with id = %d was not found".formatted(lessonId);
			log.error(message);
			return new ServiceException(message);
		});
		Course course = courseRepository.findById(courseId).orElseThrow(() -> {
			String message = "The course with id = %d was not found".formatted(courseId);
			log.error(message);
			return new ServiceException(message);
		});
		if (course.getLessons().contains(lesson)) {
			String message = "The course with id = %d already has the lesson with id = %d".formatted(courseId,
					lessonId);
			log.error(message);
			throw new ServiceException(message);
		}
		lesson.setCourse(course);

		log.info("The lesson with id = %d was added to the course with id = %d".formatted(lessonId, courseId));

	}

	@Override
	public void deleteLessonFromCourse(Long lessonId, Long courseId) throws ServiceException {
		Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> {
			String message = "The lesson with id = %d was not found".formatted(lessonId);
			log.error(message);
			return new ServiceException(message);
		});
		Course course = courseRepository.findById(courseId).orElseThrow(() -> {
			String message = "The course with id = %d was not found".formatted(courseId);
			log.error(message);
			return new ServiceException(message);
		});
		if (!course.getLessons().contains(lesson)) {
			String message = "The course with id = %d does not have the lesson with id = %d".formatted(courseId,
					lessonId);
			log.error(message);
			throw new ServiceException(message);
		}
		lesson.setCourse(null);

		log.info("The lesson with id = %d was deleted from the course with id = %d".formatted(lessonId, courseId));
	}

	@Override
	public CourseDto save(@Valid CourseDto courseDto) throws ServiceDataIntegrityException {
		ServiceDataIntegrityException serviceDataIntegrityException = new ServiceDataIntegrityException();
		courseRepository.findByCourseName(courseDto.getCourseName()).ifPresent(value -> {
			if (!(Objects.equals(value.getId(), courseDto.getId())
					&& Objects.equals(value.getCourseName(), courseDto.getCourseName()))) {
				String message = "A course %s already exists".formatted(courseDto.getCourseName());
				log.error(message);
				serviceDataIntegrityException.getExceptions()
						.add(new ServiceDataIntegrityException("courseName", message));
			}
		});
		courseRepository.findByCourseDescription(courseDto.getCourseDescription()).ifPresent(value -> {
			if (!(Objects.equals(value.getId(), courseDto.getId())
					&& Objects.equals(value.getCourseDescription(), courseDto.getCourseDescription()))) {
				String message = "The duplicate description for the course %s".formatted(courseDto.getCourseName());
				log.warn(message);
				serviceDataIntegrityException.getExceptions()
						.add(new ServiceDataIntegrityException("courseDescription", message));
			}
		});
		if (!serviceDataIntegrityException.getExceptions().isEmpty()) {
			throw serviceDataIntegrityException;
		}
		CourseDto result = modelMapper.map(courseRepository.save(modelMapper.map(courseDto, Course.class)),
				CourseDto.class);
		String message = "The course with id = %d was saved".formatted(result.getId());
		log.info(message);
		return result;
	}

	@Override
	public void deleteById(Long courseId) throws ServiceException {
		courseRepository.findById(courseId).orElseThrow(() -> {
			String message = "The course with id = %d was not found".formatted(courseId);
			log.error(message);
			return new ServiceException(message);
		});
		courseRepository.deleteById(courseId);
		String message = "The course with id = %d was deleted".formatted(courseId);
		log.info(message);
	}

	@Override
	public void addTeacherToCourseById(Long teacherId, Long courseId) throws ServiceException {
		Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> {
			String message = "The teacher with id = %d was not found".formatted(teacherId);
			log.error(message);
			return new ServiceException(message);
		});
		Course course = courseRepository.findById(courseId).orElseThrow(() -> {
			String message = "The course with id = %d was not found".formatted(courseId);
			log.error(message);
			return new ServiceException(message);
		});
		if (Objects.nonNull(course.getTeacher())) {
			String message = "The course already has a teacher";
			log.error(message);
			throw new ServiceException(message);
		}
		course.setTeacher(teacher);
		String message = "The teacher with id = %d was added to the course with id = %d".formatted(teacherId, courseId);
		log.info(message);
	}

	@Override
	public void deleteTeacherFromCourseById(Long courseId) throws ServiceException {
		Course course = courseRepository.findById(courseId).orElseThrow(() -> {
			String message = "The course with id = %d was not found".formatted(courseId);
			log.error(message);
			return new ServiceException(message);
		});
		if (Objects.isNull(course.getTeacher())) {
			String message = "The current course does not have a teacher";
			log.error(message);
			throw new ServiceException(message);
		}
		Long teacherId = course.getTeacher().getId();
		course.setTeacher(null);
		String message = "The teacher with id = %d was deleted from the course with id = %d".formatted(teacherId,
				course.getId());
		log.info(message);
	}
}
