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
import lombok.RequiredArgsConstructor;
import ua.com.foxmineded.universitycms.controllers.GroupController;
import ua.com.foxmineded.universitycms.dto.GroupDto;
import ua.com.foxmineded.universitycms.dto.UserDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.GroupService;

@Controller
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupControllerImpl implements GroupController {
	private final GroupService groupService;
	private final UserDetailsService userDetailsService;

	@GetMapping("/{id}/findById")
	@Override
	public String findById(Principal principal, Model model, @PathVariable Long id) throws ServiceException {
		GroupDto groupDto = groupService.findById(id);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("groups", Arrays.asList(groupDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("currentPagePath", "/groups/%d/findById".formatted(id));
		return "groups";
	}

	@GetMapping("/{groupName}/findByGroupName")
	@Override
	public String findByGroupName(Principal principal, Model model, @PathVariable String groupName)
			throws ServiceException {
		GroupDto groupDto = groupService.findByGroupName(groupName);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("groups", Arrays.asList(groupDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("currentPagePath", "/groups/%s/findByGroupName".formatted(groupName));
		return "groups";
	}

	@GetMapping("/{studentId}/findByStudentId")
	@Override
	public String findByStudentId(Principal principal, @PathVariable Long studentId, Model model)
			throws ServiceException {
		GroupDto groupDto = groupService.findByStudentId(studentId);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("groups", Arrays.asList(groupDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("currentPagePath", "/groups/%s/findByStudentId".formatted(studentId));
		return "groups";
	}

	@GetMapping
	@Override
	public String findAll(Principal principal, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, Model model) {
		Page<GroupDto> pageGroup = groupService.findAll(PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("groups", pageGroup.getContent());
		model.addAttribute("currentPage", pageGroup.getNumber() + 1);
		model.addAttribute("totalItems", pageGroup.getTotalElements());
		model.addAttribute("totalPages", pageGroup.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("currentPagePath", "/groups");
		return "groups";
	}

	@GetMapping("/{specialization}/findAllBySpecialization")
	@Override
	public String findAllBySpecialization(Principal principal, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, Model model, @PathVariable String specialization)
			throws ServiceException {
		Page<GroupDto> pageGroup = groupService.findAllBySpecialization(specialization,
				PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("groups", pageGroup.getContent());
		model.addAttribute("currentPage", pageGroup.getNumber() + 1);
		model.addAttribute("totalItems", pageGroup.getTotalElements());
		model.addAttribute("totalPages", pageGroup.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("currentPagePath", "/groups/%s/findAllBySpecialization".formatted(specialization));
		return "groups";
	}

	@GetMapping("/{studentAmount}/findAllWithLessOrEqualStudents")
	@Override
	public String findAllWithLessOrEqualStudents(Principal principal, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, Model model, @PathVariable int studentAmount) {
		Page<GroupDto> pageGroup = groupService.findAllWithLessOrEqualStudents(studentAmount,
				PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("groups", pageGroup.getContent());
		model.addAttribute("currentPage", pageGroup.getNumber() + 1);
		model.addAttribute("totalItems", pageGroup.getTotalElements());
		model.addAttribute("totalPages", pageGroup.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("currentPagePath", "/groups/%d/findAllWithLessOrEqualStudents".formatted(studentAmount));
		return "groups";
	}

	@PostMapping("/addStudentToGroupById/{groupId}/groupId/{studentId}/studentId")
	@Override
	public String addStudentToGroupById(@PathVariable Long groupId, @PathVariable Long studentId,
			HttpServletRequest request) throws ServiceException {
		groupService.addStudentToGroupById(groupId, studentId);
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		return "redirect:" + referer;
	}

	@PostMapping("/deleteStudentFromGroupById/{groupId}/groupId/{studentId}/studentId")
	@Override
	public String deleteStudentFromGroupById(@PathVariable Long groupId, @PathVariable Long studentId,
			HttpServletRequest request) throws ServiceException {
		groupService.deleteStudentFromGroupById(groupId, studentId);
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		return "redirect:" + referer;
	}

	@GetMapping("/new")
	@Override
	public String createGroup(Model model, HttpServletRequest request) {
		GroupDto groupDto = new GroupDto();
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		model.addAttribute("group", groupDto);
		model.addAttribute("referer", referer);
		return "group-create";
	}

	@PostMapping("/save")
	@Override
	public String saveGroup(@ModelAttribute("group") GroupDto groupDto, Model model, HttpServletRequest request)
			throws ServiceDataIntegrityException {
		String referer = (request.getParameter("referer").contains("/new")
				|| request.getParameter("referer").contains("/update")) ? "/groups" : request.getParameter("referer");
		model.addAttribute("referer", referer);
		String viewName = request.getParameter("viewName");

		try {
			groupService.save(groupDto);
		} catch (ConstraintViolationException e) {
			model.addAttribute("validationExceptions", e.getConstraintViolations());
			model.addAttribute("group", groupDto);
			if ("group-create".equals(viewName)) {
				return "group-create";
			}
			if ("group-update".equals(viewName)) {
				return "group-update";
			}
		} catch (ServiceDataIntegrityException e) {
			model.addAttribute("integrityExceptions", e.getExceptions());
			model.addAttribute("group", groupDto);
			if ("group-create".equals(viewName)) {
				return "group-create";
			}
			if ("group-update".equals(viewName)) {
				return "group-update";
			}
		}
		return "redirect:" + referer;
	}

	@GetMapping("/{id}/update")
	@Override
	public String updateGroup(@PathVariable Long id, Model model, HttpServletRequest request) throws ServiceException {
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		GroupDto groupDto = groupService.findById(id);
		model.addAttribute("group", groupDto);
		model.addAttribute("referer", referer);
		return "group-update";
	}

	@PostMapping("/{id}/delete")
	@Override
	public String deleteGroup(@PathVariable Long id, HttpServletRequest request) throws ServiceException {
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		groupService.deleteById(id);
		return "redirect:" + referer;
	}
}
