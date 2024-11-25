package ua.com.foxmineded.universitycms.controllers.impl;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import ua.com.foxmineded.universitycms.controllers.GlobalExceptionHandler;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

@ControllerAdvice
public class GlobalExceptionHandlerImpl implements GlobalExceptionHandler {
	@ExceptionHandler(ServiceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleException(ServiceException e, Model model, HttpServletRequest request) {
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		model.addAttribute("referer", referer);
		model.addAttribute("message", e.getMessage());
		return "exception";
	}
}
