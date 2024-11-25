package ua.com.foxmineded.universitycms.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import ua.com.foxmineded.universitycms.config.SecurityConfig;
import ua.com.foxmineded.universitycms.config.TypeMapConfig;
import ua.com.foxmineded.universitycms.dao.TeacherRepository;
import ua.com.foxmineded.universitycms.entities.impl.Teacher;
import ua.com.foxmineded.universitycms.services.TeacherGeneratorService;
import ua.com.foxmineded.universitycms.utils.PersonNamesReader;
import ua.com.foxmineded.universitycms.utils.TeacherPhotoReader;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		TeacherPhotoReader.class, TypeMapConfig.class, PersonNamesReader.class, TeacherRepository.class,
		TeacherGeneratorService.class, JpaUserDetailsServiceImpl.class, SecurityConfig.class }))
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class TeacherGeneratorServiceImplTest {
	@Autowired
	TeacherRepository teacherRepository;
	@Autowired
	TeacherGeneratorService teacherGeneratorService;

	@Test
	void testGenerate_AskToGenerateTestData_TestDataShouldBeValid() {
		int expectedTeacherAmount = 40;
		assertDoesNotThrow(() -> {
			teacherGeneratorService.generate();
		});
		List<Teacher> teachers = teacherRepository.findAll();
		assertEquals(expectedTeacherAmount, teachers.size());
		for (int i = 0; i < teachers.size(); i++) {
			assertNotNull(teachers.get(i).getId());
		}
	}
}
