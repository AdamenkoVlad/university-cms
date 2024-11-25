package ua.com.foxmineded.universitycms.controllers;

import java.security.Principal;

import org.springframework.ui.Model;

import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface HomePageController {
	String getHomePage(Principal principal, Model model) throws ServiceException;
}
