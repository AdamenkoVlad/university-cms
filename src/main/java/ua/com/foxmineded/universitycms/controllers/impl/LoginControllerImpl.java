package ua.com.foxmineded.universitycms.controllers.impl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ua.com.foxmineded.universitycms.controllers.LoginController;

@Controller
public class LoginControllerImpl implements LoginController {

	@GetMapping("/login")
	@Override
	public String login() {
		return "login";
	}
}
