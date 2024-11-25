package ua.com.foxmineded.universitycms.dao;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.foxmineded.universitycms.entities.impl.Course;
import ua.com.foxmineded.universitycms.enums.Specialization;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

	Optional<Course> findByCourseName(String courseName);

	Optional<Course> findByCourseDescription(String courseDescription);

	Page<Course> findAllBySpecialization(Specialization specialization, Pageable pageable);

	@Query("from Course c join c.teacher where c.teacher.id = :teacherId")
	Page<Course> findAllByTeacherId(Long teacherId, Pageable pageable);
	
	@Query("from Course c join c.students s where s.id = :studentId")
	Page<Course> findAllByStudentId(Long studentId, Pageable pageable);

	@Query("from Course c join c.teacher where c.teacher.name = :teacherName")
	Page<Course> findAllByTeacherName(String teacherName, Pageable pageable);

	@Query("from Course c join c.lessons l join l.room r where r.id = :roomId")
	Page<Course> findAllByRoomId(Long roomId, Pageable pageable);

	@Query("from Course c join c.lessons l join l.room r where r.roomNumber = :roomNumber")
	Page<Course> findAllByRoomNumber(int roomNumber, Pageable pageable);
}
