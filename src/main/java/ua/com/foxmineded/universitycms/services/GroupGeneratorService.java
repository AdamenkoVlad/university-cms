package ua.com.foxmineded.universitycms.services;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxmineded.universitycms.entities.impl.Group;

public interface GroupGeneratorService {
	@Transactional
	List<Group> generate();

	@Transactional(readOnly = true)
	Long countAll();

	@Transactional(readOnly = true)
	List<Group> findAll();
}
