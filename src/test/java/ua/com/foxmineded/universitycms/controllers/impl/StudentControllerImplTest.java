package ua.com.foxmineded.universitycms.controllers.impl;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.HashSet;
import java.util.stream.Stream;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.hamcrest.Matchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import ua.com.foxmineded.universitycms.config.TypeMapConfig;
import ua.com.foxmineded.universitycms.controllers.StudentController;
import ua.com.foxmineded.universitycms.dto.AdministratorDto;
import ua.com.foxmineded.universitycms.dto.StudentDto;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.StudentService;
import ua.com.foxmineded.universitycms.utils.StudentPhotoReader;

@WithMockUser(username = "admin", password = "1234", roles = "ADMINISTRATOR")
@WebMvcTest({ StudentController.class, StudentPhotoReader.class, TypeMapConfig.class, UserDetailsService.class })
class StudentControllerImplTest {
	@MockBean
	StudentService studentService;
	@MockBean
	UserDetailsService userDetailsService;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	StudentPhotoReader studentPhotoReader;
	@Autowired
	StudentController studentController;

	@Test
	void testFindById_AskReturnStudentsViewIfIdExists_ViewShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		StudentDto studentDto = Instancio.create(StudentDto.class);
		when(studentService.findById(studentDto.getId())).thenReturn(studentDto);
		MockHttpServletRequestBuilder requestBuilder = get("/students/{id}/findById", studentDto.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("students", Matchers.contains(studentDto)),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath", "/students/%d/findById".formatted(studentDto.getId())))
				.andExpect(view().name("students")).andDo(print());
	}

	@Test
	void testFindById_AskReturnStudentViewIfIdDoesNotExist_ExceptionShouldBeThrown() throws Exception {
		Long id = -1L;
		String message = "There was no student with id = %d".formatted(id);
		when(studentService.findById(id)).thenThrow(new ServiceException(message));
		MockHttpServletRequestBuilder requestBuilder = get("/students/{id}/findById", id)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.header("referer", "http://localhost:8080/students/%d/findById".formatted(id));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ServiceException))
				.andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
	}

	@Test
	void testFindByPassportNumber_AskFindValidStudent_StudentShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		StudentDto studentDto = Instancio.create(StudentDto.class);
		when(studentService.findByPassportNumber(studentDto.getPassportNumber())).thenReturn(studentDto);
		MockHttpServletRequestBuilder requestBuilder = get("/students/{passportNumber}/findByPassportNumber",
				studentDto.getPassportNumber()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("students", Matchers.contains(studentDto)),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath",
								"/students/%s/findByPassportNumber".formatted(studentDto.getPassportNumber())))
				.andExpect(view().name("students")).andDo(print());
	}

	@Test
	void testFindByPassportNumber_AskFindStudentIfPassportNumberDoesNotExist_ExceptionShouldBeThrown()
			throws Exception {
		StudentDto studentDto = Instancio.create(StudentDto.class);
		String message = "The was no student with passport number = %s".formatted(studentDto.getPassportNumber());
		when(studentService.findByPassportNumber(studentDto.getPassportNumber()))
				.thenThrow(new ServiceException(message));
		MockHttpServletRequestBuilder requestBuilder = get("/students/{passportNumber}/findByPassportNumber",
				studentDto.getPassportNumber()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.header("referer", "http://localhost:8080/students/%s/findByPassportNumber"
						.formatted(studentDto.getPassportNumber()));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ServiceException))
				.andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
	}

	@Test
	void testFindByLogin_AskFindValidStudent_StudentShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		StudentDto studentDto = Instancio.create(StudentDto.class);
		when(studentService.findByLogin(studentDto.getLogin())).thenReturn(studentDto);
		MockHttpServletRequestBuilder requestBuilder = get("/students/{login}/findByLogin", studentDto.getLogin())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("students", Matchers.contains(studentDto)),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath",
								"/students/%s/findByLogin".formatted(studentDto.getLogin())))
				.andExpect(view().name("students")).andDo(print());
	}

	@Test
	void testFindByLogin_AskFindStudentIfLoginDoesNotExist_ExceptionShouldBeThrown() throws Exception {
		StudentDto studentDto = Instancio.create(StudentDto.class);
		String message = "The was no student with login = %s".formatted(studentDto.getLogin());
		when(studentService.findByLogin(studentDto.getLogin())).thenThrow(new ServiceException(message));
		MockHttpServletRequestBuilder requestBuilder = get("/students/{login}/findByLogin", studentDto.getLogin())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.header("referer", "http://localhost:8080/students/%s/findByLogin".formatted(studentDto.getLogin()));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ServiceException))
				.andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
	}

	@Test
	void testFindByTelephoneNumber_AskFindValidStudent_StudentShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		StudentDto studentDto = Instancio.create(StudentDto.class);
		when(studentService.findByTelephoneNumber(studentDto.getTelephoneNumber())).thenReturn(studentDto);
		MockHttpServletRequestBuilder requestBuilder = get("/students/{telephoneNumber}/findByTelephoneNumber",
				studentDto.getTelephoneNumber()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("students", Matchers.contains(studentDto)),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath",
								"/students/%s/findByTelephoneNumber".formatted(studentDto.getTelephoneNumber())))
				.andExpect(view().name("students")).andDo(print());
	}

	@Test
	void testFindByTelephoneNumber_AskFindStudentIfTelephoneNumberDoesNotExist_ExceptionShouldBeThrown()
			throws Exception {
		StudentDto studentDto = Instancio.create(StudentDto.class);
		String message = "The was no student with telephone number = %s".formatted(studentDto.getTelephoneNumber());
		when(studentService.findByTelephoneNumber(studentDto.getTelephoneNumber()))
				.thenThrow(new ServiceException(message));
		MockHttpServletRequestBuilder requestBuilder = get("/students/{telephoneNumber}/findByTelephoneNumber",
				studentDto.getTelephoneNumber()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.header("referer", "http://localhost:8080/students/%s/findByTelephoneNumber"
						.formatted(studentDto.getTelephoneNumber()));
		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ServiceException))
				.andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
	}

	@Test
	void testFindAll_AskFindValidStudent_StudentsShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		StudentDto studentDto = Instancio.create(StudentDto.class);
		when(studentService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Stream.of(studentDto).toList(),
				PageRequest.of(page, size), Stream.of(studentDto).toList().size()));

		MockHttpServletRequestBuilder requestBuilder = get("/students")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("students", Stream.of(studentDto).toList()),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1L),
						model().attribute("totalPages", 1), model().attribute("pageSize", 10),
						model().attribute("currentPagePath", "/students"))
				.andExpect(view().name("students")).andDo(print());
	}

	@Test
	void testFindAllByName_AskFindValidStudent_StudentsShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		StudentDto studentDto = Instancio.create(StudentDto.class);
		when(studentService.findAllByName(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(studentDto).toList(), PageRequest.of(page, size), Stream.of(studentDto).toList().size()));

		MockHttpServletRequestBuilder requestBuilder = get("/students/{name}/findAllByName", studentDto.getName())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("students", Stream.of(studentDto).toList()),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1L),
						model().attribute("totalPages", 1), model().attribute("pageSize", 10),
						model().attribute("currentPagePath",
								"/students/%s/findAllByName".formatted(studentDto.getName())))
				.andExpect(view().name("students")).andDo(print());
	}

	@Test
	void testFindAllByGroupName_AskFindValidStudent_StudentsShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		String groupName = "AA-11";
		StudentDto studentDto = Instancio.create(StudentDto.class);
		when(studentService.findAllByGroupName(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(studentDto).toList(), PageRequest.of(page, size), Stream.of(studentDto).toList().size()));

		MockHttpServletRequestBuilder requestBuilder = get("/students/{groupName}/findAllByGroupName", groupName)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("students", Stream.of(studentDto).toList()),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1L),
						model().attribute("totalPages", 1), model().attribute("pageSize", 10),
						model().attribute("currentPagePath", "/students/%s/findAllByGroupName".formatted(groupName)))
				.andExpect(view().name("students")).andDo(print());
	}

	@Test
	void testFindAllByCourseName_AskFindValidStudent_StudentsShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		String courseName = "Math";
		StudentDto studentDto = Instancio.create(StudentDto.class);
		when(studentService.findAllByCourseName(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(studentDto).toList(), PageRequest.of(page, size), Stream.of(studentDto).toList().size()));

		MockHttpServletRequestBuilder requestBuilder = get("/students/{courseName}/findAllByCourseName", courseName)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("students", Stream.of(studentDto).toList()),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1L),
						model().attribute("totalPages", 1), model().attribute("pageSize", 10),
						model().attribute("currentPagePath", "/students/%s/findAllByCourseName".formatted(courseName)))
				.andExpect(view().name("students")).andDo(print());
	}

	@Test
	void testFindAllByTeacherName_AskFindValidStudent_StudentsShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		String teacherName = "Indiana Jones";
		StudentDto studentDto = Instancio.create(StudentDto.class);
		when(studentService.findAllByTeacherName(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(studentDto).toList(), PageRequest.of(page, size), Stream.of(studentDto).toList().size()));

		MockHttpServletRequestBuilder requestBuilder = get("/students/{teacherName}/findAllByTeacherName", teacherName)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("students", Stream.of(studentDto).toList()),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1L),
						model().attribute("totalPages", 1), model().attribute("pageSize", 10),
						model().attribute("currentPagePath",
								"/students/%s/findAllByTeacherName".formatted(teacherName)))
				.andExpect(view().name("students")).andDo(print());
	}

	@Test
	void testFindAllBySpecialization_AskFindValidStudent_StudentsShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		String specialization = "Economic";
		StudentDto studentDto = Instancio.create(StudentDto.class);
		when(studentService.findAllBySpecialization(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(studentDto).toList(), PageRequest.of(page, size), Stream.of(studentDto).toList().size()));

		MockHttpServletRequestBuilder requestBuilder = get("/students/{specialization}/findAllBySpecialization",
				specialization).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("students", Stream.of(studentDto).toList()),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1L),
						model().attribute("totalPages", 1), model().attribute("pageSize", 10),
						model().attribute("currentPagePath",
								"/students/%s/findAllBySpecialization".formatted(specialization)))
				.andExpect(view().name("students")).andDo(print());
	}

	@Test
	void testCreateStudent_AskReturnStudentCreateView_ViewShouldBeReturned() throws Exception {
		String referer = "http://localhost:8080/students";
		MockHttpServletRequestBuilder requestBuilder = get("/students/new")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).header("referer", referer);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attributeExists("student"),
						model().attribute("referer", referer.replace("http://localhost:8080", "")))
				.andExpect(view().name("student-create")).andDo(print());
	}

	@Test
	void testUpdateStudent_AskReturnStudentUpdateView_ViewShouldBeReturned() throws Exception {
		StudentDto studentDto = Instancio.create(StudentDto.class);
		when(studentService.findById(studentDto.getId())).thenReturn(studentDto);
		String referer = "http://localhost:8080/students/%d/update".formatted(studentDto.getId());
		MockHttpServletRequestBuilder requestBuilder = get("/students/{id}/update", studentDto.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).header("referer", referer);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attributeExists("student"),
						model().attribute("referer", referer.replace("http://localhost:8080", "")))
				.andExpect(view().name("student-update")).andDo(print());
	}

	@Test
	void testDeleteStudent_AskDeleteStudent_StudentShouldBeDeleted() throws Exception {
		StudentDto studentDto = Instancio.create(StudentDto.class);
		String referer = "/students";
		MockHttpServletRequestBuilder requestBuilder = post("/students/{id}/delete", studentDto.getId())
				.header("referer", referer).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).with(csrf());
		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(referer))
				.andDo(print());
	}

	@Test
	void testSaveStudent_AskSaveStudent_ModelShouldBeSaved() throws Exception {
		StudentDto studentDto = Instancio.create(StudentDto.class);
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		String referer = "/students";
		formData.add("firstName", studentDto.getName());
		formData.add("referer", referer);
		formData.add("viewName", "student-update");
		MockHttpServletRequestBuilder requestBuilder = post("/students/save").contentType(MediaType.MULTIPART_FORM_DATA)
				.params(formData).with(csrf());

		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/students"))
				.andDo(print());
	}

	@Test
	void testSaveStudent_AskSaveStudentIfViewStudentCreateAndServiceDataIntegrityException_ShouldReturnStudentCreate()
			throws Exception {
		StudentDto studentDto = Instancio.create(StudentDto.class);
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		String referer = "/students";
		formData.add("firstName", studentDto.getName());
		formData.add("referer", referer);
		formData.add("viewName", "student-create");
		when(studentService.save(any(StudentDto.class))).thenThrow(new ServiceDataIntegrityException("error", "error"));
		MockHttpServletRequestBuilder requestBuilder = post("/students/save").contentType(MediaType.MULTIPART_FORM_DATA)
				.params(formData).with(csrf());
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(view().name("student-create"))
				.andDo(print());
	}

	@Test
	void testSaveStudent_AskSaveStudentIfViewStudentCreateAndConstraintViolationException_ShouldReturnStudentCreate()
			throws Exception {
		StudentDto studentDto = Instancio.create(StudentDto.class);
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		String referer = "/students";
		formData.add("firstName", studentDto.getName());
		formData.add("referer", referer);
		formData.add("viewName", "student-create");
		when(studentService.save(any(StudentDto.class))).thenThrow(new ConstraintViolationException(new HashSet<>()));
		MockHttpServletRequestBuilder requestBuilder = post("/students/save").contentType(MediaType.MULTIPART_FORM_DATA)
				.params(formData).with(csrf());

		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(view().name("student-create"))
				.andDo(print());
	}
}
