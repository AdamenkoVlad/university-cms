package ua.com.foxmineded.universitycms.controllers.impl;

import java.security.Principal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import ua.com.foxmineded.universitycms.controllers.HomePageController;
import ua.com.foxmineded.universitycms.dto.UserDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomePageControllerImpl implements HomePageController {
	private final UserDetailsService userDetailsService;

	@GetMapping
	@Override
	public String getHomePage(Principal principal, Model model) throws ServiceException {
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		return "home-page";
	}
}
