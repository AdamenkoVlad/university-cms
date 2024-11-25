package ua.com.foxmineded.universitycms.controllers.impl;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import ua.com.foxmineded.universitycms.config.TypeMapConfig;
import ua.com.foxmineded.universitycms.controllers.TeacherController;
import ua.com.foxmineded.universitycms.dto.AdministratorDto;
import ua.com.foxmineded.universitycms.dto.TeacherDto;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.TeacherService;
import ua.com.foxmineded.universitycms.utils.TeacherPhotoReader;

@WithMockUser(username = "admin", password = "1234", roles = "ADMINISTRATOR")
@WebMvcTest({ TeacherController.class, TeacherPhotoReader.class, TypeMapConfig.class, UserDetailsService.class })
class TeacherControllerImplTest {
	@MockBean
	TeacherService teacherService;
	@MockBean
	UserDetailsService userDetailsService;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	TeacherPhotoReader photoReader;
	@Autowired
	TeacherController teacherController;

	@Test
	void testFindAll_AskFindValidTeacher_TeachersShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		when(teacherService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Stream.of(teacherDto).toList(),
				PageRequest.of(page, size), Stream.of(teacherDto).toList().size()));

		MockHttpServletRequestBuilder requestBuilder = get("/teachers")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("teachers", Stream.of(teacherDto).toList()),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1L),
						model().attribute("totalPages", 1), model().attribute("pageSize", 10),
						model().attribute("currentPagePath", "/teachers"))
				.andExpect(view().name("teachers")).andDo(print());
	}

	@Test
	void testFindAllByName_AskFindValidTeacher_TeacherShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		when(teacherService.findAllByName(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(teacherDto).toList(), PageRequest.of(page, size), Stream.of(teacherDto).toList().size()));

		MockHttpServletRequestBuilder requestBuilder = get("/teachers/{name}/findAllByName", teacherDto.getName())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("teachers", Stream.of(teacherDto).toList()),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1L),
						model().attribute("totalPages", 1), model().attribute("pageSize", 10),
						model().attribute("currentPagePath",
								"/teachers/%s/findAllByName".formatted(teacherDto.getName())))
				.andExpect(view().name("teachers")).andDo(print());
	}

	@Test
	void testFindById_AskReturnTeacherViewIfIdExists_ViewShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		when(teacherService.findById(teacherDto.getId())).thenReturn(teacherDto);
		MockHttpServletRequestBuilder requestBuilder = get("/teachers/{id}/findById", teacherDto.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("teachers", Matchers.contains(teacherDto)),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath", "/teachers/%d/findById".formatted(teacherDto.getId())))
				.andExpect(view().name("teachers")).andDo(print());
	}

	@Test
	void testFindById_AskReturnTeacherViewIfIdDoesNotExists_ExceptionShouldBeThrown() throws Exception {
		Long id = -1L;
		String message = "There was no teacher with id = %d".formatted(id);
		when(teacherService.findById(id)).thenThrow(new ServiceException(message));
		MockHttpServletRequestBuilder requestBuilder = get("/teachers/{id}/findById", id)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.header("referer", "http://localhost:8080/teachers/%d/findById".formatted(id));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ServiceException))
				.andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
	}

	@Test
	void testFindByLogin_AskFindValidTeacher_TeacherShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		when(teacherService.findByLogin(teacherDto.getLogin())).thenReturn(teacherDto);
		MockHttpServletRequestBuilder requestBuilder = get("/teachers/{login}/findByLogin", teacherDto.getLogin())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("teachers", Matchers.contains(teacherDto)),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath",
								"/teachers/%s/findByLogin".formatted(teacherDto.getLogin())))
				.andExpect(view().name("teachers")).andDo(print());
	}

	@Test
	void testFindByLogin_AskFindTeacherIfLoginDoesNotExist_ExceptionShouldBeThrown() throws Exception {
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		String message = "The was no teacher with login = %s".formatted(teacherDto.getLogin());
		when(teacherService.findByLogin(teacherDto.getLogin())).thenThrow(new ServiceException(message));
		MockHttpServletRequestBuilder requestBuilder = get("/teachers/{login}/findByLogin", teacherDto.getLogin())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.header("referer", "http://localhost:8080/teachers/%s/findByLogin".formatted(teacherDto.getLogin()));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ServiceException))
				.andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
	}

	@Test
	void testFindByPassportNumber_AskFindValidTeacher_TeacherShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		when(teacherService.findByPassportNumber(teacherDto.getPassportNumber())).thenReturn(teacherDto);
		MockHttpServletRequestBuilder requestBuilder = get("/teachers/{passportNumber}/findByPassportNumber",
				teacherDto.getPassportNumber()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("teachers", Matchers.contains(teacherDto)),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath",
								"/teachers/%s/findByPassportNumber".formatted(teacherDto.getPassportNumber())))
				.andExpect(view().name("teachers")).andDo(print());
	}

	@Test
	void testFindByPassportNumber_AskFindTeacherIfPassportNumberDoesNotExist_ExceptionShouldBeThrown()
			throws Exception {
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		String message = "The was no teacher with passport number = %s".formatted(teacherDto.getPassportNumber());
		when(teacherService.findByPassportNumber(teacherDto.getPassportNumber()))
				.thenThrow(new ServiceException(message));
		MockHttpServletRequestBuilder requestBuilder = get("/teachers/{passportNumber}/findByPassportNumber",
				teacherDto.getPassportNumber()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.header("referer", "http://localhost:8080/teachers/%s/findByPassportNumber"
						.formatted(teacherDto.getPassportNumber()));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ServiceException))
				.andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
	}

	@Test
	void testFindByTelephoneNumber_AskFindValidTeacher_TeacherShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		when(teacherService.findByTelephoneNumber(teacherDto.getTelephoneNumber())).thenReturn(teacherDto);
		MockHttpServletRequestBuilder requestBuilder = get("/teachers/{telephoneNumber}/findByTelephoneNumber",
				teacherDto.getTelephoneNumber()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("teachers", Matchers.contains(teacherDto)),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath",
								"/teachers/%s/findByTelephoneNumber".formatted(teacherDto.getTelephoneNumber())))
				.andExpect(view().name("teachers")).andDo(print());
	}

	@Test
	void testFindByTelephoneNumber_AskFindTeacherIfTelephoneNumberDoesNotExist_ExceptionShouldBeThrown()
			throws Exception {
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		String message = "The was no teacher with telephone number = %s".formatted(teacherDto.getTelephoneNumber());
		when(teacherService.findByTelephoneNumber(teacherDto.getTelephoneNumber()))
				.thenThrow(new ServiceException(message));
		MockHttpServletRequestBuilder requestBuilder = get("/teachers/{telephoneNumber}/findByTelephoneNumber",
				teacherDto.getTelephoneNumber()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.header("referer", "http://localhost:8080/teachers/%s/findByTelephoneNumber"
						.formatted(teacherDto.getTelephoneNumber()));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ServiceException))
				.andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
	}

	@Test
	void testFindByCourseName_AskFindValidTeachers_TeachersShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		String courseName = "Math";
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		when(teacherService.findByCourseName(courseName)).thenReturn(teacherDto);

		MockHttpServletRequestBuilder requestBuilder = get("/teachers/{courseName}/findByCourseName", courseName)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("teachers", Stream.of(teacherDto).toList()),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath", "/teachers/%s/findByCourseName".formatted(courseName)))
				.andExpect(view().name("teachers")).andDo(print());
	}

	@Test
	void testFindByCourseId_AskFindByValidCourseId_TeachersShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		Long courseId = 1L;
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		when(teacherService.findByCourseId(anyLong())).thenReturn(teacherDto);

		MockHttpServletRequestBuilder requestBuilder = get("/teachers/{courseId}/findByCourseId", courseId)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("teachers", Stream.of(teacherDto).toList()),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath", "/teachers/%d/findByCourseId?courseId=null".formatted(courseId)))
				.andExpect(view().name("teachers")).andDo(print());
	}

	@Test
	void testFindByCourseId_AskFindByInvalidCourseId_ExceptionShouldBeThrown() throws Exception {
		Long courseId = -1L;
		String message = "There was no teacher with course id = %d".formatted(courseId);
		when(teacherService.findByCourseId(anyLong())).thenThrow(new ServiceException(message));

		MockHttpServletRequestBuilder requestBuilder = get("/teachers/{courseId}/findByCourseId", courseId)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.header("referer", "http://localhost:8080/teachers/%d/findByCourseId".formatted(courseId));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ServiceException))
				.andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
	}

	@Test
	void testFindByLessonId_AskFindByValidLessonId_TeachersShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		Long lessonId = 1L;
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		when(teacherService.findByLessonId(anyLong())).thenReturn(teacherDto);

		MockHttpServletRequestBuilder requestBuilder = get("/teachers/{lessonId}/findByLessonId", lessonId)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("teachers", Stream.of(teacherDto).toList()),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath", "/teachers/%d/findByLessonId".formatted(lessonId)))
				.andExpect(view().name("teachers")).andDo(print());
	}

	@Test
	void testFindByLessonId_AskFindByInvalidLessonId_ExceptionShouldBeThrown() throws Exception {
		Long lessonId = -1L;
		String message = "There was no teacher with lesson id = %d".formatted(lessonId);
		when(teacherService.findByLessonId(anyLong())).thenThrow(new ServiceException(message));

		MockHttpServletRequestBuilder requestBuilder = get("/teachers/{lessonId}/findByLessonId", lessonId)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.header("referer", "http://localhost:8080/teachers/%d/findByLessonId".formatted(lessonId));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ServiceException))
				.andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
	}

	@Test
	void testSaveTeacher_AskSaveTeacher_ModelShouldBeSaved() throws Exception {
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		String referer = "/teachers";
		formData.add("firstName", teacherDto.getName());
		formData.add("referer", referer);
		formData.add("viewName", "teacher-update");
		MockHttpServletRequestBuilder requestBuilder = post("/teachers/save").contentType(MediaType.MULTIPART_FORM_DATA)
				.params(formData).with(csrf());

		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/teachers"))
				.andDo(print());
	}

	@Test
	void testSaveTeacher_AskSaveTeacherIfViewTeacherCreateAndServiceDataIntegrityException_ShouldReturnTeacherCreate()
			throws Exception {
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		String referer = "/teachers";
		formData.add("firstName", teacherDto.getName());
		formData.add("referer", referer);
		formData.add("viewName", "teacher-create");
		when(teacherService.save(any(TeacherDto.class))).thenThrow(new ServiceDataIntegrityException("error", "error"));
		MockHttpServletRequestBuilder requestBuilder = post("/teachers/save").contentType(MediaType.MULTIPART_FORM_DATA)
				.params(formData).with(csrf());
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(view().name("teacher-create"))
				.andDo(print());
	}

	@Test
	void testSaveTeacher_AskSaveTeacherIfViewTeacherCreateAndConstraintViolationException_ShouldReturnTeacherCreate()
			throws Exception {
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		String referer = "/students";
		formData.add("firstName", teacherDto.getName());
		formData.add("referer", referer);
		formData.add("viewName", "teacher-create");
		when(teacherService.save(any(TeacherDto.class))).thenThrow(new ConstraintViolationException(new HashSet<>()));
		MockHttpServletRequestBuilder requestBuilder = post("/teachers/save").contentType(MediaType.MULTIPART_FORM_DATA)
				.params(formData).with(csrf());

		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(view().name("teacher-create"))
				.andDo(print());
	}

	@Test
	void testCreateTeacher_AskReturnTeacherCreateView_ViewShouldBeReturned() throws Exception {
		String referer = "http://localhost:8080/teachers";
		MockHttpServletRequestBuilder requestBuilder = get("/teachers/new")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).header("referer", referer);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attributeExists("teacher"),
						model().attribute("referer", referer.replace("http://localhost:8080", "")))
				.andExpect(view().name("teacher-create")).andDo(print());
	}

	@Test
	void testUpdateTeacher_AskReturnTeacherUpdateView_ViewShouldBeReturned() throws Exception {
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		when(teacherService.findById(teacherDto.getId())).thenReturn(teacherDto);
		String referer = "http://localhost:8080/teachers/%d/update".formatted(teacherDto.getId());
		MockHttpServletRequestBuilder requestBuilder = get("/teachers/{id}/update", teacherDto.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).header("referer", referer);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attributeExists("teacher"),
						model().attribute("referer", referer.replace("http://localhost:8080", "")))
				.andExpect(view().name("teacher-update")).andDo(print());
	}

	@Test
	void testDeleteTeacher_AskDeleteTeacher_TeacherShouldBeDeleted() throws Exception {
		TeacherDto teacherDto = Instancio.create(TeacherDto.class);
		String referer = "/teachers";
		MockHttpServletRequestBuilder requestBuilder = post("/teachers/{id}/delete", teacherDto.getId())
				.header("referer", referer).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).with(csrf());
		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(referer))
				.andDo(print());
	}
}
