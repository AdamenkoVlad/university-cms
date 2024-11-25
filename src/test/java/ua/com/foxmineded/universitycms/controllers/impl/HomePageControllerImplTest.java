package ua.com.foxmineded.universitycms.controllers.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.instancio.Select.field;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.instancio.Instancio;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import ua.com.foxmineded.universitycms.controllers.HomePageController;
import ua.com.foxmineded.universitycms.dto.AdministratorDto;

@WithMockUser(username = "admin", password = "1234")
@WebMvcTest({ HomePageController.class, UserDetailsService.class })
class HomePageControllerImplTest {
	@MockBean
	UserDetailsService userDetailsService;
	@Autowired
	MockMvc mockMvc;

	@Test
	void testGetHomePage_AskReturnHomePage_HomePageShouldBeReturned() throws Exception {
		AdministratorDto administratorDto = Instancio.of(AdministratorDto.class)
				.set(field(AdministratorDto::getLogin), "admin").create();
		when(userDetailsService.loadUserByUsername(administratorDto.getLogin())).thenReturn(administratorDto);
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("home-page")).andDo(print());
	}
}
