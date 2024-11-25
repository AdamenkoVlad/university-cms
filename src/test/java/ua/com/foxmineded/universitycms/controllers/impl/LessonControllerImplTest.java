package ua.com.foxmineded.universitycms.controllers.impl;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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

import ua.com.foxmineded.universitycms.controllers.LessonController;
import ua.com.foxmineded.universitycms.dto.AdministratorDto;
import ua.com.foxmineded.universitycms.dto.LessonDto;
import ua.com.foxmineded.universitycms.dto.UserDto;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.services.LessonService;

@WithMockUser(username = "admin", password = "1234", roles = "ADMINISTRATOR")
@WebMvcTest({ LessonController.class })
class LessonControllerImplTest {
	@Autowired
	MockMvc mockMvc;
	@MockBean
	ModelMapper modelMapper;
	@MockBean
	LessonService lessonService;
	@MockBean
	UserDetailsService userDetailsService;

	@Test
	void testFindAll_AskFindValidLessons_LessonsShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		LessonDto lessonDto = Instancio.create(LessonDto.class);
		when(lessonService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Stream.of(lessonDto).toList(),
				PageRequest.of(page, size), Stream.of(lessonDto).toList().size()));
		MockHttpServletRequestBuilder requestBuilder = get("/lessons")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("lessons", Stream.of(lessonDto).toList()),
						model().attribute("principal", (UserDto) administratorDto), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1L), model().attribute("totalPages", 1),
						model().attribute("pageSize", 10), model().attribute("currentPagePath", "/lessons"))
				.andExpect(view().name("lessons")).andDo(print());
	}

	@Test
	void testFindAllByLessonDate_AskFindValidLessons_LessonsShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		LessonDto lessonDto = Instancio.create(LessonDto.class);
		when(lessonService.findAllByLessonDate(any(LocalDate.class), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(lessonDto).toList(), PageRequest.of(page, size), Stream.of(lessonDto).toList().size()));
		MockHttpServletRequestBuilder requestBuilder = get("/lessons/{lessonDate}/findAllByLessonDate",
				lessonDto.getLessonDate().toString()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("lessons", Stream.of(lessonDto).toList()),
						model().attribute("principal", (UserDto) administratorDto), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1L), model().attribute("totalPages", 1),
						model().attribute("pageSize", 10),
						model().attribute("currentPagePath",
								"/lessons/%s/findAllByLessonDate".formatted(lessonDto.getLessonDate())))
				.andExpect(view().name("lessons")).andDo(print());
	}

	@Test
	void testFindAllByCourseId_AskFindValidLessons_LessonsShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		LessonDto lessonDto = Instancio.create(LessonDto.class);
		when(lessonService.findAllByCourseId(anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(lessonDto).toList(), PageRequest.of(page, size), Stream.of(lessonDto).toList().size()));
		MockHttpServletRequestBuilder requestBuilder = get("/lessons/{courseId}/findAllByCourseId",
				lessonDto.getCourseId()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("lessons", Stream.of(lessonDto).toList()),
						model().attribute("principal", (UserDto) administratorDto), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1L), model().attribute("totalPages", 1),
						model().attribute("pageSize", 10),
						model().attribute("currentPagePath",
								"/lessons/%d/findAllByCourseId".formatted(lessonDto.getCourseId())))
				.andExpect(view().name("lessons")).andDo(print());
	}

	@Test
	void testFindAllByCourseName_AskFindValidLessons_LessonsShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		LessonDto lessonDto = Instancio.create(LessonDto.class);
		when(lessonService.findAllByCourseName(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(lessonDto).toList(), PageRequest.of(page, size), Stream.of(lessonDto).toList().size()));
		MockHttpServletRequestBuilder requestBuilder = get("/lessons/{courseName}/findAllByCourseName",
				lessonDto.getCourseName()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("lessons", Stream.of(lessonDto).toList()),
						model().attribute("principal", (UserDto) administratorDto), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1L), model().attribute("totalPages", 1),
						model().attribute("pageSize", 10),
						model().attribute("currentPagePath",
								"/lessons/%s/findAllByCourseName".formatted(lessonDto.getCourseName())))
				.andExpect(view().name("lessons")).andDo(print());
	}

	@Test
	void testFindAllByRoomId_AskFindValidLessons_LessonsShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		LessonDto lessonDto = Instancio.create(LessonDto.class);
		when(lessonService.findAllByRoomId(anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(lessonDto).toList(), PageRequest.of(page, size), Stream.of(lessonDto).toList().size()));
		MockHttpServletRequestBuilder requestBuilder = get("/lessons/{roomId}/findAllByRoomId", lessonDto.getRoomId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("lessons", Stream.of(lessonDto).toList()),
						model().attribute("principal", (UserDto) administratorDto), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1L), model().attribute("totalPages", 1),
						model().attribute("pageSize", 10),
						model().attribute("currentPagePath",
								"/lessons/%d/findAllByRoomId".formatted(lessonDto.getRoomId())))
				.andExpect(view().name("lessons")).andDo(print());
	}

	@Test
	void testFindById_AskReturnLessonViewIfIdExists_ViewShouldBeReturned() throws Exception {
		LessonDto lessonDto = Instancio.create(LessonDto.class);
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getLogin), "admin")
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR).create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		when(lessonService.findById(lessonDto.getId())).thenReturn(lessonDto);
		MockHttpServletRequestBuilder requestBuilder = get("/lessons/{id}/findById", lessonDto.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("principal", (UserDto) administratorDto),
						model().attribute("lessons", Matchers.contains(lessonDto)), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1), model().attribute("totalPages", 1),
						model().attribute("pageSize", 1),
						model().attribute("currentPagePath", "/lessons/%d/findById".formatted(lessonDto.getId())))
				.andExpect(view().name("lessons")).andDo(print());
	}

	@Test
	void testCreateLesson_AskReturnLessonCreateView_ViewShouldBeReturned() throws Exception {
		String referer = "http://localhost:8080/groups";
		MockHttpServletRequestBuilder requestBuilder = get("/lessons/new")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).header("referer", referer);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attributeExists("lesson"),
						model().attribute("referer", referer.replace("http://localhost:8080", "")))
				.andExpect(view().name("lesson-create")).andDo(print());
	}

	@Test
	void testUpdateLesson_AskReturnLessonUpdateView_ViewShouldBeReturned() throws Exception {
		LessonDto lessonDto = Instancio.create(LessonDto.class);
		when(lessonService.findById(lessonDto.getId())).thenReturn(lessonDto);
		String referer = "http://localhost:8080/lessons/%d/update".formatted(lessonDto.getId());
		MockHttpServletRequestBuilder requestBuilder = get("/lessons/{id}/update", lessonDto.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).header("referer", referer);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attributeExists("lesson"),
						model().attribute("referer", referer.replace("http://localhost:8080", "")))
				.andExpect(view().name("lesson-update")).andDo(print());
	}

	@Test
	void testDeleteLesson_AskDeleteLesson_LessonShouldBeDeleted() throws Exception {
		LessonDto lessonDto = Instancio.create(LessonDto.class);
		String referer = "/lessons";
		MockHttpServletRequestBuilder requestBuilder = post("/lessons/{id}/delete", lessonDto.getId())
				.header("referer", referer).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).with(csrf());
		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(referer))
				.andDo(print());
	}

	@Test
	void testSaveLesson_AskSaveLesson_ModelShouldBeSaved() throws Exception {
		LessonDto lessonDto = Instancio.create(LessonDto.class);
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		String referer = "/lessons";
		formData.add("lessonNumber", lessonDto.getLessonNumber().toString());
		formData.add("referer", referer);
		formData.add("viewName", "lesson-update");
		MockHttpServletRequestBuilder requestBuilder = post("/lessons/save").contentType(MediaType.MULTIPART_FORM_DATA)
				.params(formData).with(csrf());

		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/lessons"))
				.andDo(print());
	}

}
