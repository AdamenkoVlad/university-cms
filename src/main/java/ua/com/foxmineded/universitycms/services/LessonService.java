package ua.com.foxmineded.universitycms.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import ua.com.foxmineded.universitycms.dto.LessonDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface LessonService {
	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<LessonDto> findAll(Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<LessonDto> findAllByLessonDate(LocalDate lessonDate, Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<LessonDto> findAllByCourseId(Long courseId, Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<LessonDto> findAllByCourseName(String courseName, Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<LessonDto> findAllByRoomId(Long roomId, Pageable pageable);

	@Secured({"ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	List<LessonDto> findAllByStudentId(Long studentId);

	@Secured({"ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	List<LessonDto> findAllByTeacherId(Long teacherId);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	LessonDto findById(Long lessonId) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER" })
	@Transactional
	void addRoomById(Long lessonId, Long roomId) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER" })
	@Transactional
	LessonDto save(@Valid LessonDto lessonDto) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER" })
	@Transactional
	void deleteById(Long lessonId) throws ServiceException;
}
