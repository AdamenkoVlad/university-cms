package ua.com.foxmineded.universitycms.dao;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.foxmineded.universitycms.entities.impl.Student;
import ua.com.foxmineded.universitycms.enums.Specialization;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	Optional<Student> findByPassportNumber(String passportNumber);

	Optional<Student> findByLogin(String login);

	Optional<Student> findByEmail(String email);

	Optional<Student> findByTelephoneNumber(String telephoneNumber);

	Page<Student> findAllByName(String name, Pageable pageable);

	@Query("from Student s join s.group g where g.groupName = :groupName")
	Page<Student> findAllByGroupName(String groupName, Pageable pageable);

	@Query("from Student s join s.courses c where c.courseName = :courseName")
	Page<Student> findAllByCourseName(String courseName, Pageable pageable);

	@Query("from Student s join s.courses c join c.teacher t where t.name = :teacherName")
	Page<Student> findAllByTeacherName(String teacherName, Pageable pageable);

	Page<Student> findAllBySpecialization(Specialization specialization, Pageable pageable);
}
