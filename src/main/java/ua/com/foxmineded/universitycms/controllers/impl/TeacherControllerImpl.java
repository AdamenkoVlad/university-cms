package ua.com.foxmineded.universitycms.controllers.impl;

import java.security.Principal;
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
import ua.com.foxmineded.universitycms.controllers.TeacherController;
import ua.com.foxmineded.universitycms.dto.TeacherDto;
import ua.com.foxmineded.universitycms.dto.UserDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.TeacherService;

@Controller
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherControllerImpl implements TeacherController {
	private final TeacherService teacherService;
	private final UserDetailsService userDetailsService;

	@GetMapping
	@Override
	public String findAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String purpose, @RequestParam(required = false) Long courseId,
			Principal principal, Model model) {
		Page<TeacherDto> pageTeachers = teacherService.findAll(PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("teachers", pageTeachers.getContent());
		model.addAttribute("currentPage", pageTeachers.getNumber() + 1);
		model.addAttribute("totalItems", pageTeachers.getTotalElements());
		model.addAttribute("totalPages", pageTeachers.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("courseId", courseId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/teachers";
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "teachers";
	}

	@GetMapping("/{name}/findAllByName")
	@Override
	public String findAllByName(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String purpose, @RequestParam(required = false) Long courseId,
			Principal principal, @PathVariable String name, Model model) {
		Page<TeacherDto> pageTeachers = teacherService.findAllByName(name,
				PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("teachers", pageTeachers.getContent());
		model.addAttribute("currentPage", pageTeachers.getNumber() + 1);
		model.addAttribute("totalItems", pageTeachers.getTotalElements());
		model.addAttribute("totalPages", pageTeachers.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("courseId", courseId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/teachers/%s/findAllByName".formatted(name);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "teachers";
	}

	@GetMapping("/{id}/findById")
	@Override
	public String findById(@RequestParam(required = false) String purpose,
			@RequestParam(required = false) Long courseId, @PathVariable Long id, Principal principal, Model model)
			throws ServiceException {
		TeacherDto teacherDto = teacherService.findById(id);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("teachers", Arrays.asList(teacherDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("courseId", courseId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/teachers/%d/findById".formatted(id);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "teachers";
	}

	@GetMapping("/{login}/findByLogin")
	@Override
	public String findByLogin(@RequestParam(required = false) String purpose,
			@RequestParam(required = false) Long courseId, @PathVariable String login, Principal principal, Model model)
			throws ServiceException {
		TeacherDto teacherDto = teacherService.findByLogin(login);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("teachers", Arrays.asList(teacherDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("courseId", courseId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/teachers/%s/findByLogin".formatted(login);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "teachers";
	}

	@GetMapping("/{passportNumber}/findByPassportNumber")
	@Override
	public String findByPassportNumber(@RequestParam(required = false) String purpose,
			@RequestParam(required = false) Long courseId, @PathVariable String passportNumber, Principal principal,
			Model model) throws ServiceException {
		TeacherDto teacherDto = teacherService.findByPassportNumber(passportNumber);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("teachers", Arrays.asList(teacherDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("courseId", courseId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/teachers/%s/findByPassportNumber".formatted(passportNumber);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "teachers";
	}

	@GetMapping("/{telephoneNumber}/findByTelephoneNumber")
	@Override
	public String findByTelephoneNumber(@RequestParam(required = false) String purpose,
			@RequestParam(required = false) Long courseId, @PathVariable String telephoneNumber, Principal principal,
			Model model) throws ServiceException {
		TeacherDto teacherDto = teacherService.findByTelephoneNumber(telephoneNumber);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("teachers", Arrays.asList(teacherDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("courseId", courseId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/teachers/%s/findByTelephoneNumber".formatted(telephoneNumber);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "teachers";
	}

	@GetMapping("/{courseName}/findByCourseName")
	@Override
	public String findByCourseName(@RequestParam(required = false) String purpose,
			@RequestParam(required = false) Long courseId, @PathVariable String courseName, Principal principal,
			Model model) throws ServiceException {
		TeacherDto teacherDto = teacherService.findByCourseName(courseName);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("teachers", Arrays.asList(teacherDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("courseId", courseId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/teachers/%s/findByCourseName".formatted(courseName);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "teachers";
	}

	@GetMapping("/{courseId}/findByCourseId")
	@Override
	public String findByCourseId(@RequestParam(required = false) String purpose,
			@RequestParam(required = false, name = "courseId") Long destinationCourseId, @PathVariable Long courseId,
			Principal principal, Model model) throws ServiceException {
		TeacherDto teacherDto = teacherService.findByCourseId(courseId);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("teachers", Arrays.asList(teacherDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("courseId", destinationCourseId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/teachers/%s/findByCourseId".formatted(courseId);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?")
					+ "courseId=%s".formatted(destinationCourseId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "teachers";
	}

	@GetMapping("/{lessonId}/findByLessonId")
	@Override
	public String findByLessonId(@RequestParam(required = false) String purpose,
			@RequestParam(required = false) Long courseId, @PathVariable Long lessonId, Principal principal,
			Model model) throws ServiceException {
		TeacherDto teacherDto = teacherService.findByLessonId(lessonId);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("teachers", Arrays.asList(teacherDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("courseId", courseId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/teachers/%s/findByLessonId".formatted(lessonId);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "teachers";
	}

	@GetMapping("/new")
	@Override
	public String createTeacher(Model model, HttpServletRequest request) {
		TeacherDto teacherDto = new TeacherDto();
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		model.addAttribute("teacher", teacherDto);
		model.addAttribute("referer", referer);
		return "teacher-create";
	}

	@PostMapping("/save")
	@Override
	public String saveTeacher(@ModelAttribute("teacher") TeacherDto teacherDto, Model model,
			HttpServletRequest request) {
		String referer = (request.getParameter("referer").contains("/new")
				|| request.getParameter("referer").contains("/update")) ? "/teachers" : request.getParameter("referer");
		model.addAttribute("referer", referer);
		String viewName = request.getParameter("viewName");

		try {
			teacherService.save(teacherDto);
		} catch (ConstraintViolationException e) {
			model.addAttribute("validationExceptions", e.getConstraintViolations());
			model.addAttribute("teacher", teacherDto);
			if ("teacher-create".equals(viewName)) {
				return "teacher-create";
			}
			if ("teacher-update".equals(viewName)) {
				return "teacher-update";
			}
		} catch (ServiceDataIntegrityException e) {
			model.addAttribute("integrityExceptions", e.getExceptions());
			model.addAttribute("teacher", teacherDto);
			if ("teacher-create".equals(viewName)) {
				return "teacher-create";
			}
			if ("teacher-update".equals(viewName)) {
				return "teacher-update";
			}
		}
		return "redirect:" + referer;
	}

	@GetMapping("/{id}/update")
	@Override
	public String updateTeacher(@PathVariable Long id, Model model, HttpServletRequest request)
			throws ServiceException {
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		TeacherDto teacherDto = teacherService.findById(id);
		model.addAttribute("teacher", teacherDto);
		model.addAttribute("referer", referer);
		return "teacher-update";
	}

	@PostMapping("/{id}/delete")
	@Override
	public String deleteTeacher(@PathVariable Long id, HttpServletRequest request) throws ServiceException {
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		teacherService.deleteById(id);
		return "redirect:" + referer;
	}
}
