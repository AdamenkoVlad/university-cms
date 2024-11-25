package ua.com.foxmineded.universitycms.controllers.impl;

import java.security.Principal;
import java.util.Arrays;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import ua.com.foxmineded.universitycms.controllers.CourseController;
import ua.com.foxmineded.universitycms.dto.CourseDto;
import ua.com.foxmineded.universitycms.dto.UserDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.CourseService;

@Controller
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseControllerImpl implements CourseController {
	private final CourseService courseService;
	private final UserDetailsService userDetailsService;

	@GetMapping("/{id}/findById")
	@Override
	public String findById(Principal principal, Model model, @PathVariable Long id) throws ServiceException {
		CourseDto courseDto = courseService.findById(id);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("courses", Arrays.asList(courseDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("currentPagePath", "/courses/%d/findById".formatted(id));
		return "courses";
	}

	@GetMapping("/{courseName}/findByCourseName")
	@Override
	public String findByCourseName(Principal principal, Model model, @PathVariable String courseName)
			throws ServiceException {
		CourseDto courseDto = courseService.findByCourseName(courseName);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("courses", Arrays.asList(courseDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("currentPagePath", "/courses/%s/findByCourseName".formatted(courseName));
		return "courses";
	}

	@GetMapping("/{specialization}/findAllBySpecialization")
	@Override
	public String findAllBySpecialization(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, Principal principal, Model model,
			@PathVariable String specialization) throws ServiceException {
		Page<CourseDto> pageCourses = courseService.findAllBySpecialization(specialization,
				PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("courses", pageCourses.getContent());
		model.addAttribute("currentPage", pageCourses.getNumber() + 1);
		model.addAttribute("totalItems", pageCourses.getTotalElements());
		model.addAttribute("totalPages", pageCourses.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("currentPagePath", "/courses/%s/findAllBySpecialization".formatted(specialization));
		return "courses";
	}

	@GetMapping
	@Override
	public String findAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
			Principal principal, Model model) {
		Page<CourseDto> pageCourses = courseService.findAll(PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("courses", pageCourses.getContent());
		model.addAttribute("currentPage", pageCourses.getNumber() + 1);
		model.addAttribute("totalItems", pageCourses.getTotalElements());
		model.addAttribute("totalPages", pageCourses.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("currentPagePath", "/courses");
		return "courses";
	}

	@GetMapping("/{teacherId}/findAllByTeacherId")
	@Override
	public String findAllByTeacherId(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, Principal principal, Model model,
			@PathVariable Long teacherId) {
		Page<CourseDto> pageCourses = courseService.findAllByTeacherId(teacherId,
				PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("courses", pageCourses.getContent());
		model.addAttribute("currentPage", pageCourses.getNumber() + 1);
		model.addAttribute("totalItems", pageCourses.getTotalElements());
		model.addAttribute("totalPages", pageCourses.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("currentPagePath", "/courses/%d/findAllByTeacherId".formatted(teacherId));
		return "courses";
	}

	@GetMapping("/{teacherName}/findAllByTeacherName")
	@Override
	public String findAllByTeacherName(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, Principal principal, Model model,
			@PathVariable String teacherName) {
		Page<CourseDto> pageCourses = courseService.findAllByTeacherName(teacherName,
				PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("courses", pageCourses.getContent());
		model.addAttribute("currentPage", pageCourses.getNumber() + 1);
		model.addAttribute("totalItems", pageCourses.getTotalElements());
		model.addAttribute("totalPages", pageCourses.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("currentPagePath", "/courses/%s/findAllByTeacherName".formatted(teacherName));
		return "courses";
	}

	@GetMapping("/{roomId}/findAllByRoomId")
	@Override
	public String findAllByRoomId(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, Principal principal, Model model, @PathVariable Long roomId) {
		Page<CourseDto> pageCourses = courseService.findAllByRoomId(roomId,
				PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("courses", pageCourses.getContent());
		model.addAttribute("currentPage", pageCourses.getNumber() + 1);
		model.addAttribute("totalItems", pageCourses.getTotalElements());
		model.addAttribute("totalPages", pageCourses.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("currentPagePath", "/courses/%d/findAllByRoomId".formatted(roomId));
		return "courses";
	}

	@GetMapping("/{roomNumber}/findAllByRoomNumber")
	@Override
	public String findAllByRoomNumber(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, Principal principal, Model model,
			@PathVariable int roomNumber) {
		Page<CourseDto> pageCourses = courseService.findAllByRoomNumber(roomNumber,
				PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("courses", pageCourses.getContent());
		model.addAttribute("currentPage", pageCourses.getNumber() + 1);
		model.addAttribute("totalItems", pageCourses.getTotalElements());
		model.addAttribute("totalPages", pageCourses.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("currentPagePath", "/courses/%d/findAllByRoomNumber".formatted(roomNumber));
		return "courses";
	}
	
	@GetMapping("/{studentId}/findAllByStudentId")
	@Override
	public String findAllByStudentId(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, Principal principal, Model model, @PathVariable  Long studentId) {
		Page<CourseDto> pageCourses = courseService.findAllByStudentId(studentId,
				PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("courses", pageCourses.getContent());
		model.addAttribute("currentPage", pageCourses.getNumber() + 1);
		model.addAttribute("totalItems", pageCourses.getTotalElements());
		model.addAttribute("totalPages", pageCourses.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("currentPagePath", "/courses/%d/findAllByStudentId".formatted(studentId));
		return "courses";
	}

	@PostMapping("/addStudentToCourseById/{studentId}/studentId/{courseId}/courseId")
	@Override
	public String addStudentToCourseById(@PathVariable Long studentId, @PathVariable Long courseId,
			HttpServletRequest request) throws ServiceException {
		courseService.addStudentToCourseById(studentId, courseId);
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		return "redirect:" + referer;
	}

	@PostMapping("/addTeacherToCourseById/{teacherId}/teacherId/{courseId}/courseId")
	@Override
	public String addTeacherToCourseById(@PathVariable Long teacherId, @PathVariable Long courseId)
			throws ServiceException {
		courseService.addTeacherToCourseById(teacherId, courseId);
		return "redirect:/teachers";

	}

	@PostMapping("/deleteStudentFromCourseById/{studentId}/studentId/{courseId}/courseId")
	@Override
	public String deleteStudentFromCourseById(@PathVariable Long studentId, @PathVariable Long courseId,
			HttpServletRequest request) throws ServiceException {
		courseService.deleteStudentFromCourseById(studentId, courseId);
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		return "redirect:" + referer;
	}

	@PostMapping("/deleteTeacherFromCourseById/{courseId}/courseId")
	@Override
	public String deleteTeacherFromCourseById(@PathVariable Long courseId, HttpServletRequest request)
			throws ServiceException {
		courseService.deleteTeacherFromCourseById(courseId);
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		return "redirect:" + referer;

	}

	@PostMapping("/addLessonToCourse/{lessonId}/lessonId/{courseId}/courseId")
	@Override
	public String addLessonToCourse(@PathVariable Long lessonId, @PathVariable Long courseId,
			HttpServletRequest request) throws ServiceException {
		courseService.addLessonToCourse(lessonId, courseId);
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		return "redirect:" + referer;
	}

	@PostMapping("deleteLessonFromCourse/{lessonId}/lessonId/{courseId}/courseId")
	@Override
	public String deleteLessonFromCourse(@PathVariable Long lessonId, @PathVariable Long courseId,
			HttpServletRequest request) throws ServiceException {
		courseService.deleteLessonFromCourse(lessonId, courseId);
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		return "redirect:" + referer;
	}

	@GetMapping("/{id}/update")
	@Override
	public String updateCourse(@PathVariable Long id, Model model, HttpServletRequest request) throws ServiceException {
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		CourseDto courseDto = courseService.findById(id);
		model.addAttribute("course", courseDto);
		model.addAttribute("referer", referer);
		return "course-update";
	}

	@GetMapping("/new")
	@Override
	public String createCourse(Model model, HttpServletRequest request) {
		CourseDto courseDto = new CourseDto();
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		model.addAttribute("course", courseDto);
		model.addAttribute("referer", referer);
		return "course-create";
	}

	@PostMapping("/save")
	@SneakyThrows
	@Override
	public String saveCourse(@ModelAttribute("course") CourseDto courseDto, Model model, HttpServletRequest request) {
		String referer = (request.getParameter("referer").contains("/new")
				|| request.getParameter("referer").contains("/update")) ? "/courses" : request.getParameter("referer");
		model.addAttribute("referer", referer);
		String viewName = request.getParameter("viewName");
		try {
			courseService.save(courseDto);
		} catch (ConstraintViolationException e) {
			model.addAttribute("validationExceptions", e.getConstraintViolations());
			model.addAttribute("course", courseDto);
			if ("course-create".equals(viewName)) {
				return "course-create";
			}
			if ("course-update".equals(viewName)) {
				return "course-update";
			}
		} catch (ServiceDataIntegrityException e) {
			model.addAttribute("integrityExceptions", e.getExceptions());
			model.addAttribute("course", courseDto);
			if ("course-create".equals(viewName)) {
				return "course-create";
			}
			if ("course-update".equals(viewName)) {
				return "course-update";
			}
		}
		return "redirect:" + referer;
	}

	@PostMapping("/{id}/delete")
	@Override
	public String deleteCourse(@PathVariable Long id, HttpServletRequest request) throws ServiceException {
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		courseService.deleteById(id);
		return "redirect:" + referer;
	}
}
