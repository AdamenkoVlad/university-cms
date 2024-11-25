package ua.com.foxmineded.universitycms.services;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import ua.com.foxmineded.universitycms.entities.impl.Teacher;

public interface TeacherGeneratorService {
	@Transactional
	List<Teacher> generate();

	@Transactional(readOnly = true)
	Long countAll();

	@Transactional(readOnly = true)
	List<Teacher> findAll();
}
