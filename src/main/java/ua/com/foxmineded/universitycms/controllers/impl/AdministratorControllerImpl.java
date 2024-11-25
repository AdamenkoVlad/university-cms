package ua.com.foxmineded.universitycms.controllers.impl;

import java.security.Principal;
import java.util.Arrays;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ua.com.foxmineded.universitycms.controllers.AdministratorController;
import ua.com.foxmineded.universitycms.dto.AdministratorDto;
import ua.com.foxmineded.universitycms.dto.UserDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.AdministratorService;

@Controller
@RequestMapping("/administrators")
@RequiredArgsConstructor
public class AdministratorControllerImpl implements AdministratorController {
	private final AdministratorService administratorService;
	private final UserDetailsService userDetailsService;

	@GetMapping("/{id}/findById")
	@Override
	public String findById(Principal principal, Model model, @PathVariable Long id) throws ServiceException {
		AdministratorDto administratorDto = administratorService.findById(id);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("administrators", Arrays.asList(administratorDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("currentPa3gePath", "/administrators/%d/findById".formatted(id));
		return "administrators";
	}

	@GetMapping("/{login}/findByLogin")
	@Override
	public String findByLogin(Principal principal, Model model, @PathVariable String login) throws ServiceException {
		AdministratorDto administratorDto = administratorService.findByLogin(login);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("administrators", Arrays.asList(administratorDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("currentPagePath", "/administrators/%s/findByLogin".formatted(login));
		return "administrators";
	}

	@GetMapping("/{passportNumber}/findByPassportNumber")
	@Override
	public String findByPassportNumber(Principal principal, Model model, @PathVariable String passportNumber)
			throws ServiceException {
		AdministratorDto administratorDto = administratorService.findByPassportNumber(passportNumber);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("administrators", Arrays.asList(administratorDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("currentPagePath", "/administrators/%s/findByPassportNumber".formatted(passportNumber));
		return "administrators";
	}

	@GetMapping("/{telephoneNumber}/findByTelephoneNumber")
	@Override
	public String findByTelephoneNumber(Principal principal, Model model, @PathVariable String telephoneNumber)
			throws ServiceException {
		AdministratorDto administratorDto = administratorService.findByTelephoneNumber(telephoneNumber);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("administrators", Arrays.asList(administratorDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("currentPagePath", "/administrators/%s/findByTelephoneNumber".formatted(telephoneNumber));
		return "administrators";
	}

	@GetMapping("/{name}/findByName")
	@Override
	public String findByName(Principal principal, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, Model model, @PathVariable String name) {
		Page<AdministratorDto> pageAdministrators = administratorService.findAllByName(name,
				PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("administrators", pageAdministrators.getContent());
		model.addAttribute("currentPage", pageAdministrators.getNumber() + 1);
		model.addAttribute("totalItems", pageAdministrators.getTotalElements());
		model.addAttribute("totalPages", pageAdministrators.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("currentPagePath", "/administrators/%s/findByName".formatted(name));
		return "administrators";
	}

	@GetMapping
	@Override
	public String findAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
			Principal principal, Model model) throws ServiceException {
		Page<AdministratorDto> pageAdministrators = administratorService
				.findAll(PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("administrators", pageAdministrators.getContent());
		model.addAttribute("currentPage", pageAdministrators.getNumber() + 1);
		model.addAttribute("totalItems", pageAdministrators.getTotalElements());
		model.addAttribute("totalPages", pageAdministrators.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("currentPagePath", "/administrators");
		return "administrators";
	}

	@GetMapping("/new")
	@Override
	public String createAdministrator(Model model, HttpServletRequest request) {
		AdministratorDto administratorDto = new AdministratorDto();
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		model.addAttribute("administrator", administratorDto);
		model.addAttribute("referer", referer);
		return "administrator-create";
	}

	@PostMapping("/save")
	@SneakyThrows
	@Override
	public String saveAdministrator(@ModelAttribute("administrator") AdministratorDto administratorDto, Model model,
			HttpServletRequest request) {
		String referer = (request.getParameter("referer").contains("/new")
				|| request.getParameter("referer").contains("/update")) ? "/administrators"
						: request.getParameter("referer");
		model.addAttribute("referer", referer);
		String viewName = request.getParameter("viewName");
		try {
			administratorService.save(administratorDto);
		} catch (ConstraintViolationException e) {
			model.addAttribute("validationExceptions", e.getConstraintViolations());
			model.addAttribute("administrator", administratorDto);
			if ("administrator-create".equals(viewName)) {
				return "administrator-create";
			}
			if ("administrator-update".equals(viewName)) {
				return "administrator-update";
			}
		} catch (ServiceDataIntegrityException e) {
			model.addAttribute("integrityExceptions", e.getExceptions());
			model.addAttribute("administrator", administratorDto);
			if ("administrator-create".equals(viewName)) {
				return "administrator-create";
			}
			if ("administrator-update".equals(viewName)) {
				return "administrator-update";
			}
		}
		return "redirect:" + referer;
	}

	@GetMapping("/{id}/update")
	@Override
	public String updateAdministrator(@PathVariable Long id, Model model, HttpServletRequest request)
			throws ServiceException {
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		AdministratorDto administratorDto = administratorService.findById(id);
		model.addAttribute("administrator", administratorDto);
		model.addAttribute("referer", referer);
		return "administrator-update";
	}

	@PostMapping("/{id}/delete")
	@Override
	public String deleteAdministrator(@PathVariable Long id, HttpServletRequest request) throws ServiceException {
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		administratorService.deleteById(id);
		return "redirect:" + referer;
	}
}
