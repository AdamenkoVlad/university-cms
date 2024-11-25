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

import ua.com.foxmineded.universitycms.dao.GroupRepository;
import ua.com.foxmineded.universitycms.entities.impl.Group;
import ua.com.foxmineded.universitycms.services.GroupGeneratorService;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		GroupRepository.class, GroupGeneratorService.class }))
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class GroupGeneratorServiceImplTest {
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	GroupGeneratorService groupGeneratorService;

	@Test
	void testGenerate_ShouldGenerateTestData_TestDataShouldBeValid() {
		int expectedGroupsAmount = 27;
		assertDoesNotThrow(() -> {
			groupGeneratorService.generate();
		});
		List<Group> groups = groupRepository.findAll();
		assertEquals(expectedGroupsAmount, groups.size());
		for (int i = 0; i < groups.size(); i++) {
			assertNotNull(groups.get(i).getId());
		}
	}
}
