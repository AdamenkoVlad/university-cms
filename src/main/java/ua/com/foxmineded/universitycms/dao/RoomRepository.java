package ua.com.foxmineded.universitycms.dao;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.foxmineded.universitycms.entities.impl.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

	Page<Room> findAllByFloor(int floor, Pageable pageable);

	Optional<Room> findByRoomNumber(int roomNumber);

	@Query("from Room r join r.lesson l where l.id = :lessonId")
	Optional<Room> findByLessonId(Long lessonId);
}
