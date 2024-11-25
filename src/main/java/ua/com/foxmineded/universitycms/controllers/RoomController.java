package ua.com.foxmineded.universitycms.controllers;

import java.security.Principal;

import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;
import ua.com.foxmineded.universitycms.dto.RoomDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface RoomController {

	String findAll(Long lessonId, String purpose, int page, int size, Principal principal, Model model);

	String findAllByFloor(Long lessonId, String purpose, int page, int size, Principal principal, Integer floor,
			Model model);

	String findById(Long lessonId, String purpose, Long id, Principal principal, Model model) throws ServiceException;

	String findByRoomNumber(Long lessonId, String purpose, Integer roomNumber, Principal principal, Model model)
			throws ServiceException;

	String findByLessonId(Long destinationLessonId, String purpose, Long lessonId, Principal principal, Model model)
			throws ServiceException;

	String saveRoom(RoomDto roomDto, Model model, HttpServletRequest request) throws ServiceDataIntegrityException;

	String updateRoom(Long id, Model model, HttpServletRequest request) throws ServiceException;

	String createRoom(Model model, HttpServletRequest request);

	String deleteRoom(Long roomId, HttpServletRequest request) throws ServiceException;
}
