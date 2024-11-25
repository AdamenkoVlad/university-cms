package ua.com.foxmineded.universitycms.controllers.impl;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

import ua.com.foxmineded.universitycms.dto.CourseDto;
import ua.com.foxmineded.universitycms.controllers.CourseController;
import ua.com.foxmineded.universitycms.dto.AdministratorDto;
import ua.com.foxmineded.universitycms.dto.UserDto;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.services.CourseService;

@WithMockUser(username = "admin", password = "1234", roles = "ADMINISTRATOR")
@WebMvcTest({ CourseController.class })
class CourseControllerImplTest {
	@Autowired
	MockMvc mockMvc;
	@MockBean
	ModelMapper modelMapper;
	@MockBean
	CourseService courseService;
	@MockBean
	UserDetailsService userDetailsService;

	@Test
	void testFindById_AskReturnCourseViewIfIdExists_ViewShouldBeReturned() throws Exception {
		CourseDto courseDto = Instancio.create(CourseDto.class);
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getLogin), "admin")
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR).create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		when(courseService.findById(courseDto.getId())).thenReturn(courseDto);
		MockHttpServletRequestBuilder requestBuilder = get("/courses/{id}/findById", courseDto.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("principal", (UserDto) administratorDto),
						model().attribute("courses", Matchers.contains(courseDto)), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1), model().attribute("totalPages", 1),
						model().attribute("pageSize", 1),
						model().attribute("currentPagePath", "/courses/%d/findById".formatted(courseDto.getId())))
				.andExpect(view().name("courses")).andDo(print());
	}

	@Test
	void testFindByCourseName_AskReturnCourseViewIfCourseNameExists_ViewShouldBeReturned() throws Exception {
		CourseDto courseDto = Instancio.create(CourseDto.class);
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getLogin), "admin")
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR).create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		when(courseService.findByCourseName(courseDto.getCourseName())).thenReturn(courseDto);
		MockHttpServletRequestBuilder requestBuilder = get("/courses/{courseName}/findByCourseName",
				courseDto.getCourseName()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("principal", (UserDto) administratorDto),
						model().attribute("courses", Matchers.contains(courseDto)), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1), model().attribute("totalPages", 1),
						model().attribute("pageSize", 1),
						model().attribute("currentPagePath",
								"/courses/%s/findByCourseName".formatted(courseDto.getCourseName())))
				.andExpect(view().name("courses")).andDo(print());
	}

	@Test
	void testFindAllBySpecialization_AskFindValidCourses_CoursesShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		CourseDto courseDto = Instancio.create(CourseDto.class);
		when(courseService.findAllBySpecialization(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(courseDto).toList(), PageRequest.of(page, size), Stream.of(courseDto).toList().size()));
		MockHttpServletRequestBuilder requestBuilder = get("/courses/{specialization}/findAllBySpecialization",
				courseDto.getSpecialization()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("courses", Stream.of(courseDto).toList()),
						model().attribute("principal", (UserDto) administratorDto), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1L), model().attribute("totalPages", 1),
						model().attribute("pageSize", 10),
						model().attribute("currentPagePath",
								"/courses/%s/findAllBySpecialization".formatted(courseDto.getSpecialization())))
				.andExpect(view().name("courses")).andDo(print());
	}

	@Test
	void testFindAll_AskFindValidCourses_CoursesShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		CourseDto courseDto = Instancio.create(CourseDto.class);
		when(courseService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Stream.of(courseDto).toList(),
				PageRequest.of(page, size), Stream.of(courseDto).toList().size()));
		MockHttpServletRequestBuilder requestBuilder = get("/courses")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("courses", Stream.of(courseDto).toList()),
						model().attribute("principal", (UserDto) administratorDto), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1L), model().attribute("totalPages", 1),
						model().attribute("pageSize", 10), model().attribute("currentPagePath", "/courses"))
				.andExpect(view().name("courses")).andDo(print());
	}

	@Test
	void testFindAllByTeacherId_AskFindValidCourses_CoursesShouldBeReturned() throws Exception {
		Long teacherId = 10001L;
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		CourseDto courseDto = Instancio.create(CourseDto.class);
		when(courseService.findAllByTeacherId(anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(courseDto).toList(), PageRequest.of(page, size), Stream.of(courseDto).toList().size()));
		MockHttpServletRequestBuilder requestBuilder = get("/courses/{teacherId}/findAllByTeacherId", teacherId)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("courses", Stream.of(courseDto).toList()),
						model().attribute("principal", (UserDto) administratorDto), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1L), model().attribute("totalPages", 1),
						model().attribute("pageSize", 10),
						model().attribute("currentPagePath", "/courses/%d/findAllByTeacherId".formatted(teacherId)))
				.andExpect(view().name("courses")).andDo(print());
	}

	@Test
	void testFindAllByTeacherName_AskFindValidCourses_CoursesShouldBeReturned() throws Exception {
		String teacherName = "Teacher Name";
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		CourseDto courseDto = Instancio.create(CourseDto.class);
		when(courseService.findAllByTeacherName(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(courseDto).toList(), PageRequest.of(page, size), Stream.of(courseDto).toList().size()));
		MockHttpServletRequestBuilder requestBuilder = get("/courses/{teacherName}/findAllByTeacherName", teacherName)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("courses", Stream.of(courseDto).toList()),
						model().attribute("principal", (UserDto) administratorDto), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1L), model().attribute("totalPages", 1),
						model().attribute("pageSize", 10),
						model().attribute("currentPagePath", "/courses/%s/findAllByTeacherName".formatted(teacherName)))
				.andExpect(view().name("courses")).andDo(print());
	}

	@Test
	void testFindAllByRoomId_AskFindValidCourses_CoursesShouldBeReturned() throws Exception {
		Long roomId = 10001L;
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		CourseDto courseDto = Instancio.create(CourseDto.class);
		when(courseService.findAllByRoomId(anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(courseDto).toList(), PageRequest.of(page, size), Stream.of(courseDto).toList().size()));
		MockHttpServletRequestBuilder requestBuilder = get("/courses/{roomId}/findAllByRoomId", roomId)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("courses", Stream.of(courseDto).toList()),
						model().attribute("principal", (UserDto) administratorDto), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1L), model().attribute("totalPages", 1),
						model().attribute("pageSize", 10),
						model().attribute("currentPagePath", "/courses/%d/findAllByRoomId".formatted(roomId)))
				.andExpect(view().name("courses")).andDo(print());
	}

	@Test
	void testFindAllByRoomNumber_AskFindValidCourses_CoursesShouldBeReturned() throws Exception {
		int roomNumber = 1;
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		CourseDto courseDto = Instancio.create(CourseDto.class);
		when(courseService.findAllByRoomNumber(anyInt(), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(courseDto).toList(), PageRequest.of(page, size), Stream.of(courseDto).toList().size()));
		MockHttpServletRequestBuilder requestBuilder = get("/courses/{roomNumber}/findAllByRoomNumber", roomNumber)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("courses", Stream.of(courseDto).toList()),
						model().attribute("principal", (UserDto) administratorDto), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1L), model().attribute("totalPages", 1),
						model().attribute("pageSize", 10),
						model().attribute("currentPagePath", "/courses/%d/findAllByRoomNumber".formatted(roomNumber)))
				.andExpect(view().name("courses")).andDo(print());
	}

	@Test
	void testCreateCourse_AskReturnCourseCreateView_ViewShouldBeReturned() throws Exception {
		String referer = "http://localhost:8080/courses";
		MockHttpServletRequestBuilder requestBuilder = get("/courses/new")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).header("referer", referer);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attributeExists("course"),
						model().attribute("referer", referer.replace("http://localhost:8080", "")))
				.andExpect(view().name("course-create")).andDo(print());
	}

	@Test
	void testUpdateCourse_AskReturnCourseUpdateView_ViewShouldBeReturned() throws Exception {
		CourseDto courseDto = Instancio.create(CourseDto.class);
		when(courseService.findById(courseDto.getId())).thenReturn(courseDto);
		String referer = "http://localhost:8080/courses/%d/update".formatted(courseDto.getId());
		MockHttpServletRequestBuilder requestBuilder = get("/courses/{id}/update", courseDto.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).header("referer", referer);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attributeExists("course"),
						model().attribute("referer", referer.replace("http://localhost:8080", "")))
				.andExpect(view().name("course-update")).andDo(print());
	}

	@Test
	void testDeleteCourse_AskDeleteCourse_CourseShouldBeDeleted() throws Exception {
		CourseDto courseDto = Instancio.create(CourseDto.class);
		String referer = "/students";
		MockHttpServletRequestBuilder requestBuilder = post("/courses/{id}/delete", courseDto.getId())
				.header("referer", referer).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).with(csrf());
		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(referer))
				.andDo(print());
	}

	@Test
	void testSaveCourse_AskSaveCourse_ModelShouldBeSaved() throws Exception {
		CourseDto courseDto = Instancio.create(CourseDto.class);
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		String referer = "/courses";
		formData.add("courseName", courseDto.getCourseName());
		formData.add("referer", referer);
		formData.add("viewName", "course-update");
		MockHttpServletRequestBuilder requestBuilder = post("/courses/save").contentType(MediaType.MULTIPART_FORM_DATA)
				.params(formData).with(csrf());

		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/courses"))
				.andDo(print());
	}

}
