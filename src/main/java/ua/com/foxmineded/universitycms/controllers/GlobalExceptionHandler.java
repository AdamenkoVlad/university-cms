package ua.com.foxmineded.universitycms.controllers;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface GlobalExceptionHandler {
	public String handleException(ServiceException e, Model model, HttpServletRequest request);
}
