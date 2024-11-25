package ua.com.foxmineded.universitycms.services.impl;

import java.util.ArrayList;
import java.util.List;
import org.instancio.Instancio;
import static org.instancio.Select.field;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxmineded.universitycms.dao.RoomRepository;
import ua.com.foxmineded.universitycms.entities.impl.Room;
import ua.com.foxmineded.universitycms.services.RoomGeneratorService;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomGeneratorServiceImpl implements RoomGeneratorService {
	private static final int FLOORS_AMOUNT = 3;
	private static final int ROOMS_AMOUNT = 300;

	private final RoomRepository roomRepository;

	@Override
	public List<Room> generate() {
		log.info("The room generation process was started");
		List<Room> rooms = new ArrayList<>();
		for (int i = 0; i < FLOORS_AMOUNT; i++) {
			for (int j = 0; j < ROOMS_AMOUNT / FLOORS_AMOUNT; j++) {
				Room room = Instancio.of(Room.class).ignore(field(Room::getId)).ignore(field(Room::getLesson))
						.set(field(Room::getFloor), i + 1).set(field(Room::getRoomNumber), i * 100 + j + 1).create();
				rooms.add(room);
			}
		}
		return roomRepository.saveAll(rooms);
	}

	@Override
	public Long countAll() {
		return roomRepository.count();
	}

	@Override
	public List<Room> findAll() {
		return roomRepository.findAll();
	}
}
