package ua.com.foxmineded.universitycms.controllers.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;

@WebMvcTest({ LoginControllerImpl.class })
class LoginControllerImplTest {
	@Autowired
	MockMvc mockMvc;

	@WithMockUser(username = "admin", password = "1234")
	@Test
	void testLogin_AskAuthenticateValidUser_UserShouldBeAuthenticated() throws Exception {
		mockMvc.perform(formLogin().user("admin").password("1234")).andExpect(authenticated().withUsername("admin"));
	}

	@Test
	void testLogin_AskAuthenticateInvalidUser_ExceptionShouldArise() throws Exception {
		mockMvc.perform(formLogin().user("admin").password("invalid")).andExpect(unauthenticated());
	}
}
