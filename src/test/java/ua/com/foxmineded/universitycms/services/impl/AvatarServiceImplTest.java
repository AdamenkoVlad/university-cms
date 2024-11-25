package ua.com.foxmineded.universitycms.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.instancio.Instancio;
import static org.instancio.Select.field;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.com.foxmineded.universitycms.config.TypeMapConfig;
import ua.com.foxmineded.universitycms.dao.AdministratorRepository;
import ua.com.foxmineded.universitycms.dao.AvatarRepository;
import ua.com.foxmineded.universitycms.dao.StudentRepository;
import ua.com.foxmineded.universitycms.dao.TeacherRepository;
import ua.com.foxmineded.universitycms.dto.AvatarDto;
import ua.com.foxmineded.universitycms.entities.impl.Avatar;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.AvatarService;

@SpringBootTest(classes = { AvatarServiceImpl.class, TypeMapConfig.class })
class AvatarServiceImplTest {
	@MockBean
	AvatarRepository avatarRepository;
	@MockBean
	StudentRepository studentRepository;
	@MockBean
	AdministratorRepository administratorRepository;
	@MockBean
	TeacherRepository teacherRepository;
	@Autowired
	AvatarService avatarService;

	@Test
	void testSave_AskSaveInvalidFile_ExceptionShouldBeThrown() {
		AvatarDto avatarDto = Instancio.of(AvatarDto.class).set(field(AvatarDto::getContentType), "Invalid").create();
		String message = "The uploaded file is not an image";
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			avatarService.save(avatarDto);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testSave_AskSaveValidFile_OperationShouldSucceed() {
		AvatarDto avatarDto = Instancio.of(AvatarDto.class).set(field(AvatarDto::getContentType), "image").create();
		Avatar avatar = Instancio.create(Avatar.class);
		when(avatarRepository.save(any(Avatar.class))).thenReturn(avatar);
		assertDoesNotThrow(() -> {
			avatarService.save(avatarDto);
		});
	}
}
