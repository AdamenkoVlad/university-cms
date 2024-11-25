package ua.com.foxmineded.universitycms.services;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxmineded.universitycms.entities.impl.Group;
import ua.com.foxmineded.universitycms.entities.impl.Student;

public interface StudentGeneratorService {
	@Transactional
	List<Student> generateWithGroups(List<Group> groupList);

	@Transactional(readOnly = true)
	Long countAll();
	
	@Transactional(readOnly = true) 
	List<Student> findAll();
}
