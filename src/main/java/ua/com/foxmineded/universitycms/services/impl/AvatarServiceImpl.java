package ua.com.foxmineded.universitycms.services.impl;

import java.util.List;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxmineded.universitycms.dao.AdministratorRepository;
import ua.com.foxmineded.universitycms.dao.AvatarRepository;
import ua.com.foxmineded.universitycms.dao.StudentRepository;
import ua.com.foxmineded.universitycms.dao.TeacherRepository;
import ua.com.foxmineded.universitycms.dto.AvatarDto;
import ua.com.foxmineded.universitycms.entities.AbstractPerson;
import ua.com.foxmineded.universitycms.entities.impl.Avatar;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.AvatarService;

@Service
@Slf4j
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {
	private final ModelMapper modelMapper;
	private final AvatarRepository avatarRepository;
	private final StudentRepository studentRepository;
	private final AdministratorRepository administratorRepository;
	private final TeacherRepository teacherRepository;

	@Override
	public AvatarDto save(AvatarDto avatarDto) throws ServiceException {
		if (!avatarDto.getContentType().contains("image")) {
			String message = "The uploaded file is not an image";
			log.error(message);
			throw new ServiceException(message);
		}
		return modelMapper.map(avatarRepository.save(modelMapper.map(avatarDto, Avatar.class)), AvatarDto.class);
	}

	@Override
	public AvatarDto findById(UUID uuid) throws ServiceException {
		return avatarRepository.findById(uuid).map(value -> modelMapper.map(value, AvatarDto.class)).orElseThrow(() -> {
			String message = "The avatar with UUID = %s was not found".formatted(uuid.toString());
			log.error(message);
			return new ServiceException(message);
		});
	}

	@Scheduled(cron = "0 0/40 * * * *")
	@Override
	public void removeOrphanedAdministratorPhoto() {
		List<UUID> existingUuids = administratorRepository.findAll().stream().map(AbstractPerson::getPhotoUuid)
				.toList();

		List<UUID> allUuids = avatarRepository.findAll().stream()
				.filter(avatar -> avatar.getRole().equals(Role.ADMINISTRATOR)).map(Avatar::getId).toList();

		for (UUID uuid : allUuids) {
			if (!existingUuids.contains(uuid)) {
				avatarRepository.deleteById(uuid);
				log.info("The orphaned administrator picture with uuid = %s was deleted".formatted(uuid.toString()));
			}
		}
	}

	@Scheduled(cron = "0 0/30 * * * *")
	@Override
	public void removeOrphanedTeacherPhoto() {
		List<UUID> existingUuids = teacherRepository.findAll().stream().map(AbstractPerson::getPhotoUuid).toList();

		List<UUID> allUuids = avatarRepository.findAll().stream()
				.filter(avatar -> avatar.getRole().equals(Role.TEACHER)).map(Avatar::getId).toList();

		for (UUID uuid : allUuids) {
			if (!existingUuids.contains(uuid)) {
				avatarRepository.deleteById(uuid);
				log.info("The orphaned teacher picture with uuid = %s was deleted".formatted(uuid.toString()));
			}
		}
	}

	@Scheduled(cron = "0 0/30 * * * *")
	@Override
	public void removeOrphanedStudentPhoto() {
		List<UUID> existingUuids = studentRepository.findAll().stream().map(AbstractPerson::getPhotoUuid).toList();

		List<UUID> allUuids = avatarRepository.findAll().stream()
				.filter(avatar -> avatar.getRole().equals(Role.STUDENT)).map(Avatar::getId).toList();

		for (UUID uuid : allUuids) {
			if (!existingUuids.contains(uuid)) {
				avatarRepository.deleteById(uuid);
				log.info("The orphaned student picture with uuid = %s was deleted".formatted(uuid.toString()));
			}
		}
	}

}
