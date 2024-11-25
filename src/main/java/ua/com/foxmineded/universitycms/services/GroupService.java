package ua.com.foxmineded.universitycms.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import ua.com.foxmineded.universitycms.dto.GroupDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface GroupService {
	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	GroupDto findById(Long groupId) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	GroupDto findByGroupName(String groupName) throws ServiceException;

	@Secured({ "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	GroupDto findByStudentId(Long studentId) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<GroupDto> findAll(Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<GroupDto> findAllBySpecialization(String specialization, Pageable pageable) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<GroupDto> findAllWithLessOrEqualStudents(int studentAmount, Pageable pageable);

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional
	GroupDto save(@Valid GroupDto groupDto) throws ServiceDataIntegrityException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER" })
	@Transactional
	void addStudentToGroupById(Long groupId, Long studentId) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER" })
	@Transactional
	void deleteStudentFromGroupById(Long groupId, Long studentId) throws ServiceException;

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional
	void deleteById(Long groupId) throws ServiceException;
}
