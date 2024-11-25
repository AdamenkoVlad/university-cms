package ua.com.foxmineded.universitycms.entities.impl;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

import org.instancio.Instancio;
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
class RoomTest {
	@PersistenceContext
	EntityManager em;

	@Test
	void testToPersistRoom() {
		Room roomInput = Instancio.of(Room.class).ignore(field(Room::getId)).ignore(field(Room::getLesson)).create();
		assertNull(roomInput.getId());
		em.persist(roomInput);
		em.flush();
		em.clear();
		Room roomSaved = em.find(Room.class, roomInput.getId());
		assertNotNull(roomSaved.getId());
		assertNotNull(roomSaved.getRoomNumber());
		assertNotNull(roomSaved.getFloor());
	}
}
