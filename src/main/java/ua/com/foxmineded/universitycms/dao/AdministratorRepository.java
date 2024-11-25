package ua.com.foxmineded.universitycms.dao;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxmineded.universitycms.entities.impl.Administrator;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

	Optional<Administrator> findByLogin(String login);

	Optional<Administrator> findByPassportNumber(String passportNumber);

	Optional<Administrator> findByEmail(String email);

	Optional<Administrator> findByTelephoneNumber(String telephoneNumber);

	Page<Administrator> findAllByName(String name, Pageable pageable);
}
