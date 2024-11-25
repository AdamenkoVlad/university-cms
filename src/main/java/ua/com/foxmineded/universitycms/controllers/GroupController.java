package ua.com.foxmineded.universitycms.controllers;

import java.security.Principal;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import ua.com.foxmineded.universitycms.dto.GroupDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface GroupController {
	String findById(Principal principal, Model model, Long id) throws ServiceException;

	String findByGroupName(Principal principal, Model model, String groupName) throws ServiceException;

	String findByStudentId(Principal principal, Long studentId, Model model) throws ServiceException;

	String findAll(Principal principal, int page, int size, Model model);

	String findAllBySpecialization(Principal principal, int page, int size, Model model, String specialization)
			throws ServiceException;

	String findAllWithLessOrEqualStudents(Principal principal, int page, int size, Model model, int studentAmount);

	String createGroup(Model model, HttpServletRequest request);

	String saveGroup(GroupDto groupDto, Model model, HttpServletRequest request) throws ServiceDataIntegrityException;

	String updateGroup(Long id, Model model, HttpServletRequest request) throws ServiceException;

	String deleteGroup(Long id, HttpServletRequest request) throws ServiceException;

	String addStudentToGroupById(Long groupId, Long studentId, HttpServletRequest request) throws ServiceException;

	String deleteStudentFromGroupById(Long groupId, Long studentId, HttpServletRequest request) throws ServiceException;
}
