package ua.com.foxmineded.universitycms.dao;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxmineded.universitycms.entities.impl.Avatar;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, UUID> {
}
