package ua.com.foxmineded.universitycms.services;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import ua.com.foxmineded.universitycms.dto.RoomDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface RoomService {

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<RoomDto> findAll(Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	Page<RoomDto> findAllByFloor(int floor, Pageable pageable);

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	RoomDto findById(Long roomId) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	RoomDto findByRoomNumber(int roomNumber) throws ServiceException;

	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_TEACHER", "ROLE_STUDENT" })
	@Transactional(readOnly = true)
	RoomDto findByLessonId(Long lessonId) throws ServiceException;

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional
	RoomDto save(@Valid RoomDto roomDto) throws ServiceDataIntegrityException;

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional
	void deleteById(Long roomId) throws ServiceException;
}
