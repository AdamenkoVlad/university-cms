package ua.com.foxmineded.universitycms.controllers;

import java.security.Principal;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import ua.com.foxmineded.universitycms.dto.CourseDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface CourseController {

	String findById(Principal principal, Model model, Long id) throws ServiceException;

	String findByCourseName(Principal principal, Model model, String courseName) throws ServiceException;

	String findAllBySpecialization(int page, int size, Principal principal, Model model, String specialization)
			throws ServiceException;

	String findAll(int page, int size, Principal principal, Model model);

	String findAllByTeacherId(int page, int size, Principal principal, Model model, Long teacherId);

	String findAllByTeacherName(int page, int size, Principal principal, Model model, String teacherName);

	String findAllByRoomId(int page, int size, Principal principal, Model model, Long roomId);

	String findAllByRoomNumber(int page, int size, Principal principal, Model model, int roomNumber);

	String findAllByStudentId(int page, int size, Principal principal, Model model, Long studentId);

	String addStudentToCourseById(Long studentId, Long courseId, HttpServletRequest request) throws ServiceException;

	String addTeacherToCourseById(Long teacherId, Long courseId) throws ServiceException;

	String deleteStudentFromCourseById(Long studentId, Long courseId, HttpServletRequest request)
			throws ServiceException;

	String deleteTeacherFromCourseById(Long courseId, HttpServletRequest request) throws ServiceException;

	String addLessonToCourse(Long lessonId, Long courseId, HttpServletRequest request) throws ServiceException;

	String deleteLessonFromCourse(Long lessonId, Long courseId, HttpServletRequest request) throws ServiceException;

	String updateCourse(Long id, Model model, HttpServletRequest request) throws ServiceException;

	String createCourse(Model model, HttpServletRequest request);

	String saveCourse(CourseDto courseDto, Model model, HttpServletRequest request);

	String deleteCourse(Long id, HttpServletRequest request) throws ServiceException;
}
