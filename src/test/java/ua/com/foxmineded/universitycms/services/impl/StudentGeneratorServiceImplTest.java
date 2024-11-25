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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxmineded.universitycms.config.SecurityConfig;
import ua.com.foxmineded.universitycms.config.TypeMapConfig;
import ua.com.foxmineded.universitycms.dao.StudentRepository;
import ua.com.foxmineded.universitycms.entities.impl.Group;
import ua.com.foxmineded.universitycms.entities.impl.Student;
import ua.com.foxmineded.universitycms.services.GroupGeneratorService;
import ua.com.foxmineded.universitycms.services.StudentGeneratorService;
import ua.com.foxmineded.universitycms.utils.PersonNamesReader;
import ua.com.foxmineded.universitycms.utils.StudentPhotoReader;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		PersonNamesReader.class, StudentPhotoReader.class, StudentRepository.class, StudentGeneratorService.class,
		GroupGeneratorService.class, SecurityConfig.class, JpaUserDetailsServiceImpl.class, TypeMapConfig.class }))
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class StudentGeneratorServiceImplTest {
	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	StudentRepository studentRepository;
	@Autowired
	StudentGeneratorService studentGeneratorService;
	@Autowired
	GroupGeneratorService groupGeneratorService;

	@Test
	void testGenerateWithGroups_AskGenerateTestData_AllDataShouldBeValid() {
		int expectedStudentAmount = 800;
		List<Group> groups = groupGeneratorService.generate();
		assertDoesNotThrow(() -> {
			studentGeneratorService.generateWithGroups(groups);
		});
		List<Student> students = studentRepository.findAll();
		assertEquals(expectedStudentAmount, students.size());
		for (int i = 0; i < 10; i++) {
			assertNotNull(students.get(i).getId());
			assertNotNull(students.get(i).getGender());
		}
	}
}
