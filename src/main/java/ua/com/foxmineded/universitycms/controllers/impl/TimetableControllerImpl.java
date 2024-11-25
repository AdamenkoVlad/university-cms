package ua.com.foxmineded.universitycms.controllers.impl;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import static java.util.stream.Collectors.toCollection;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.RequiredArgsConstructor;
import ua.com.foxmineded.universitycms.controllers.TimetableController;
import ua.com.foxmineded.universitycms.dto.LessonDto;
import ua.com.foxmineded.universitycms.dto.UserDto;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.services.LessonService;

@Controller
@RequestMapping("/timetable")
@RequiredArgsConstructor
public class TimetableControllerImpl implements TimetableController {	
	private static final int DAYS_OF_WEEK = 7;
	
	private final LessonService lessonService;
	private final UserDetailsService userDetailsService;

	@GetMapping
	@Override
	public String getTimetableForUser(@RequestParam(defaultValue = "0") int addMonths, Principal principal, Model model) {
		UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(principal.getName());	
		List<LessonDto> allLessons = new ArrayList<>();
		if (userDto.getRole().equals(Role.STUDENT)) {
			allLessons = lessonService.findAllByStudentId(userDto.getId());
		} else if (userDto.getRole().equals(Role.TEACHER)) {
			allLessons = lessonService.findAllByTeacherId(userDto.getId());
		}
		
		LocalDate currentDate = LocalDate.now().plusMonths(addMonths);
		LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
		DayOfWeek firstDayOfWeek = firstDayOfMonth.getDayOfWeek();
		LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
		DayOfWeek lastDayOfWeek = lastDayOfMonth.getDayOfWeek();		
		
		Long weeksInMonth = ChronoUnit.WEEKS.between(firstDayOfMonth, lastDayOfMonth) + 1;
		Map<Integer, Map<Integer, List<LessonDto>>> timetable = new LinkedHashMap<>();
		
		int dayNumber = 0;
		int weekNumber = 0;
		Map<Integer,List<LessonDto>> days1 = new LinkedHashMap<>();
		for (int j = 0; j < DAYS_OF_WEEK; j++) {
			if (j + 1 < firstDayOfWeek.ordinal() + 1) {
				days1.put(j + 1, null);
			} else {
				LocalDate currentDate1 = firstDayOfMonth.plusDays(dayNumber);
				List<LessonDto> sortedLessonsToday = allLessons.stream()
						.filter(lesson -> lesson.getLessonDate().equals(currentDate1))
						.sorted((lesson1, lesson2) -> lesson1.getLessonNumber().compareTo(lesson2.getLessonNumber()))
						.collect(toCollection(ArrayList::new));
				days1.put(j + 1, sortedLessonsToday);
				dayNumber++;
			}
		}
		weekNumber++;
		timetable.put(weekNumber, days1);
		for (int i = 1; i < weeksInMonth - 1; i++) {
			Map<Integer,List<LessonDto>> days2 = new LinkedHashMap<>();
			for (int j = 0; j < DAYS_OF_WEEK; j++) {
				LocalDate currentDate1 = firstDayOfMonth.plusDays(dayNumber);
				List<LessonDto> sortedLessonsToday = allLessons.stream()
						.filter(lesson -> lesson.getLessonDate().equals(currentDate1))
						.sorted((lesson1, lesson2) -> lesson1.getLessonNumber().compareTo(lesson2.getLessonNumber()))
						.collect(toCollection(ArrayList::new));
				days2.put(j + 1, sortedLessonsToday);	
				dayNumber++;
			}
			weekNumber++;
			timetable.put(weekNumber, days2);
		}
		Map<Integer,List<LessonDto>> days3 = new LinkedHashMap<>();
		for (int j = 0; j < DAYS_OF_WEEK; j++) {
			if (j + 1 >  lastDayOfWeek.ordinal() + 1) {
				days3.put(j + 1,null);

			} else {
				LocalDate currentDate1 = firstDayOfMonth.plusDays(dayNumber);
				List<LessonDto> sortedLessonsToday = allLessons.stream()
						.filter(lesson -> lesson.getLessonDate().equals(currentDate1))
						.sorted((lesson1, lesson2) -> lesson1.getLessonNumber().compareTo(lesson2.getLessonNumber()))
						.collect(toCollection(ArrayList::new));
				days3.put(j + 1, sortedLessonsToday);
				dayNumber++;
			}
		}
		
		weekNumber++;
		timetable.put(weekNumber, days3);
			
		model.addAttribute("timetable", timetable);
		model.addAttribute("principal", userDto);
		model.addAttribute("todayDate", LocalDate.now());
		model.addAttribute("year", currentDate.getYear());
		model.addAttribute("addMonths", addMonths);
		model.addAttribute("currentUrl", "/timetable");
		model.addAttribute("month", currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
		return "timetable";
	}
}
