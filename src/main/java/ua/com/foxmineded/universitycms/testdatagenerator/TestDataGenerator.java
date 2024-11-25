package ua.com.foxmineded.universitycms.testdatagenerator;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxmineded.universitycms.services.AdministratorGeneratorService;
import ua.com.foxmineded.universitycms.services.ConcurrentDataImporterService;
import ua.com.foxmineded.universitycms.services.CourseGeneratorService;
import ua.com.foxmineded.universitycms.services.GroupGeneratorService;
import ua.com.foxmineded.universitycms.services.LessonGeneratorService;
import ua.com.foxmineded.universitycms.services.RoomGeneratorService;
import ua.com.foxmineded.universitycms.services.StudentGeneratorService;
import ua.com.foxmineded.universitycms.services.TeacherGeneratorService;

@Profile("!test")
@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataGenerator implements ApplicationRunner {
	private final GroupGeneratorService groupGeneratorService;
	private final AdministratorGeneratorService administratorGeneratorService;
	private final StudentGeneratorService studentGeneratorService;
	private final TeacherGeneratorService teacherGeneratorService;
	private final CourseGeneratorService courseGeneratorService;
	private final RoomGeneratorService roomGeneratorService;
	private final LessonGeneratorService lessonGeneratorService;
	private final ConcurrentDataImporterService concurrentDataImporterService;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		log.info("The test data generation process was started. It may take some time...");
		concurrentDataImporterService.importConcurrently(() -> administratorGeneratorService.generate(),
				() -> groupGeneratorService.generate(), () -> teacherGeneratorService.generate(),
				() -> roomGeneratorService.generate());
		log.info("%d administrators were generated".formatted(administratorGeneratorService.countAll()));
		log.info("%d groups were generated".formatted(groupGeneratorService.countAll()));
		log.info("%d teachers were generated".formatted(teacherGeneratorService.countAll()));
		log.info("%d rooms were generated".formatted(roomGeneratorService.countAll()));

		concurrentDataImporterService.importConcurrently(
				() -> studentGeneratorService.generateWithGroups(groupGeneratorService.findAll()),
				() -> courseGeneratorService.generateWithTeachers(teacherGeneratorService.findAll()));
		log.info("%d students were allocated to groups".formatted(studentGeneratorService.countAll()));
		log.info("%d courses were created".formatted(courseGeneratorService.countAll()));

		concurrentDataImporterService.importConcurrently(
				() -> courseGeneratorService.allocateStudentsToCourses(studentGeneratorService.findAll(),
						courseGeneratorService.findAll()),
				() -> lessonGeneratorService.generateWithCoursesAndRooms(courseGeneratorService.findAll(),
						roomGeneratorService.findAll()));
		log.info("The students were allocated to courses");
		log.info("%d lessons were generated".formatted(lessonGeneratorService.countAll()));

		log.info("The data generation process is completed");

	}
}
