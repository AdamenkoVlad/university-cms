package ua.com.foxmineded.universitycms.services;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxmineded.universitycms.entities.impl.Administrator;

public interface AdministratorGeneratorService {
	@Transactional
	List<Administrator> generate();

	@Transactional(readOnly = true)
	Long countAll();
	
	@Transactional(readOnly = true) 
	List<Administrator> findAll();
}
