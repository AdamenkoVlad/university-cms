package ua.com.foxmineded.universitycms.controllers.impl;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import ua.com.foxmineded.universitycms.controllers.AvatarController;
import ua.com.foxmineded.universitycms.dto.AvatarDto;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.AvatarService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/avatars")
public class AvatarControllerImpl implements AvatarController {
	@Autowired
	private final AvatarService avatarService;

	@PostMapping
	@Override
	public ResponseEntity<AvatarDto> create(MultipartFile file, @RequestParam Role role) throws ServiceException {
		try {
			AvatarDto avatarDto = AvatarDto.builder().withAvatarContents(file.getBytes())
					.withContentType(file.getContentType()).withFileName(file.getOriginalFilename()).withRole(role)
					.build();
			AvatarDto stored = avatarService.save(avatarDto);
			return ResponseEntity.created(URI.create("/avatars/%s".formatted(stored.getId()))).body(stored);
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Unable to parse uploaded file due " + e.getMessage());
		}
	}

	@GetMapping(path = "/{id}")
	@Override
	public ResponseEntity<Resource> findById(@PathVariable UUID id) throws ServiceException {
		AvatarDto avatarDto = avatarService.findById(id);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(avatarDto.getContentType()))
				.body(new ByteArrayResource(avatarDto.getAvatarContents()));
	}
}
