package ua.com.foxmineded.universitycms.services;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxmineded.universitycms.entities.impl.Room;

public interface RoomGeneratorService {
	@Transactional
	List<Room> generate();

	@Transactional(readOnly = true)
	Long countAll();

	@Transactional(readOnly = true)
	List<Room> findAll();
}
