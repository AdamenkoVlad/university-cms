package ua.com.foxmineded.universitycms.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import ua.com.foxmineded.universitycms.dto.TeacherDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface TeacherService {
	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<TeacherDto> findAll(Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<TeacherDto> findAllByName(String name, Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	TeacherDto findById(Long teacherId) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	TeacherDto findByLogin(String login) throws ServiceException;

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional(readOnly = true)
	TeacherDto findByPassportNumber(String passportNumber) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER" })
	@Transactional(readOnly = true)
	TeacherDto findByTelephoneNumber(String telephoneNumber) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	TeacherDto findByCourseName(String courseName) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	TeacherDto findByCourseId(Long courseId) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	TeacherDto findByLessonId(Long lessonId) throws ServiceException;

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional
	TeacherDto save(@Valid TeacherDto teacherDto) throws ServiceDataIntegrityException;

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional
	void deleteById(Long id) throws ServiceException;
}
