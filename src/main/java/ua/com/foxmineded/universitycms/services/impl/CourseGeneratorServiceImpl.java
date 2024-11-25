package ua.com.foxmineded.universitycms.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.instancio.Instancio;
import static org.instancio.Select.field;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxmineded.universitycms.dao.CourseRepository;
import ua.com.foxmineded.universitycms.dao.StudentRepository;
import ua.com.foxmineded.universitycms.entities.impl.Course;
import ua.com.foxmineded.universitycms.entities.impl.Student;
import ua.com.foxmineded.universitycms.entities.impl.Teacher;
import ua.com.foxmineded.universitycms.enums.Specialization;
import ua.com.foxmineded.universitycms.services.CourseGeneratorService;
import ua.com.foxmineded.universitycms.utils.CoursesNamesReader;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseGeneratorServiceImpl implements CourseGeneratorService {
	private final CoursesNamesReader coursesNamesReader;
	private final StudentRepository studentRepository;
	private final CourseRepository courseRepository;

	@SneakyThrows
	@Override
	public List<Course> generateWithTeachers(List<Teacher> teachers) {
		log.info("The course generation process was started");
		List<Course> resultList = new ArrayList<>();
		List<String> artCoursesNames = coursesNamesReader.readArtCourses();
		List<String> medicineCoursesNames = coursesNamesReader.readMedicineCourses();
		List<String> computerScienceCourses = coursesNamesReader.readComputerScienceCourses();
		List<String> economicCoursesReader = coursesNamesReader.readEconomicCourses();
		for (int i = 0; i < artCoursesNames.size(); i++) {
			Course course = Instancio.of(Course.class).ignore(field(Course::getId)).ignore(field(Course::getStudents))
					.ignore(field(Course::getLessons)).ignore(field(Course::getTeacher))
					.set(field(Course::getSpecialization), Specialization.ART)
					.set(field(Course::getCourseName), artCoursesNames.get(i)).create();
			Course persistentCourse = courseRepository.save(course);
			persistentCourse.setTeacher(teachers.get(i));
			resultList.add(persistentCourse);
		}
		for (int i = 0; i < medicineCoursesNames.size(); i++) {
			Course course = Instancio.of(Course.class).ignore(field(Course::getId)).ignore(field(Course::getStudents))
					.ignore(field(Course::getLessons)).ignore(field(Course::getTeacher))
					.set(field(Course::getSpecialization), Specialization.MEDICINE)
					.set(field(Course::getCourseName), medicineCoursesNames.get(i)).create();
			Course persistentCourse = courseRepository.save(course);
			persistentCourse.setTeacher(teachers.get(10 + i));
			resultList.add(persistentCourse);
		}

		for (int i = 0; i < computerScienceCourses.size(); i++) {
			Course course = Instancio.of(Course.class).ignore(field(Course::getId)).ignore(field(Course::getStudents))
					.ignore(field(Course::getLessons)).ignore(field(Course::getTeacher))
					.set(field(Course::getSpecialization), Specialization.COMPUTER_SCIENCE)
					.set(field(Course::getCourseName), computerScienceCourses.get(i)).create();
			Course persistentCourse = courseRepository.save(course);
			persistentCourse.setTeacher(teachers.get(20 + i));
			resultList.add(persistentCourse);
		}

		for (int i = 0; i < economicCoursesReader.size(); i++) {
			Course course = Instancio.of(Course.class).ignore(field(Course::getId)).ignore(field(Course::getStudents))
					.ignore(field(Course::getLessons)).ignore(field(Course::getTeacher))
					.set(field(Course::getSpecialization), Specialization.ECONOMICS)
					.set(field(Course::getCourseName), economicCoursesReader.get(i)).create();
			Course persistentCourse = courseRepository.save(course);
			persistentCourse.setTeacher(teachers.get(30 + i));
			resultList.add(persistentCourse);
		}
		return courseRepository.saveAll(resultList);
	}

	@Override
	public void allocateStudentsToCourses(List<Student> students, List<Course> courses) {
		Random random = new Random();
		log.info("The allocation process students to courses was started");
		List<Course> artCourses = courseRepository.findAllBySpecialization(Specialization.ART, PageRequest.of(0, 10))
				.getContent();
		List<Course> medicineCourses = courseRepository
				.findAllBySpecialization(Specialization.MEDICINE, PageRequest.of(0, 10)).getContent();
		List<Course> computerScienceCourses = courseRepository
				.findAllBySpecialization(Specialization.COMPUTER_SCIENCE, PageRequest.of(0, 10)).getContent();
		List<Course> economicCourses = courseRepository
				.findAllBySpecialization(Specialization.ECONOMICS, PageRequest.of(0, 10)).getContent();

		Map<Specialization, List<Course>> map = new HashMap<>();
		map.put(Specialization.ART, artCourses);
		map.put(Specialization.MEDICINE, medicineCourses);
		map.put(Specialization.COMPUTER_SCIENCE, computerScienceCourses);
		map.put(Specialization.ECONOMICS, economicCourses);

		List<Student> students1 = studentRepository.findAll();

		students1.stream().forEach(student -> {
			List<Course> coursesOnSpecialization = map.get(student.getSpecialization());
			int amountCourses = random.nextInt(4) + 4;
			for (int i = 0; i < amountCourses; i++) {
				int courseNumber = random.nextInt(10);
				Course course = coursesOnSpecialization.get(courseNumber);
				if (student.getCourses().contains(course)) {
					continue;
				}
				course.getStudents().add(student);
				student.getCourses().add(course);
				studentRepository.save(student);
				studentRepository.flush();
			}

		});
	}

	@Override
	public Long countAll() {
		return courseRepository.count();
	}

	@Override
	public List<Course> findAll() {
		return courseRepository.findAll();
	}
}
