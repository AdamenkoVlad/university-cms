package ua.com.foxmineded.universitycms.entities.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.instancio.Instancio;
import static org.instancio.Select.field;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class GroupTest {
	@PersistenceContext
	EntityManager em;

	@Test
	void testToPersistGroup() {
		Group groupInput = Instancio.of(Group.class).ignore(field(Group::getId)).ignore(field(Group::getStudents))
				.generate(field(Group::getGroupName), gen -> gen.text().pattern("#C#C-#d#d")).create();
		assertNull(groupInput.getId());
		em.persist(groupInput);
		em.flush();
		em.clear();
		Group groupSaved = em.find(Group.class, groupInput.getId());
		assertNotNull(groupSaved.getId());
		assertNotNull(groupSaved.getGroupName());
		assertNotNull(groupSaved.getSpecialization());
	}
}
