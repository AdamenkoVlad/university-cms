package ua.com.foxmineded.universitycms.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.foxmineded.universitycms.entities.impl.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByLogin(String login);
}
