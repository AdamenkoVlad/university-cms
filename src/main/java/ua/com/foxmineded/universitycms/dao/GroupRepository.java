package ua.com.foxmineded.universitycms.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ua.com.foxmineded.universitycms.entities.impl.Group;
import ua.com.foxmineded.universitycms.enums.Specialization;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
	Optional<Group> findByGroupName(String groupName);
	
	@Query("from Group g join g.students s where s.id = :studentId")
	Optional<Group> findByStudentId(Long studentId);

	Page<Group> findAllBySpecialization(Specialization specialization, Pageable pageable);

	@Query("from Group g where size(g.students) <= :studentAmount")
	Page<Group> findAllWithLessOrEqualStudents(int studentAmount, Pageable pageable);

	void deleteByGroupName(String groupName);
}
