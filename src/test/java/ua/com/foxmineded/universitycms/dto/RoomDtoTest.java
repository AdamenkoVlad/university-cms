package ua.com.foxmineded.universitycms.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.com.foxmineded.universitycms.entities.impl.Room;

@SpringBootTest(classes = ModelMapper.class)
class RoomDtoTest {
	@Autowired
	ModelMapper modelMapper;

	@Test
	void testMapEntityToDto() {
		Room room = Instancio.create(Room.class);
		RoomDto roomDto = modelMapper.map(room, RoomDto.class);

		assertEquals(room.getId(), roomDto.getId());
		assertEquals(room.getRoomNumber(), roomDto.getRoomNumber());
		assertEquals(room.getFloor(), roomDto.getFloor());
	}

	@Test
	void testMapDtoToEntity() {
		RoomDto roomDto = Instancio.create(RoomDto.class);
		Room room = modelMapper.map(roomDto, Room.class);

		assertEquals(roomDto.getId(), room.getId());
		assertEquals(roomDto.getRoomNumber(), room.getRoomNumber());
		assertEquals(roomDto.getFloor(), room.getFloor());
	}
}
