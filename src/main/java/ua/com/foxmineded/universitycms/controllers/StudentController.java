package ua.com.foxmineded.universitycms.controllers;

import java.security.Principal;

import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;
import ua.com.foxmineded.universitycms.dto.StudentDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface StudentController {
	String findById(Long courseId, Long groupId, String purpose, Model model, Principal principal, Long id)
			throws ServiceException;

	String findByPassportNumber(Long courseId, Long groupId, String purpose, Model model, Principal principal,
			String passportNumber) throws ServiceException;

	String findByLogin(Long courseId, Long groupId, String purpose, Model model, Principal principal, String login)
			throws ServiceException;

	String findByTelephoneNumber(Long courseId, Long groupId, String purpose, Model model, Principal principal,
			String telephoneNumber) throws ServiceException;

	String findAll(int page, int size, Long courseId, Long groupId, String purpose, Principal principal, Model model);

	String findAllByName(int page, int size, Long courseId, Long groupId, String purpose, Principal principal,
			Model model, String name);

	String findAllByGroupName(int page, int size, Long courseId, Long groupId, String purpose, Principal principal,
			Model model, String groupName);

	String findAllByCourseName(int page, int size, Long courseId, Long groupId, String purpose, Principal principal,
			Model model, String courseName);

	String findAllByTeacherName(int page, int size, Long courseId, Long groupId, String purpose, Principal principal,
			Model model, String teacherName);

	String findAllBySpecialization(int page, int size, Long courseId, Long groupId, String purpose, Principal principal,
			Model model, String specialization) throws ServiceException;

	String createStudent(Model model, HttpServletRequest request);

	String saveStudent(StudentDto studentDto, Model model, HttpServletRequest request)
			throws ServiceDataIntegrityException;

	String updateStudent(Long id, Model model, HttpServletRequest request) throws ServiceException;

	String deleteStudent(Long id, HttpServletRequest request) throws ServiceException;
}
