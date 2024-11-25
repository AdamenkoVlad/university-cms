package ua.com.foxmineded.universitycms.services.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.instancio.Instancio;
import static org.instancio.Select.field;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxmineded.universitycms.config.AppConfig;
import ua.com.foxmineded.universitycms.config.SecurityConfig;
import ua.com.foxmineded.universitycms.config.TypeMapConfig;
import ua.com.foxmineded.universitycms.dao.RoomRepository;
import ua.com.foxmineded.universitycms.entities.impl.Room;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.RoomService;

@WithMockUser(roles = { "ADMINISTRATOR" })
@SpringBootTest(classes = { TypeMapConfig.class, SecurityConfig.class, RoomServiceImpl.class, AppConfig.class })
@ActiveProfiles("test")
class RoomServiceImplTest {
	@MockBean
	RoomRepository roomRepository;
	@MockBean
	UserDetailsService userDetailsService;
	@Autowired
	RoomService roomService;

	@Test
	void testDeleteById_AskDeleteByIdIfIdInvalid_ExceptionShouldArise() {
		Long roomId = 10000L;
		when(roomRepository.findById(roomId)).thenReturn(Optional.empty());
		String message = "The room with id = %d was not found".formatted(roomId);
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			roomService.deleteById(roomId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@WithMockUser(roles = { "STUDENT" })
	@Test
	void testDeleteById_AskDeleteByIdIfRoleInvalid_ExceptionShouldArise() {
		Long roomId = 10000L;
		assertThrows(AccessDeniedException.class, () -> {
			roomService.deleteById(roomId);
		});
	}

	@Test
	void testDeleteById_AskDeleteById_DeletionShouldSucceed() {
		Long roomId = 10000L;
		Room room = Instancio.of(Room.class).set(field(Room::getId), roomId).create();
		when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
		assertDoesNotThrow(() -> {
			roomService.deleteById(roomId);
		});
	}
}
