package ua.com.foxmineded.universitycms.controllers;

import java.security.Principal;
import java.time.LocalDate;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import ua.com.foxmineded.universitycms.dto.LessonDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface LessonController {
	String findAll(int page, int size, String purpose, Long courseId, Principal principal, Model model);

	String findAllByLessonDate(int page, int size, String purpose, Long courseId, Principal principal, Model model,
			LocalDate lessonDate);

	String findAllByCourseId(int page, int size, String purpose, Long destinationCourseId, Principal principal,
			Model model, Long courseId);

	String findAllByCourseName(int page, int size, String purpose, Long courseId, Principal principal, Model model,
			String courseName);

	String findAllByRoomId(int page, int size, String purpose, Long courseId, Principal principal, Model model,
			Long roomId);

	String findById(String purpose, Long courseId, Principal principal, Model model, Long id) throws ServiceException;

	String addRoomById(Long lessonId, Long roomId) throws ServiceException;

	String createLesson(Model model, HttpServletRequest request);

	String updateLesson(Long id, Model model, HttpServletRequest request) throws ServiceException;

	String saveLesson(LessonDto lessonDto, Model model, HttpServletRequest request) throws ServiceException;

	String deleteLesson(Long id, HttpServletRequest request) throws ServiceException;
}
