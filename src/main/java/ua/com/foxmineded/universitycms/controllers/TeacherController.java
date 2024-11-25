package ua.com.foxmineded.universitycms.controllers;

import java.security.Principal;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import ua.com.foxmineded.universitycms.dto.TeacherDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface TeacherController {

	String findAll(int page, int size, String purpose, Long courseId, Principal principal, Model model);

	String findAllByName(int page, int size, String purpose, Long courseId, Principal principal, String name,
			Model model);

	String findById(String purpose, Long courseId, Long id, Principal principal, Model model) throws ServiceException;

	String findByLogin(String purpose, Long courseId, String login, Principal principal, Model model)
			throws ServiceException;

	String findByPassportNumber(String purpose, Long courseId, String passportNumber, Principal principal, Model model)
			throws ServiceException;

	String findByTelephoneNumber(String purpose, Long courseId, String telephoneNumber, Principal principal,
			Model model) throws ServiceException;

	String findByCourseName(String purpose, Long courseId, String courseName, Principal principal, Model model)
			throws ServiceException;

	String findByCourseId(String purpose, Long destinationCourseId, Long courseId, Principal principal, Model model)
			throws ServiceException;

	String findByLessonId(String purpose, Long courseId, Long lessonId, Principal principal, Model model)
			throws ServiceException;

	String createTeacher(Model model, HttpServletRequest request);

	String saveTeacher(TeacherDto teacherDto, Model model, HttpServletRequest request);

	String updateTeacher(Long id, Model model, HttpServletRequest request) throws ServiceException;

	String deleteTeacher(Long teacherId, HttpServletRequest request) throws ServiceException;
}
