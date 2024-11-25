package ua.com.foxmineded.universitycms.services;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxmineded.universitycms.entities.impl.Course;
import ua.com.foxmineded.universitycms.entities.impl.Student;
import ua.com.foxmineded.universitycms.entities.impl.Teacher;

public interface CourseGeneratorService {
	@Transactional
	List<Course> generateWithTeachers(List<Teacher> teachers);

	@Transactional
	void allocateStudentsToCourses(List<Student> students, List<Course> courses);

	@Transactional(readOnly = true)
	Long countAll();

	@Transactional(readOnly = true)
	List<Course> findAll();
}
