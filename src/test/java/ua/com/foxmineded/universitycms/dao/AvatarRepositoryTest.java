package ua.com.foxmineded.universitycms.dao;

import org.instancio.Instancio;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ua.com.foxmineded.universitycms.entities.impl.Avatar;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		AvatarRepository.class }))
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = { "/test/sql/clear_tables.sql" })
class AvatarRepositoryTest {
	@Autowired
	AvatarRepository avatarRepository;
	@PersistenceContext
	EntityManager entityManager;

	@Test
	void testFindById_AskFindValidEntityById_EntityShouldBeFound() {
		Avatar avatar = Instancio.of(Avatar.class).ignore(field(Avatar::getId)).create();
		assertEquals(0, avatarRepository.findAll().size());
		Avatar avatarInput = avatarRepository.save(avatar);

		entityManager.flush();
		entityManager.clear();

		assertTrue(avatarRepository.findById(avatarInput.getId()).isPresent());
	}
}
