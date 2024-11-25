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
import ua.com.foxmineded.universitycms.controllers.StudentController;
import ua.com.foxmineded.universitycms.dto.StudentDto;
import ua.com.foxmineded.universitycms.dto.UserDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.StudentService;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentControllerImpl implements StudentController {
	private final StudentService studentService;
	private final UserDetailsService userDetailsService;

	@GetMapping("/{id}/findById")
	@Override
	public String findById(@RequestParam(required = false) Long courseId, @RequestParam(required = false) Long groupId,
			@RequestParam(required = false) String purpose, Model model, Principal principal, @PathVariable Long id)
			throws ServiceException {
		StudentDto studentDto = studentService.findById(id);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("students", Arrays.asList(studentDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("courseId", courseId);
		model.addAttribute("groupId", groupId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/students/%d/findById".formatted(id);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		if (Objects.nonNull(groupId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "groupId=%s".formatted(groupId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "students";
	}

	@GetMapping("/{passportNumber}/findByPassportNumber")
	@Override
	public String findByPassportNumber(@RequestParam(required = false) Long courseId,
			@RequestParam(required = false) Long groupId, @RequestParam(required = false) String purpose, Model model,
			Principal principal, @PathVariable String passportNumber) throws ServiceException {
		StudentDto studentDto = studentService.findByPassportNumber(passportNumber);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("students", Arrays.asList(studentDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("courseId", courseId);
		model.addAttribute("groupId", groupId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/students/%s/findByPassportNumber".formatted(passportNumber);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		if (Objects.nonNull(groupId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "groupId=%s".formatted(groupId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "students";
	}

	@GetMapping("/{login}/findByLogin")
	@Override
	public String findByLogin(@RequestParam(required = false) Long courseId,
			@RequestParam(required = false) Long groupId, @RequestParam(required = false) String purpose, Model model,
			Principal principal, @PathVariable String login) throws ServiceException {
		StudentDto studentDto = studentService.findByLogin(login);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("students", Arrays.asList(studentDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("courseId", courseId);
		model.addAttribute("groupId", groupId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/students/%s/findByLogin".formatted(login);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		if (Objects.nonNull(groupId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "groupId=%s".formatted(groupId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "students";
	}

	@GetMapping("/{telephoneNumber}/findByTelephoneNumber")
	@Override
	public String findByTelephoneNumber(@RequestParam(required = false) Long courseId,
			@RequestParam(required = false) Long groupId, @RequestParam(required = false) String purpose, Model model,
			Principal principal, @PathVariable String telephoneNumber) throws ServiceException {
		StudentDto studentDto = studentService.findByTelephoneNumber(telephoneNumber);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("students", Arrays.asList(studentDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("courseId", courseId);
		model.addAttribute("groupId", groupId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/students/%s/findByTelephoneNumber".formatted(telephoneNumber);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		if (Objects.nonNull(groupId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "groupId=%s".formatted(groupId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "students";
	}

	@GetMapping
	@Override
	public String findAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) Long courseId, @RequestParam(required = false) Long groupId,
			@RequestParam(required = false) String purpose, Principal principal, Model model) {
		Page<StudentDto> pageStudents = studentService.findAll(PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("students", pageStudents.getContent());
		model.addAttribute("currentPage", pageStudents.getNumber() + 1);
		model.addAttribute("totalItems", pageStudents.getTotalElements());
		model.addAttribute("totalPages", pageStudents.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("courseId", courseId);
		model.addAttribute("groupId", groupId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/students";
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		if (Objects.nonNull(groupId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "groupId=%s".formatted(groupId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "students";
	}

	@GetMapping("/{name}/findAllByName")
	@Override
	public String findAllByName(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) Long courseId, @RequestParam(required = false) Long groupId,
			@RequestParam(required = false) String purpose, Principal principal, Model model,
			@PathVariable String name) {
		Page<StudentDto> pageStudents = studentService.findAllByName(name,
				PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("students", pageStudents.getContent());
		model.addAttribute("currentPage", pageStudents.getNumber() + 1);
		model.addAttribute("totalItems", pageStudents.getTotalElements());
		model.addAttribute("totalPages", pageStudents.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("courseId", courseId);
		model.addAttribute("groupId", groupId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/students/%s/findAllByName".formatted(name);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		if (Objects.nonNull(groupId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "groupId=%s".formatted(groupId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "students";
	}

	@GetMapping("/{groupName}/findAllByGroupName")
	@Override
	public String findAllByGroupName(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) Long courseId,
			@RequestParam(required = false) Long groupId, @RequestParam(required = false) String purpose,
			Principal principal, Model model, @PathVariable String groupName) {
		Page<StudentDto> pageStudents = studentService.findAllByGroupName(groupName,
				PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("students", pageStudents.getContent());
		model.addAttribute("currentPage", pageStudents.getNumber() + 1);
		model.addAttribute("totalItems", pageStudents.getTotalElements());
		model.addAttribute("totalPages", pageStudents.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("courseId", courseId);
		model.addAttribute("groupId", groupId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/students/%s/findAllByGroupName".formatted(groupName);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		if (Objects.nonNull(groupId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "groupId=%s".formatted(groupId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "students";
	}

	@GetMapping("/{courseName}/findAllByCourseName")
	@Override
	public String findAllByCourseName(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) Long courseId,
			@RequestParam(required = false) Long groupId, @RequestParam(required = false) String purpose,
			Principal principal, Model model, @PathVariable String courseName) {
		Page<StudentDto> pageStudents = studentService.findAllByCourseName(courseName,
				PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("students", pageStudents.getContent());
		model.addAttribute("currentPage", pageStudents.getNumber() + 1);
		model.addAttribute("totalItems", pageStudents.getTotalElements());
		model.addAttribute("totalPages", pageStudents.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("courseId", courseId);
		model.addAttribute("groupId", groupId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/students/%s/findAllByCourseName".formatted(courseName);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		if (Objects.nonNull(groupId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "groupId=%s".formatted(groupId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "students";
	}

	@GetMapping("/{teacherName}/findAllByTeacherName")
	@Override
	public String findAllByTeacherName(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) Long courseId,
			@RequestParam(required = false) Long groupId, @RequestParam(required = false) String purpose,
			Principal principal, Model model, @PathVariable String teacherName) {
		Page<StudentDto> pageStudents = studentService.findAllByTeacherName(teacherName,
				PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("students", pageStudents.getContent());
		model.addAttribute("currentPage", pageStudents.getNumber() + 1);
		model.addAttribute("totalItems", pageStudents.getTotalElements());
		model.addAttribute("totalPages", pageStudents.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("courseId", courseId);
		model.addAttribute("groupId", groupId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/students/%s/findAllByTeacherName".formatted(teacherName);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		if (Objects.nonNull(groupId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "groupId=%s".formatted(groupId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "students";
	}

	@GetMapping(value = "/{specialization}/findAllBySpecialization")
	@Override
	public String findAllBySpecialization(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) Long courseId,
			@RequestParam(required = false) Long groupId, @RequestParam(required = false) String purpose,
			Principal principal, Model model, @PathVariable String specialization) throws ServiceException {
		Page<StudentDto> pageStudents = studentService.findAllBySpecialization(specialization,
				PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("students", pageStudents.getContent());
		model.addAttribute("currentPage", pageStudents.getNumber() + 1);
		model.addAttribute("totalItems", pageStudents.getTotalElements());
		model.addAttribute("totalPages", pageStudents.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("courseId", courseId);
		model.addAttribute("groupId", groupId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/students/%s/findAllBySpecialization".formatted(specialization);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(courseId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "courseId=%s".formatted(courseId);
		}
		if (Objects.nonNull(groupId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "groupId=%s".formatted(groupId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "students";
	}

	@GetMapping("/new")
	@Override
	public String createStudent(Model model, HttpServletRequest request) {
		StudentDto studentDto = new StudentDto();
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		model.addAttribute("student", studentDto);
		model.addAttribute("referer", referer);
		return "student-create";
	}

	@PostMapping("/save")
	@Override
	public String saveStudent(@ModelAttribute("student") StudentDto studentDto, Model model, HttpServletRequest request)
			throws ServiceDataIntegrityException {
		String referer = (request.getParameter("referer").contains("/new")
				|| request.getParameter("referer").contains("/update")) ? "/students" : request.getParameter("referer");
		model.addAttribute("referer", referer);
		String viewName = request.getParameter("viewName");

		try {
			studentService.save(studentDto);
		} catch (ConstraintViolationException e) {
			model.addAttribute("validationExceptions", e.getConstraintViolations());
			model.addAttribute("student", studentDto);
			if ("student-create".equals(viewName)) {
				return "student-create";
			}
			if ("student-update".equals(viewName)) {
				return "student-update";
			}
		} catch (ServiceDataIntegrityException e) {
			model.addAttribute("integrityExceptions", e.getExceptions());
			model.addAttribute("student", studentDto);
			if ("student-create".equals(viewName)) {
				return "student-create";
			}
			if ("student-update".equals(viewName)) {
				return "student-update";
			}
		}
		return "redirect:" + referer;
	}

	@GetMapping("/{id}/update")
	@Override
	public String updateStudent(@PathVariable Long id, Model model, HttpServletRequest request)
			throws ServiceException {
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		StudentDto studentDto = studentService.findById(id);
		model.addAttribute("student", studentDto);
		model.addAttribute("referer", referer);
		return "student-update";
	}

	@PostMapping("/{id}/delete")
	@Override
	public String deleteStudent(@PathVariable Long id, HttpServletRequest request) throws ServiceException {
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		studentService.deleteById(id);
		return "redirect:" + referer;
	}

}
