package ua.com.foxmineded.universitycms.dao;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.foxmineded.universitycms.entities.impl.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
	Page<Teacher> findAllByName(String name, Pageable pageable);

	Optional<Teacher> findByLogin(String login);

	Optional<Teacher> findByPassportNumber(String passportNumber);

	Optional<Teacher> findByEmail(String email);

	Optional<Teacher> findByTelephoneNumber(String telephoneNumber);

	@Query("from Teacher t join t.courses c where c.courseName = :courseName")
	Optional<Teacher> findByCourseName(String courseName);

	@Query("from Teacher t join t.courses c where c.id = :courseId")
	Optional<Teacher> findByCourseId(Long courseId);

	@Query("from Teacher t join t.courses c join c.lessons l where l.id = :lessonId")
	Optional<Teacher> findByLessonId(Long lessonId);
}
