package ua.com.foxmineded.universitycms.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import ua.com.foxmineded.universitycms.dto.StudentDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface StudentService {
	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	StudentDto findById(Long studentId) throws ServiceException;

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional(readOnly = true)
	StudentDto findByPassportNumber(String passportNumber) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	StudentDto findByLogin(String login) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	StudentDto findByEmail(String email) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER" })
	@Transactional(readOnly = true)
	StudentDto findByTelephoneNumber(String telephoneNumber) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<StudentDto> findAll(Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<StudentDto> findAllByName(String name, Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<StudentDto> findAllByGroupName(String groupName, Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<StudentDto> findAllByCourseName(String courseName, Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<StudentDto> findAllByTeacherName(String teacherName, Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<StudentDto> findAllBySpecialization(String specialization, Pageable pageable) throws ServiceException;

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional
	StudentDto save(@Valid StudentDto studentDto) throws ServiceDataIntegrityException;

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional
	void deleteById(Long studentId) throws ServiceException;
}
