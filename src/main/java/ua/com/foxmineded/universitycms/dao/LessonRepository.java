package ua.com.foxmineded.universitycms.dao;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.foxmineded.universitycms.entities.impl.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
	Page<Lesson> findAllByLessonDate(LocalDate lessonDate, Pageable pageable);

	@Query("from Lesson l join l.course c where c.id = :courseId")
	Page<Lesson> findAllByCourseId(Long courseId, Pageable pageable);

	@Query("from Lesson l join l.course c where c.courseName = :courseName")
	Page<Lesson> findAllByCourseName(String courseName, Pageable pageable);

	@Query("from Lesson l join l.room r where r.id = :roomId")
	Page<Lesson> findAllByRoomId(Long roomId, Pageable pageable);

	@Query("select l from Student s join s.courses c join c.lessons l where s.id = :studentId")
	List<Lesson> findAllByStudentId(Long studentId);

	@Query("select l from Teacher t join t.courses c join c.lessons l where t.id = :teacherId")
	List<Lesson> findAllByTeacherId(Long teacherId);

}
