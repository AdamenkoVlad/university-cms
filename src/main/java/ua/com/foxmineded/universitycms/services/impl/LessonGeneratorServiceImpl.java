package ua.com.foxmineded.universitycms.services.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxmineded.universitycms.dao.LessonRepository;
import ua.com.foxmineded.universitycms.entities.impl.Course;
import ua.com.foxmineded.universitycms.entities.impl.Lesson;
import ua.com.foxmineded.universitycms.entities.impl.Room;
import ua.com.foxmineded.universitycms.enums.Specialization;
import ua.com.foxmineded.universitycms.services.LessonGeneratorService;

@Service
@Slf4j
@RequiredArgsConstructor
public class LessonGeneratorServiceImpl implements LessonGeneratorService {
	private static final int WEEKS_AMOUNT = 26;
	private static final int DAYS_IN_WEEK = 7;

	private final LessonRepository lessonRepository;

	@Override
	public List<Lesson> generateWithCoursesAndRooms(List<Course> courses, List<Room> rooms) {
		log.info("The lessons generation process was started");
		Random random = new Random();
		List<Lesson> lessons = new ArrayList<>();
		List<Course> artCourses = courses.stream().filter(course -> course.getSpecialization() == Specialization.ART)
				.toList();
		List<Course> computerScienceCourses = courses.stream()
				.filter(course -> course.getSpecialization() == Specialization.COMPUTER_SCIENCE).toList();
		List<Course> medicineCourses = courses.stream()
				.filter(course -> course.getSpecialization() == Specialization.MEDICINE).toList();
		List<Course> economicCourses = courses.stream()
				.filter(course -> course.getSpecialization() == Specialization.ECONOMICS).toList();

		LocalDate now = LocalDate.now().withDayOfMonth(1);
		for (int i = 0; i < WEEKS_AMOUNT * DAYS_IN_WEEK; i++) {
			LocalDate currentDate = now.plusDays(i);
			if (currentDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)
					|| currentDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
				continue;
			}
			for (int j = 0; j < artCourses.size(); j++) {
				int roomIndex = random.nextInt(rooms.size());
				Lesson lesson = new Lesson(null, currentDate, j + 1, null, null);
				lessonRepository.save(lesson);
				lessonRepository.flush();
				Course course = artCourses.get(j);
				Room room = rooms.get(roomIndex);
				lesson.setCourse(course);
				lesson.setRoom(room);
				lessonRepository.save(lesson);
				lessonRepository.flush();
				lessons.add(lesson);
			}
			for (int j = 0; j < computerScienceCourses.size(); j++) {
				int roomIndex = random.nextInt(rooms.size());
				Lesson lesson = new Lesson(null, currentDate, j + 1, null, null);
				lessonRepository.save(lesson);
				lessonRepository.flush();
				Course course = computerScienceCourses.get(j);
				Room room = rooms.get(roomIndex);
				lesson.setCourse(course);
				lesson.setRoom(room);
				lessonRepository.save(lesson);
				lessonRepository.flush();
				lessons.add(lesson);
			}
			for (int j = 0; j < medicineCourses.size(); j++) {
				int roomIndex = random.nextInt(rooms.size());
				Lesson lesson = new Lesson(null, currentDate, j + 1, null, null);
				lessonRepository.save(lesson);
				lessonRepository.flush();
				Course course = medicineCourses.get(j);
				Room room = rooms.get(roomIndex);
				lesson.setCourse(course);
				lesson.setRoom(room);
				lessonRepository.save(lesson);
				lessonRepository.flush();
				lessons.add(lesson);
			}
			for (int j = 0; j < economicCourses.size(); j++) {
				int roomIndex = random.nextInt(rooms.size());
				Lesson lesson = new Lesson(null, currentDate, j + 1, null, null);
				lessonRepository.save(lesson);
				lessonRepository.flush();
				Course course = economicCourses.get(j);
				Room room = rooms.get(roomIndex);
				lesson.setCourse(course);
				lesson.setRoom(room);
				lessonRepository.save(lesson);
				lessonRepository.flush();
				lessons.add(lesson);
			}
		}
		return lessons;
	}

	@Override
	public Long countAll() {
		return lessonRepository.count();
	}

	@Override
	public List<Lesson> findAll() {
		return lessonRepository.findAll();
	}
}
