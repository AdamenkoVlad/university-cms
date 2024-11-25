package ua.com.foxmineded.universitycms.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import ua.com.foxmineded.universitycms.dto.CourseDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface CourseService {
	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	CourseDto findById(Long courseId) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	CourseDto findByCourseName(String courseName) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<CourseDto> findAllBySpecialization(String specialization, Pageable pageable) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<CourseDto> findAll(Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<CourseDto> findAllByTeacherId(Long teacherId, Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<CourseDto> findAllByTeacherName(String teacherName, Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<CourseDto> findAllByRoomId(Long roomId, Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<CourseDto> findAllByRoomNumber(int roomNumber, Pageable pageable);

	@Secured({ "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<CourseDto> findAllByStudentId(Long studentId, Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER" })
	@Transactional
	void addStudentToCourseById(Long studentId, Long courseId) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER" })
	@Transactional
	void addTeacherToCourseById(Long teacherId, Long courseId) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER" })
	@Transactional
	void deleteStudentFromCourseById(Long studentId, Long courseId) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER" })
	@Transactional
	void deleteTeacherFromCourseById(Long courseId) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER" })
	@Transactional
	void addLessonToCourse(Long lessonId, Long courseId) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER" })
	@Transactional
	void deleteLessonFromCourse(Long lessonId, Long courseId) throws ServiceException;

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional
	CourseDto save(@Valid CourseDto courseDto) throws ServiceDataIntegrityException;

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional
	void deleteById(Long courseId) throws ServiceException;
}
