package ua.com.foxmineded.universitycms.controllers;

import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ua.com.foxmineded.universitycms.dto.AvatarDto;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface AvatarController {
	ResponseEntity<AvatarDto> create(MultipartFile image, Role role) throws ServiceException;

	ResponseEntity<Resource> findById(UUID id) throws ServiceException;
}
