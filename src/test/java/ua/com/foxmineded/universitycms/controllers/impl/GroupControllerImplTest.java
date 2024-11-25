package ua.com.foxmineded.universitycms.controllers.impl;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

import ua.com.foxmineded.universitycms.controllers.GroupController;
import ua.com.foxmineded.universitycms.dto.AdministratorDto;
import ua.com.foxmineded.universitycms.dto.GroupDto;
import ua.com.foxmineded.universitycms.dto.UserDto;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.services.GroupService;

@WithMockUser(username = "admin", password = "1234", roles = "ADMINISTRATOR")
@WebMvcTest({ GroupController.class })
class GroupControllerImplTest {
	@Autowired
	MockMvc mockMvc;
	@MockBean
	ModelMapper modelMapper;
	@MockBean
	GroupService groupService;
	@MockBean
	UserDetailsService userDetailsService;

	@Test
	void testFindById_AskReturnGroupViewIfIdExists_ViewShouldBeReturned() throws Exception {
		GroupDto groupDto = Instancio.create(GroupDto.class);
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getLogin), "admin")
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR).create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		when(groupService.findById(groupDto.getId())).thenReturn(groupDto);
		MockHttpServletRequestBuilder requestBuilder = get("/groups/{id}/findById", groupDto.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("principal", (UserDto) administratorDto),
						model().attribute("groups", Matchers.contains(groupDto)), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1), model().attribute("totalPages", 1),
						model().attribute("pageSize", 1),
						model().attribute("currentPagePath", "/groups/%d/findById".formatted(groupDto.getId())))
				.andExpect(view().name("groups")).andDo(print());
	}

	@Test
	void testFindByGroupName_AskReturnGroupViewIfIdExists_ViewShouldBeReturned() throws Exception {
		GroupDto groupDto = Instancio.create(GroupDto.class);
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getLogin), "admin")
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR).create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		when(groupService.findByGroupName(groupDto.getGroupName())).thenReturn(groupDto);
		MockHttpServletRequestBuilder requestBuilder = get("/groups/{groupName}/findByGroupName",
				groupDto.getGroupName()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("principal", (UserDto) administratorDto),
						model().attribute("groups", Matchers.contains(groupDto)), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1), model().attribute("totalPages", 1),
						model().attribute("pageSize", 1),
						model().attribute("currentPagePath",
								"/groups/%s/findByGroupName".formatted(groupDto.getGroupName())))
				.andExpect(view().name("groups")).andDo(print());
	}

	@Test
	void testFindAll_AskFindValidGroups_GroupsShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		GroupDto groupDto = Instancio.create(GroupDto.class);
		when(groupService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Stream.of(groupDto).toList(),
				PageRequest.of(page, size), Stream.of(groupDto).toList().size()));
		MockHttpServletRequestBuilder requestBuilder = get("/groups")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("groups", Stream.of(groupDto).toList()),
						model().attribute("principal", (UserDto) administratorDto), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1L), model().attribute("totalPages", 1),
						model().attribute("pageSize", 10), model().attribute("currentPagePath", "/groups"))
				.andExpect(view().name("groups")).andDo(print());
	}

	@Test
	void testFindAllBySpecialization_AskFindValidGroups_GroupsShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		GroupDto groupDto = Instancio.create(GroupDto.class);
		when(groupService.findAllBySpecialization(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(groupDto).toList(), PageRequest.of(page, size), Stream.of(groupDto).toList().size()));
		MockHttpServletRequestBuilder requestBuilder = get("/groups/{specialization}/findAllBySpecialization",
				groupDto.getSpecialization()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("groups", Stream.of(groupDto).toList()),
						model().attribute("principal", (UserDto) administratorDto), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1L), model().attribute("totalPages", 1),
						model().attribute("pageSize", 10),
						model().attribute("currentPagePath",
								"/groups/%s/findAllBySpecialization".formatted(groupDto.getSpecialization())))
				.andExpect(view().name("groups")).andDo(print());
	}

	@Test
	void testFindAllWithLessOrEqualStudents_AskFindValidGroups_GroupsShouldBeReturned() throws Exception {
		int studentAmount = 10;
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getRole), Role.ADMINISTRATOR)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		int page = 0;
		int size = 1;
		GroupDto groupDto = Instancio.create(GroupDto.class);
		when(groupService.findAllWithLessOrEqualStudents(anyInt(), any(Pageable.class))).thenReturn(new PageImpl<>(
				Stream.of(groupDto).toList(), PageRequest.of(page, size), Stream.of(groupDto).toList().size()));
		MockHttpServletRequestBuilder requestBuilder = get("/groups/{studentAmount}/findAllWithLessOrEqualStudents",
				studentAmount).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attribute("groups", Stream.of(groupDto).toList()),
						model().attribute("principal", (UserDto) administratorDto), model().attribute("currentPage", 1),
						model().attribute("totalItems", 1L), model().attribute("totalPages", 1),
						model().attribute("pageSize", 10),
						model().attribute("currentPagePath",
								"/groups/%d/findAllWithLessOrEqualStudents".formatted(studentAmount)))
				.andExpect(view().name("groups")).andDo(print());
	}

	@Test
	void testCreateGroup_AskReturnGroupsCreateView_ViewShouldBeReturned() throws Exception {
		String referer = "http://localhost:8080/groups";
		MockHttpServletRequestBuilder requestBuilder = get("/groups/new")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).header("referer", referer);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attributeExists("group"),
						model().attribute("referer", referer.replace("http://localhost:8080", "")))
				.andExpect(view().name("group-create")).andDo(print());
	}

	@Test
	void testUpdateGroup_AskReturnGroupUpdateView_ViewShouldBeReturned() throws Exception {
		GroupDto groupDto = Instancio.create(GroupDto.class);
		when(groupService.findById(groupDto.getId())).thenReturn(groupDto);
		String referer = "http://localhost:8080/groups/%d/update".formatted(groupDto.getId());
		MockHttpServletRequestBuilder requestBuilder = get("/groups/{id}/update", groupDto.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).header("referer", referer);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpectAll(model().attributeExists("group"),
						model().attribute("referer", referer.replace("http://localhost:8080", "")))
				.andExpect(view().name("group-update")).andDo(print());
	}

	@Test
	void testDeleteGroup_AskDeleteGroup_GroupShouldBeDeleted() throws Exception {
		GroupDto groupDto = Instancio.create(GroupDto.class);
		String referer = "/groups";
		MockHttpServletRequestBuilder requestBuilder = post("/groups/{id}/delete", groupDto.getId())
				.header("referer", referer).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).with(csrf());
		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(referer))
				.andDo(print());
	}

	@Test
	void testSaveGroup_AskSaveGroup_ModelShouldBeSaved() throws Exception {
		GroupDto groupDto = Instancio.create(GroupDto.class);
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		String referer = "/groups";
		formData.add("groupName", groupDto.getGroupName());
		formData.add("referer", referer);
		formData.add("viewName", "group-update");
		MockHttpServletRequestBuilder requestBuilder = post("/groups/save").contentType(MediaType.MULTIPART_FORM_DATA)
				.params(formData).with(csrf());

		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/groups"))
				.andDo(print());
	}

}
