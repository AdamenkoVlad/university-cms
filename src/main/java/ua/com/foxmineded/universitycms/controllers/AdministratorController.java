package ua.com.foxmineded.universitycms.controllers;

import java.security.Principal;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import ua.com.foxmineded.universitycms.dto.AdministratorDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface AdministratorController {
	String findById(Principal principal, Model model, Long id) throws ServiceException;

	String findByLogin(Principal principal, Model model, String login) throws ServiceException;

	String findByPassportNumber(Principal principal, Model model, String passportNumber) throws ServiceException;

	String findByName(Principal principal, int page, int size, Model model, String name);

	String findByTelephoneNumber(Principal principal, Model model, String telephoneNumber) throws ServiceException;

	String findAll(int page, int size, Principal principal, Model model) throws ServiceException;

	String createAdministrator(Model model, HttpServletRequest request);

	String saveAdministrator(AdministratorDto administratorDto, Model model, HttpServletRequest request);

	String updateAdministrator(Long id, Model model, HttpServletRequest request) throws ServiceException;

	String deleteAdministrator(Long id, HttpServletRequest request) throws ServiceException;
}
