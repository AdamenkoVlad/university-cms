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
import ua.com.foxmineded.universitycms.controllers.RoomController;
import ua.com.foxmineded.universitycms.dto.RoomDto;
import ua.com.foxmineded.universitycms.dto.UserDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.RoomService;

@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomControllerImpl implements RoomController {
	private final RoomService roomService;
	private final UserDetailsService userDetailsService;

	@GetMapping
	@Override
	public String findAll(@RequestParam(required = false) Long lessonId, @RequestParam(required = false) String purpose,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
			Principal principal, Model model) {
		Page<RoomDto> pageRooms = roomService.findAll(PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("rooms", pageRooms.getContent());
		model.addAttribute("currentPage", pageRooms.getNumber() + 1);
		model.addAttribute("totalItems", pageRooms.getTotalElements());
		model.addAttribute("totalPages", pageRooms.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("lessonId", lessonId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/rooms";
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(lessonId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "lessonId=%s".formatted(lessonId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "rooms";
	}

	@GetMapping("/{floor}/findAllByFloor")
	@Override
	public String findAllByFloor(@RequestParam(required = false) Long lessonId,
			@RequestParam(required = false) String purpose, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, Principal principal, @PathVariable Integer floor,
			Model model) {
		Page<RoomDto> pageRooms = roomService.findAllByFloor(floor,
				PageRequest.of(page - 1, size).withSort(Sort.by("id")));
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("rooms", pageRooms.getContent());
		model.addAttribute("currentPage", pageRooms.getNumber() + 1);
		model.addAttribute("totalItems", pageRooms.getTotalElements());
		model.addAttribute("totalPages", pageRooms.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("lessonId", lessonId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/rooms/%d/findAllByFloor".formatted(floor);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(lessonId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "lessonId=%s".formatted(lessonId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "rooms";
	}

	@GetMapping("/{id}/findById")
	@Override
	public String findById(@RequestParam(required = false) Long lessonId,
			@RequestParam(required = false) String purpose, @PathVariable Long id, Principal principal, Model model)
			throws ServiceException {
		RoomDto roomDto = roomService.findById(id);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("rooms", Arrays.asList(roomDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("lessonId", lessonId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/rooms/%d/findById".formatted(id);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(lessonId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "lessonId=%s".formatted(lessonId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "rooms";
	}

	@GetMapping("/{roomNumber}/findByRoomNumber")
	@Override
	public String findByRoomNumber(@RequestParam(required = false) Long lessonId,
			@RequestParam(required = false) String purpose, @PathVariable Integer roomNumber, Principal principal,
			Model model) throws ServiceException {
		RoomDto roomDto = roomService.findByRoomNumber(roomNumber);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("rooms", Arrays.asList(roomDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("lessonId", lessonId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/rooms/%d/findByRoomNumber".formatted(roomNumber);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(lessonId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?") + "lessonId=%s".formatted(lessonId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "rooms";
	}

	@GetMapping("/{lessonId}/findByLessonId")
	@Override
	public String findByLessonId(@RequestParam(required = false) Long destinationLessonId,
			@RequestParam(required = false) String purpose, @PathVariable Long lessonId, Principal principal,
			Model model) throws ServiceException {
		RoomDto roomDto = roomService.findByLessonId(lessonId);
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("principal", userDto);
		model.addAttribute("rooms", Arrays.asList(roomDto));
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalItems", 1);
		model.addAttribute("totalPages", 1);
		model.addAttribute("pageSize", 1);
		model.addAttribute("lessonId", destinationLessonId);
		model.addAttribute("purpose", purpose);
		String currentPagePath = "/rooms/%d/findByLessonId".formatted(lessonId);
		if (Objects.nonNull(purpose)) {
			currentPagePath += "?purpose=%s".formatted(purpose);
		}
		if (Objects.nonNull(destinationLessonId)) {
			currentPagePath += (currentPagePath.contains("?") ? "&" : "?")
					+ "lessonId=%s".formatted(destinationLessonId);
		}
		model.addAttribute("currentPagePath", currentPagePath);
		return "rooms";
	}

	@GetMapping("/{id}/update")
	@Override
	public String updateRoom(@PathVariable Long id, Model model, HttpServletRequest request) throws ServiceException {
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		RoomDto roomDto = roomService.findById(id);
		model.addAttribute("room", roomDto);
		model.addAttribute("referer", referer);
		return "room-update";
	}

	@GetMapping("/new")
	@Override
	public String createRoom(Model model, HttpServletRequest request) {
		RoomDto roomDto = new RoomDto();
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		model.addAttribute("room", roomDto);
		model.addAttribute("referer", referer);
		return "room-create";
	}

	@PostMapping("/save")
	@Override
	public String saveRoom(@ModelAttribute("room") RoomDto roomDto, Model model, HttpServletRequest request)
			throws ServiceDataIntegrityException {
		String referer = (request.getParameter("referer").contains("/new")
				|| request.getParameter("referer").contains("/update")) ? "/rooms" : request.getParameter("referer");
		model.addAttribute("referer", referer);
		String viewName = request.getParameter("viewName");

		try {
			roomService.save(roomDto);
		} catch (ConstraintViolationException e) {
			model.addAttribute("validationExceptions", e.getConstraintViolations());
			model.addAttribute("room", roomDto);
			if ("room-create".equals(viewName)) {
				return "room-create";
			}
			if ("room-update".equals(viewName)) {
				return "room-update";
			}
		} catch (ServiceDataIntegrityException e) {
			model.addAttribute("integrityExceptions", e.getExceptions());
			model.addAttribute("room", roomDto);
			if ("room-create".equals(viewName)) {
				return "room-create";
			}
			if ("room-update".equals(viewName)) {
				return "room-update";
			}
		}
		return "redirect:" + referer;
	}

	@PostMapping("/{id}/delete")
	@Override
	public String deleteRoom(@PathVariable Long id, HttpServletRequest request) throws ServiceException {
		String referer = request.getHeader("referer").replace("http://localhost:8080", "");
		roomService.deleteById(id);
		return "redirect:" + referer;
	}

}
