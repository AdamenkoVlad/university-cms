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
import ua.com.foxmineded.universitycms.dao.RoomRepository;
import ua.com.foxmineded.universitycms.entities.impl.Room;
import ua.com.foxmineded.universitycms.services.RoomGeneratorService;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { RoomRepository.class,
		RoomGeneratorService.class }))
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
class RoomGeneratorServiceImplTest {
	@Autowired
	RoomRepository roomRepository;
	@Autowired
	RoomGeneratorService roomGeneratorService;

	@Test
	void testGenerate_AskToGenerateValidRooms_RoomShouldBeValid() {
		assertDoesNotThrow(() -> {
			roomGeneratorService.generate();
		});
		List<Room> rooms = roomRepository.findAll();
		for (Room room : rooms) {
			assertNotNull(room.getId());
			assertNotNull(room.getFloor());
			assertNotNull(room.getRoomNumber());
		}
	}
}
