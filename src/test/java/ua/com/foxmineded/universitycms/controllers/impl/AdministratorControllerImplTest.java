package ua.com.foxmineded.universitycms.controllers.impl;

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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.instancio.Select.field;
import java.util.Arrays;
import java.util.HashSet;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.instancio.Instancio;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import jakarta.validation.ConstraintViolationException;
import ua.com.foxmineded.universitycms.config.TypeMapConfig;
import ua.com.foxmineded.universitycms.controllers.AdministratorController;
import ua.com.foxmineded.universitycms.dto.AdministratorDto;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.AdministratorService;
import ua.com.foxmineded.universitycms.utils.AdministratorPhotoReader;

@WithMockUser(username = "admin", password = "1234", roles = "ADMINISTRATOR")
@WebMvcTest({ AdministratorController.class, AdministratorPhotoReader.class, TypeMapConfig.class,
		UserDetailsService.class })
class AdministratorControllerImplTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	AdministratorPhotoReader photoReader;
	@Autowired
	AdministratorController administratorController;
	@MockBean
	UserDetailsService userDetailsService;
	@MockBean
	AdministratorService administratorService;

	@Test
	void testFindById_AskReturnAdministratorsViewIfIdExists_ViewShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		when(administratorService.findById(administratorDto.getId())).thenReturn(administratorDto);
		MockHttpServletRequestBuilder requestBuilder = get("/administrators/{id}/findById", administratorDto.getId());
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("administrators", Arrays.asList(administratorDto)),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath",
								"/administrators/%d/findById".formatted(administratorDto.getId())))
				.andExpect(view().name("administrators")).andDo(print());
	}

	@Test
	void testFindById_AskReturnAdministratorsViewIfIdDoesNotExists_ExceptionShouldBeThrown() throws Exception {
		Long id = -1L;
		String message = "There is no administrator with id = %d".formatted(id);
		when(administratorService.findById(id)).thenThrow(new ServiceException(message));
		mockMvc.perform(
				get("/administrators/{id}/findById", id).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
						.header("referer", "http://localhost:8080/administrators/%d/findById".formatted(id)))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ServiceException))
				.andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
	}

	@Test
	void testFindByLogin_AskReturnAdministratorsViewIfLoginExists_ViewShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getLogin), "admin")
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR).create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		when(administratorService.findByLogin(administratorDto.getLogin())).thenReturn(administratorDto);
		MockHttpServletRequestBuilder requestBuilder = get("/administrators/{login}/findByLogin",
				administratorDto.getLogin());
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("administrators", Arrays.asList(administratorDto)),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath",
								"/administrators/%s/findByLogin".formatted(administratorDto.getLogin())))
				.andExpect(view().name("administrators")).andDo(print());
	}

	@Test
	void testFindByLogin_AskReturnAdministratorsViewIfLoginDoesNotExists_ExceptionShouldBeThrown() throws Exception {
		String login = "login";
		String message = "There is no administrator with login = %s".formatted(login);
		when(administratorService.findByLogin(login)).thenThrow(new ServiceException(message));
		mockMvc.perform(get("/administrators/{login}/findByLogin", login)
				.header("referer", "http://localhost:8080/administrators/%s/findByLogin".formatted(login))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ServiceException))
				.andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
	}

	@Test
	void testFindByPassportNumber_AskReturnAdministratorsViewIfPassportNumberExists_ViewShouldBeReturned()
			throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getLogin), "admin")
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR).create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		when(administratorService.findByPassportNumber(administratorDto.getPassportNumber()))
				.thenReturn(administratorDto);
		MockHttpServletRequestBuilder requestBuilder = get("/administrators/{passportNumber}/findByPassportNumber",
				administratorDto.getPassportNumber());
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("administrators", Arrays.asList(administratorDto)),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath",
								"/administrators/%s/findByPassportNumber"
										.formatted(administratorDto.getPassportNumber())))
				.andExpect(view().name("administrators")).andDo(print());
	}

	@Test
	void testFindByPassportNumber_AskReturnAdministratorsViewIfPassportNumberDoesNotExists_ExceptionShouldBeThrown()
			throws Exception {
		String passportNumber = "11114444";
		String message = "There is no administrator with passport number = %s".formatted(passportNumber);
		when(administratorService.findByPassportNumber(passportNumber)).thenThrow(new ServiceException(message));
		mockMvc.perform(
				get("/administrators/{passportNumber}/findByPassportNumber", passportNumber)
						.header("referer",
								"http://localhost:8080/administrators/%s/findByPassportNumber"
										.formatted(passportNumber))
						.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ServiceException))
				.andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
	}

	@Test
	void testFindByTelephoneNumber_AskReturnAdministratorsViewIfTelephoneNumberExists_ViewShouldBeReturned()
			throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getLogin), "admin")
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR).create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		when(administratorService.findByTelephoneNumber(administratorDto.getTelephoneNumber()))
				.thenReturn(administratorDto);
		MockHttpServletRequestBuilder requestBuilder = get("/administrators/{telephoneNumber}/findByTelephoneNumber",
				administratorDto.getTelephoneNumber());
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("administrators", Arrays.asList(administratorDto)),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath",
								"/administrators/%s/findByTelephoneNumber"
										.formatted(administratorDto.getTelephoneNumber())))
				.andExpect(view().name("administrators")).andDo(print());
	}

	@Test
	void testFindByTelephoneNumber_AskReturnAdministratorsViewIfTelephoneNumberDoesNotExists_ExceptionShouldBeThrown()
			throws Exception {
		String telephoneNumber = "88888888888";
		String message = "There is no administrator with telephone number = %s".formatted(telephoneNumber);
		when(administratorService.findByTelephoneNumber(telephoneNumber)).thenThrow(new ServiceException(message));
		mockMvc.perform(
				get("/administrators/{telephoneNumber}/findByTelephoneNumber", telephoneNumber)
						.header("referer",
								"http://localhost:8080//administrators/%s/findByTelephoneNumber"
										.formatted(telephoneNumber))
						.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ServiceException))
				.andExpect(result -> assertEquals(message, result.getResolvedException().getMessage()));
	}

	@Test
	void testFindByName_AskReturnAdministratorsViewIfNameExists_ViewShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getLogin), "admin")
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR).create();
		MockHttpServletRequestBuilder requestBuilder = get("/administrators/{name}/findByName",
				administratorDto.getName()).param("page", "1").param("size", "1");
		int page = 0;
		int size = 1;
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		when(administratorService.findAllByName(any(String.class), any(Pageable.class))).thenReturn(new PageImpl<>(
				Arrays.asList(administratorDto), PageRequest.of(page, size), Arrays.asList(administratorDto).size()));
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("administrators", Arrays.asList(administratorDto)),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1L),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath",
								"/administrators/%s/findByName".formatted(administratorDto.getName())))
				.andExpect(view().name("administrators")).andDo(print());
	}

	@Test
	void testFindAll_AskReturnAdministratorsView_ViewShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		MockHttpServletRequestBuilder requestBuilder = get("/administrators", administratorDto.getName())
				.param("page", "1").param("size", "1");
		int page = 0;
		int size = 1;
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		when(administratorService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(
				Arrays.asList(administratorDto), PageRequest.of(page, size), Arrays.asList(administratorDto).size()));
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("administrators", Arrays.asList(administratorDto)),
						model().attribute("currentPage", 1), model().attribute("totalItems", 1L),
						model().attribute("totalPages", 1), model().attribute("pageSize", 1),
						model().attribute("currentPagePath", "/administrators"))
				.andExpect(view().name("administrators")).andDo(print());
	}

	@Test
	void testCreateAdministrator_AskReturnAdministratorCreateView_ViewShouldBeReturned() throws Exception {
		String referer = "http://localhost:8080/administrators";
		MockHttpServletRequestBuilder requestBuilder = get("/administrators/new").header("referer", referer);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attributeExists("administrator"), model().attribute("referer", "/administrators"))
				.andExpect(view().name("administrator-create")).andDo(print());
	}

	@Test
	void testUpdateAdministrator_AskReturnAdministratorUpdateView_ViewShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.create(AdministratorDto.class);
		when(administratorService.findById(administratorDto.getId())).thenReturn(administratorDto);
		String referer = "http://localhost:8080/administrators";
		MvcResult result = mockMvc
				.perform(get("/administrators/{id}/update", administratorDto.getId()).header("referer", referer))
				.andExpect(status().isOk())
				.andExpectAll(model().attribute("administrator", administratorDto),
						model().attribute("referer", "/administrators"))
				.andExpect(view().name("administrator-update")).andDo(print()).andReturn();

		AdministratorDto administratorDtoResult = (AdministratorDto) result.getModelAndView().getModel()
				.get("administrator");
		assertNotNull(administratorDtoResult);
	}

	@Test
	void testSaveAdministrator_AskSaveAdministrator_ModelShouldBeSaved() throws Exception {
		AdministratorDto administratorDto = Instancio.create(AdministratorDto.class);
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		String referer = "/administrators";
		formData.add("firstName", administratorDto.getName());
		formData.add("referer", referer);
		formData.add("viewName", "administrator-update");
		MockHttpServletRequestBuilder requestBuilder = post("/administrators/save")
				.contentType(MediaType.MULTIPART_FORM_DATA).params(formData).with(csrf());

		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/administrators")).andDo(print());
	}

	@Test
	void testSaveAdministrator_AskSaveAdministratorIfViewAdministratorCreateAndServiceDataIntegrityException_ShouldReturnAdministratorCreate()
			throws Exception {
		AdministratorDto administratorDto = Instancio.create(AdministratorDto.class);
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		String referer = "/administrators";
		formData.add("firstName", administratorDto.getName());
		formData.add("referer", referer);
		formData.add("viewName", "administrator-create");
		when(administratorService.save(any(AdministratorDto.class)))
				.thenThrow(new ServiceDataIntegrityException("error", "error"));
		MockHttpServletRequestBuilder requestBuilder = post("/administrators/save")
				.contentType(MediaType.MULTIPART_FORM_DATA).params(formData).with(csrf());
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(view().name("administrator-create"))
				.andDo(print());
	}

	@Test
	void testSaveAdministrator_AskSaveAdministratorIfViewAdministratorCreateAndConstraintViolationException_ShouldReturnAdministratorCreate()
			throws Exception {
		AdministratorDto administratorDto = Instancio.create(AdministratorDto.class);
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		String referer = "/administrators";
		formData.add("firstName", administratorDto.getName());
		formData.add("referer", referer);
		formData.add("viewName", "administrator-create");
		when(administratorService.save(any(AdministratorDto.class)))
				.thenThrow(new ConstraintViolationException(new HashSet<>()));
		MockHttpServletRequestBuilder requestBuilder = post("/administrators/save")
				.contentType(MediaType.MULTIPART_FORM_DATA).params(formData).with(csrf());

		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(view().name("administrator-create"))
				.andDo(print());
	}

	@Test
	void testDeleteAdministrator_AskDeleteAdministrator_AdministratorShouldBeDeleted() throws Exception {
		AdministratorDto administratorDto = Instancio.create(AdministratorDto.class);
		String referer = "/administrators";
		MockHttpServletRequestBuilder requestBuilder = post("/administrators/{id}/delete", administratorDto.getId())
				.header("referer", referer).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).with(csrf());
		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(referer))
				.andDo(print());
	}
}
