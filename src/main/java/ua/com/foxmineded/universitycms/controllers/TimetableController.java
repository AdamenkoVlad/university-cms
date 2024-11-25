package ua.com.foxmineded.universitycms.controllers;

import java.security.Principal;
import org.springframework.ui.Model;

public interface TimetableController {
	String getTimetableForUser(int addMonths, Principal principal, Model model);
}
