package ua.com.foxmineded.universitycms.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxmineded.universitycms.entities.impl.Room;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		RoomRepository.class }))
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = { "/test/sql/clear_tables.sql" })
class RoomRepositoryTest {
	@Autowired
	RoomRepository roomRepository;

	@Test
	@Sql(scripts = { "/test/sql/roomrepository/script0.sql" })
	void testFindByLessonId_AskFindByLessonId_RoomShouldBeReturned() {
		Long expectedRoomId = 10000L;
		Room room = roomRepository.findByLessonId(expectedRoomId).get();
		assertEquals(expectedRoomId, room.getId());
	}
}
