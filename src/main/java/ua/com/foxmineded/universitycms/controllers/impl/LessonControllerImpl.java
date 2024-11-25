package ua.com.foxmineded.universitycms.controllers.impl;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

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
import lombok.RequiredArgsConstructor;
import ua.com.foxmineded.universitycms.controllers.LessonController;
import ua.com.foxmineded.universitycms.dto.LessonDto;
import ua.com.foxmineded.universitycms.dto.UserDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.LessonService;

@Controller
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonControllerImpl implements LessonController {
	private final LessonService lessonService;
	private final UserDetailsService userDetailsService;

	@GetMapping
	@Override
	public String findAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String purpose, @RequestParam(required = false) Long courseId,
			Principal principal, Model model) {
		Page<LessonDto> pageLessons = lessonService
				.findAll(PageRequest.of(page - 1, size).withSort(Sort.by("id").descending()));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("lessons", pageLessons.getContent());
		model.addAttribute("currentPage", pageLessons.getNumber() + 1);
		model.addAttribute("totalItems", pageLessons.getTotalElements());
		model.addAttribute("totalPages", pageLessons.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("courseId", courseId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/lessons";
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%d".formatted(courseId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "lessons";
	}

	@GetMapping("/{lessonDate}/findAllByLessonDate")
	@Override
	public String findAllByLessonDate(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String purpose,
			@RequestParam(required = false) Long courseId, Principal principal, Model model,
			@PathVariable LocalDate lessonDate) {
		Page<LessonDto> pageLessons = lessonService.findAllByLessonDate(lessonDate,
				PageRequest.of(page - 1, size).withSort(Sort.by("id").descending()));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("lessons", pageLessons.getContent());
		model.addAttribute("currentPage", pageLessons.getNumber() + 1);
		model.addAttribute("totalItems", pageLessons.getTotalElements());
		model.addAttribute("totalPages", pageLessons.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("courseId", courseId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/lessons/%s/findAllByLessonDate".formatted(lessonDate.toString());
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%d".formatted(courseId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "lessons";
	}

	@GetMapping("/{courseId}/findAllByCourseId")
	@Override
	public String findAllByCourseId(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String purpose,
			@RequestParam(required = false, name = "courseId") Long destinationCourseId, Principal principal,
			Model model, @PathVariable Long courseId) {
		Page<LessonDto> pageLessons = lessonService.findAllByCourseId(courseId,
				PageRequest.of(page - 1, size).withSort(Sort.by("id").descending()));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("lessons", pageLessons.getContent());
		model.addAttribute("currentPage", pageLessons.getNumber() + 1);
		model.addAttribute("totalItems", pageLessons.getTotalElements());
		model.addAttribute("totalPages", pageLessons.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("courseId", destinationCourseId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/lessons/%s/findAllByCourseId".formatted(courseId);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(destinationCourseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?")
					+ "courseId=%d".formatted(destinationCourseId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "lessons";
	}

	@GetMapping("/{courseName}/findAllByCourseName")
	@Override
	public String findAllByCourseName(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String purpose,
			@RequestParam(required = false) Long courseId, Principal principal, Model model,
			@PathVariable String courseName) {
		Page<LessonDto> pageLessons = lessonService.findAllByCourseName(courseName,
				PageRequest.of(page - 1, size).withSort(Sort.by("id").descending()));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("lessons", pageLessons.getContent());
		model.addAttribute("currentPage", pageLessons.getNumber() + 1);
		model.addAttribute("totalItems", pageLessons.getTotalElements());
		model.addAttribute("totalPages", pageLessons.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("courseId", courseId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/lessons/%s/findAllByCourseName".formatted(courseName);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%d".formatted(courseId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "lessons";
	}

	@GetMapping("/{roomId}/findAllByRoomId")
	@Override
	public String findAllByRoomId(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String purpose,
			@RequestParam(required = false) Long courseId, Principal principal, Model model,
			@PathVariable Long roomId) {
		Page<LessonDto> pageLessons = lessonService.findAllByRoomId(roomId,
				PageRequest.of(page - 1, size).withSort(Sort.by("id").descending()));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("lessons", pageLessons.getContent());
		model.addAttribute("currentPage", pageLessons.getNumber() + 1);
		model.addAttribute("totalItems", pageLessons.getTotalElements());
		model.addAttribute("totalPages", pageLessons.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("courseId", courseId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/lessons/%d/findAllByRoomId".formatted(roomId);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%d".formatted(courseId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "lessons";
	}

	@GetMapping("/{id}/findById")
	@Override
	public String findById(@RequestParam(required = false) String purpose,
			@RequestParam(required = false) Long courseId, Principal principal, Model model, @PathVariable Long id)
			throws ServiceException {
		LessonDto lessonDto = lessonService.findById(id);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("lessons", Arrays.asList(lessonDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("courseId", courseId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/lessons/%d/findById".formatted(id);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%d".formatted(courseId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "lessons";
	}

	@PostMapping("/addRoomById/{lessonId}/lessonId/{roomId}/roomId")
	@Override
	public String addRoomById(@PathVariable Long lessonId, @PathVariable Long roomId) throws ServiceException {
		lessonService.addRoomById(lessonId, roomId);
		return "redirect:/lessons";

	}

	@GetMapping("/new")
	@Override
	public String createLesson(Model model, HttpServletRequest request) {
		LessonDto lessonDto = new LessonDto();
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		model.addAttribute("lesson", lessonDto);
		model.addAttribute("referer", referer);
		return "lesson-create";
	}

	@GetMapping("/{id}/update")
	@Override
	public String updateLesson(@PathVariable Long id, Model model, HttpServletRequest request) throws ServiceException {
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		LessonDto lessonDto = lessonService.findById(id);
		model.addAttribute("lesson", lessonDto);
		model.addAttribute("referer", referer);
		return "lesson-update";
	}

	@PostMapping("/save")
	@Override
	public String saveLesson(@ModelAttribute("lesson") LessonDto lessonDto, Model model, HttpServletRequest request)
			throws ServiceException {
		String referer = (request.getParameter("referer").contains("/new")
				|| request.getParameter("referer").contains("/update")) ? "/lessons" : request.getParameter("referer");
		model.addAttribute("referer", referer);
		String viewName = request.getParameter("viewName");
		try {
			lessonService.save(lessonDto);
		} catch (ConstraintViolationException e) {
			model.addAttribute("validationExceptions", e.getConstraintViolations());
			model.addAttribute("lesson", lessonDto);
			if ("lesson-create".equals(viewName)) {
				return "lesson-create";
			}
			if ("lesson-update".equals(viewName)) {
				return "lesson-update";
			}
		}
		return "redirect:" + referer;
	}

	@PostMapping("/{id}/delete")
	@Override
	public String deleteLesson(@PathVariable Long id, HttpServletRequest request) throws ServiceException {
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		lessonService.deleteById(id);
		return "redirect:" + referer;
	}
}
