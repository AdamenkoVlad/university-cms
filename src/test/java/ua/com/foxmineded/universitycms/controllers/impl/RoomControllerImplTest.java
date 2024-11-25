package ua.com.foxmineded.universitycms.controllers.impl;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import java.util.HashSet;
import java.util.stream.Stream;
import org.hamcrest.Matchers;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import jakarta.validation.ConstraintViolationException;
import ua.com.foxmineded.universitycms.controllers.RoomController;
import ua.com.foxmineded.universitycms.dto.AdministratorDto;
import ua.com.foxmineded.universitycms.dto.RoomDto;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.RoomService;

@WithMockUser(username = "admin", password = "1234", roles = "ADMINISTRATOR")
@WebMvcTest({ RoomController.class, UserDetailsService.class })
class RoomControllerImplTest {
	@MockBean
	RoomService roomService;
	@MockBean
	UserDetailsService userDetailsService;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	RoomController roomController;

	@Test
	void testFindAll_AskFindValidRooms_RoomsShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		RoomDto roomDto = Instancio.create(RoomDto.class);
		when(roomService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Stream.of(roomDto).toList(),
				PageRequest.of(page, size), Stream.of(roomDto).toList().size()));

		MockHttpServletRequestBuilder requestBuilder = get("/rooms")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("rooms", Stream.of(roomDto).toList()),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1L),
						model().attribute("totalPages", 1), model().attribute("pageSize", 10),
						model().attribute("currentPagePath", "/rooms"))
				.andExpect(view().name("rooms")).andDo(print());
	}

	@Test
	void testFindAllByFloor_AskReturnRoomsViewIfFloorExists_ViewShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		RoomDto roomDto = Instancio.create(RoomDto.class);
		when(roomService.findAllByFloor(anyInt(), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(roomDto).toList(), PageRequest.of(page, size), Stream.of(roomDto).toList().size()));
		MockHttpServletRequestBuilder requestBuilder = get("/rooms/{floor}/findAllByFloor", roomDto.getRoomNumber())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("rooms", Stream.of(roomDto).toList()),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1L),
						model().attribute("totalPages", 1), model().attribute("pageSize", 10),
						model().attribute("currentPagePath",
								"/rooms/%d/findAllByFloor".formatted(roomDto.getRoomNumber())))
				.andExpect(view().name("rooms")).andDo(print());
	}

	@Test
	void testFindById_AskReturnRoomsViewIfIdExists_ViewShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		RoomDto roomDto = Instancio.create(RoomDto.class);
		when(roomService.findById(roomDto.getId())).thenReturn(roomDto);
		MockHttpServletRequestBuilder requestBuilder = get("/rooms/{id}/findById", roomDto.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("rooms", Matchers.contains(roomDto)),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath", "/rooms/%d/findById".formatted(roomDto.getId())))
				.andExpect(view().name("rooms")).andDo(print());
	}

	@Test
	void testFindById_AskReturnRoomsViewIfIdDoesNotExist_ExceptionShouldBeThrown() throws Exception {
		Long id = -1L;
		String message = "There was no room with id = %d".formatted(id);
		when(roomService.findById(id)).thenThrow(new ServiceException(message));
		MockHttpServletRequestBuilder requestBuilder = get("/rooms/{id}/findById", id)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.header("referer", "http://localhost:8080/rooms/%d/findById".formatted(id));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ServiceException))
				.andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
	}

	@Test
	void testFindByRoomNumber_AskReturnRoomsViewIfRoomNumberExists_ViewShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		RoomDto roomDto = Instancio.create(RoomDto.class);
		when(roomService.findByRoomNumber(roomDto.getRoomNumber())).thenReturn(roomDto);
		MockHttpServletRequestBuilder requestBuilder = get("/rooms/{roomNumber}/findByRoomNumber",
				roomDto.getRoomNumber()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("rooms", Matchers.contains(roomDto)),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath",
								"/rooms/%d/findByRoomNumber".formatted(roomDto.getRoomNumber())))
				.andExpect(view().name("rooms")).andDo(print());
	}

	@Test
	void testFindByRoomNumber_AskReturnRoomsViewIfRoomNumberDoesNotExist_ExceptionShouldBeThrown() throws Exception {
		int roomNumber = -1;
		String message = "There was no room number = %d".formatted(roomNumber);
		when(roomService.findByRoomNumber(roomNumber)).thenThrow(new ServiceException(message));
		MockHttpServletRequestBuilder requestBuilder = get("/rooms/{roomNumber}/findByRoomNumber", roomNumber)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.header("referer", "http://localhost:8080/rooms/%d/findByRoomNumber".formatted(roomNumber));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ServiceException))
				.andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
	}

	@Test
	void testFindByLessonId_AskReturnRoomsViewIfLessonIdExists_ViewShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		Long lessonId = 1L;
		RoomDto roomDto = Instancio.create(RoomDto.class);
		when(roomService.findByLessonId(lessonId)).thenReturn(roomDto);
		MockHttpServletRequestBuilder requestBuilder = get("/rooms/{lessonId}/findByLessonId", lessonId)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("rooms", Matchers.contains(roomDto)),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath", "/rooms/%d/findByLessonId".formatted(lessonId)))
				.andExpect(view().name("rooms")).andDo(print());
	}

	@Test
	void testFindByLessonId_AskReturnRoomsViewIfLessonIdDoesNotExist_ExceptionShouldBeThrown() throws Exception {
		Long lessonId = -1L;
		String message = "There was no room with lesson id = %d".formatted(lessonId);
		when(roomService.findByLessonId(lessonId)).thenThrow(new ServiceException(message));
		MockHttpServletRequestBuilder requestBuilder = get("/rooms/{lessonId}/findByLessonId", lessonId)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.header("referer", "http://localhost:8080/rooms/%d/findByLessonId".formatted(lessonId));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ServiceException))
				.andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
	}

	@Test
	void testCreateRoom_AskReturnRoomCreateView_ViewShouldBeReturned() throws Exception {
		String referer = "http://localhost:8080/rooms";
		MockHttpServletRequestBuilder requestBuilder = get("/rooms/new")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).header("referer", referer);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attributeExists("room"),
						model().attribute("referer", referer.replace("http://localhost:8080", "")))
				.andExpect(view().name("room-create")).andDo(print());
	}

	@Test
	void testUpdateRoom_AskReturnRoomUpdateView_ViewShouldBeReturned() throws Exception {
		RoomDto roomDto = Instancio.create(RoomDto.class);
		when(roomService.findById(roomDto.getId())).thenReturn(roomDto);
		String referer = "http://localhost:8080/rooms/%d/update".formatted(roomDto.getId());
		MockHttpServletRequestBuilder requestBuilder = get("/rooms/{id}/update", roomDto.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).header("referer", referer);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attributeExists("room"),
						model().attribute("referer", referer.replace("http://localhost:8080", "")))
				.andExpect(view().name("room-update")).andDo(print());
	}

	@Test
	void testDeleteRoom_AskDeleteRoom_RoomShouldBeDeleted() throws Exception {
		RoomDto roomDto = Instancio.create(RoomDto.class);
		String referer = "/rooms";
		MockHttpServletRequestBuilder requestBuilder = post("/rooms/{id}/delete", roomDto.getId())
				.header("referer", referer).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).with(csrf());
		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(referer))
				.andDo(print());
	}

	@Test
	void testSaveRoom_AskSaveRoom_ModelShouldBeSaved() throws Exception {
		RoomDto roomDto = Instancio.create(RoomDto.class);
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		String referer = "/rooms";
		formData.add("roomNumber", String.valueOf(roomDto.getRoomNumber()));
		formData.add("referer", referer);
		formData.add("viewName", "room-update");
		MockHttpServletRequestBuilder requestBuilder = post("/rooms/save").contentType(MediaType.MULTIPART_FORM_DATA)
				.params(formData).with(csrf());

		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/rooms"))
				.andDo(print());
	}

	@Test
	void testSaveRoom_AskSaveRoomIfViewRoomCreateAndServiceDataIntegrityException_ShouldReturnRoomCreate()
			throws Exception {
		RoomDto roomDto = Instancio.create(RoomDto.class);
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		String referer = "/rooms";
		formData.add("roomNumber", String.valueOf(roomDto.getRoomNumber()));
		formData.add("referer", referer);
		formData.add("viewName", "room-create");
		when(roomService.save(any(RoomDto.class))).thenThrow(new ServiceDataIntegrityException("error", "error"));
		MockHttpServletRequestBuilder requestBuilder = post("/rooms/save").contentType(MediaType.MULTIPART_FORM_DATA)
				.params(formData).with(csrf());
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(view().name("room-create")).andDo(print());
	}

	@Test
	void testSaveRoom_AskSaveRoomIfViewRoomCreateAndConstraintViolationException_ShouldReturnRoomCreate()
			throws Exception {
		RoomDto roomDto = Instancio.create(RoomDto.class);
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		String referer = "/rooms";
		formData.add("roomNumber", String.valueOf(roomDto.getRoomNumber()));
		formData.add("referer", referer);
		formData.add("viewName", "room-create");
		when(roomService.save(any(RoomDto.class))).thenThrow(new ConstraintViolationException(new HashSet<>()));
		MockHttpServletRequestBuilder requestBuilder = post("/rooms/save").contentType(MediaType.MULTIPART_FORM_DATA)
				.params(formData).with(csrf());
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(view().name("room-create")).andDo(print());
	}
}
