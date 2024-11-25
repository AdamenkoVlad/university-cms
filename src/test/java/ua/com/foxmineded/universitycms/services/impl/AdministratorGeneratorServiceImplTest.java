package ua.com.foxmineded.universitycms.services.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
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
import ua.com.foxmineded.universitycms.dao.AdministratorRepository;
import ua.com.foxmineded.universitycms.entities.impl.Administrator;
import ua.com.foxmineded.universitycms.services.AdministratorGeneratorService;
import ua.com.foxmineded.universitycms.utils.AdministratorPhotoReader;
import ua.com.foxmineded.universitycms.utils.PersonNamesReader;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		PersonNamesReader.class, AdministratorPhotoReader.class, AdministratorRepository.class,
		AdministratorGeneratorService.class, SecurityConfig.class, TypeMapConfig.class, UserDetailsService.class }))
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class AdministratorGeneratorServiceImplTest {
	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	AdministratorRepository administratorRepository;
	@Autowired
	AdministratorGeneratorService administratorGeneratorService;

	@Test
	void testGenerate_AskToGenerateTestData_AllDataShouldBeValid() throws IOException {
		int expectedAmount = 11;
		assertDoesNotThrow(() -> {
			administratorGeneratorService.generate();
		});
		List<Administrator> administrators = administratorRepository.findAll();
		assertEquals(expectedAmount, administrators.size());
		for (int i = 0; i < administrators.size(); i++) {
			assertNotNull(administrators.get(i).getId());
		}
	}
}
