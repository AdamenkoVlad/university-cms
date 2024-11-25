package ua.com.foxmineded.universitycms.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxmineded.universitycms.entities.impl.Group;
import ua.com.foxmineded.universitycms.enums.Specialization;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		GroupRepository.class }))
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = { "/test/sql/clear_tables.sql" })
class GroupRepositoryTest {
	@Autowired
	GroupRepository groupRepository;

	@Test
	@Sql(scripts = "/test/sql/grouprepository/script0.sql")
	void testFindAllWithLessOrEqualStudents_AskFindAllGroups_GroupListShouldBeReturned() {
		int studentAmount = 2;
		int groupsFound = 2;
		Long expectedGroupId0 = 10001L;
		Long expectedGroupId1 = 10002L;
		Page<Group> actualPage = groupRepository.findAllWithLessOrEqualStudents(studentAmount, Pageable.ofSize(10));
		assertEquals(groupsFound, actualPage.getContent().size());
		assertEquals(expectedGroupId0, actualPage.getContent().get(0).getId());
		assertEquals(expectedGroupId1, actualPage.getContent().get(1).getId());
	}

	@Test
	@Sql(scripts = "/test/sql/grouprepository/script0.sql")
	void testFindByStudentId_AskFindByStudentId_GroupListShouldBeReturned() {
		Long studentId = 10000L;
		Long groupId = 10000L;
		Group group = groupRepository.findByStudentId(studentId).get();
		assertEquals(groupId, group.getId());
		assertEquals("AA-11", group.getGroupName());
		assertEquals(Specialization.COMPUTER_SCIENCE, group.getSpecialization());
	}
}
