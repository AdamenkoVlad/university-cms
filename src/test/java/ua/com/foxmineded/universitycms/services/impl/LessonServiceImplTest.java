package ua.com.foxmineded.universitycms.services.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.instancio.Instancio;
import static org.instancio.Select.field;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import ua.com.foxmineded.universitycms.config.AppConfig;
import ua.com.foxmineded.universitycms.config.SecurityConfig;
import ua.com.foxmineded.universitycms.config.TypeMapConfig;
import ua.com.foxmineded.universitycms.dao.CourseRepository;
import ua.com.foxmineded.universitycms.dao.LessonRepository;
import ua.com.foxmineded.universitycms.dao.RoomRepository;
import ua.com.foxmineded.universitycms.entities.impl.Lesson;
import ua.com.foxmineded.universitycms.entities.impl.Room;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.LessonService;

@WithMockUser(roles = { "ADMINISTRATOR" })
@SpringBootTest(classes = { TypeMapConfig.class, SecurityConfig.class, LessonServiceImpl.class, AppConfig.class })
@ActiveProfiles("test")
class LessonServiceImplTest {
	@MockBean
	CourseRepository courseRepository;
	@MockBean
	UserDetailsService userDetailsService;
	@MockBean
	RoomRepository roomRepository;
	@MockBean
	LessonRepository lessonRepository;
	@Autowired
	LessonService lessonService;
	@Autowired
	ModelMapper modelMapper;

	@Test
	void testDeleteById_AskDeleteByIdIfIdInvalid_ExceptionShouldArise() {
		Long lessonId = 10001L;
		String message = "The lesson with id = %d was not found".formatted(lessonId);
		when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			lessonService.deleteById(lessonId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testDeleteById_AskDeleteByIdIfIdValid_TheDeletionShouldOccur() {
		Lesson lesson = Instancio.create(Lesson.class);
		when(lessonRepository.findById(anyLong())).thenReturn(Optional.of(lesson));
		assertDoesNotThrow(() -> {
			lessonService.deleteById(lesson.getId());
		});
	}

	@Test
	void testAddRoomById_AskAddRoomByIdIfLessonInvalid_ExceptionShouldArise() {
		Long lessonId = 10000L;
		Long roomId = 10000L;
		when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());
		String message = "The lesson with id = %d was not found".formatted(lessonId);
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			lessonService.addRoomById(lessonId, roomId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddRoomById_AskAddRoomByIdIfRoomInvalid_ExceptionShouldArise() {
		Long lessonId = 10000L;
		Long roomId = 10000L;
		Lesson lesson = Instancio.create(Lesson.class);
		String message = "The room with id = %d was not found".formatted(roomId);
		when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
		when(roomRepository.findById(roomId)).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			lessonService.addRoomById(lessonId, roomId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddRoomById_AskAddRoomByIdIfLessonAlreadyAlreadyHasRoom_ExceptionShouldArise() {
		Long lessonId = 10000L;
		Long roomId = 10000L;
		Lesson lesson = Instancio.create(Lesson.class);
		Room room = Instancio.create(Room.class);
		String message = "The lesson already has a room";
		when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
		when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			lessonService.addRoomById(lessonId, roomId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddRoomById_AskAddRoomById_AdditionShouldSucceed() {
		Long lessonId = 10000L;
		Long roomId = 10000L;
		Lesson lesson = Instancio.of(Lesson.class).ignore(field(Lesson::getRoom)).create();
		Room room = Instancio.create(Room.class);
		when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
		when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
		assertDoesNotThrow(() -> {
			lessonService.addRoomById(lessonId, roomId);
		});
	}

}
