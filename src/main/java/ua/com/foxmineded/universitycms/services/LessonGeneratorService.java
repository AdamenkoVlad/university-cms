package ua.com.foxmineded.universitycms.services;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import ua.com.foxmineded.universitycms.entities.impl.Room;
import ua.com.foxmineded.universitycms.entities.impl.Lesson;
import ua.com.foxmineded.universitycms.entities.impl.Course;

public interface LessonGeneratorService {
	@Transactional
	List<Lesson> generateWithCoursesAndRooms(List<Course> courses, List<Room> rooms);

	@Transactional(readOnly = true)
	Long countAll();

	@Transactional(readOnly = true)
	List<Lesson> findAll();
}
