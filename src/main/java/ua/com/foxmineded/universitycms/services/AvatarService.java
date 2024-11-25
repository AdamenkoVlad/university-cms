package ua.com.foxmineded.universitycms.services;

import java.util.UUID;

import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxmineded.universitycms.dto.AvatarDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface AvatarService {
	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional
	AvatarDto save(AvatarDto avatarDto) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	AvatarDto findById(UUID uuid) throws ServiceException;

	@Transactional
	void removeOrphanedStudentPhoto();

	@Transactional
	void removeOrphanedTeacherPhoto();

	@Transactional
	void removeOrphanedAdministratorPhoto();

}
