package ua.com.foxmineded.universitycms.entities;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.com.foxmineded.universitycms.enums.Gender;
import ua.com.foxmineded.universitycms.enums.Role;

@Entity
@Table(schema = "university", name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "role", length = 20)
@ToString(callSuper = true)
@Getter
@Setter
public abstract class AbstractUser extends AbstractPerson {
	@Column(name = "name", length = 40, nullable = false)
	protected String name;
	@Column(name = "login", length = 50, nullable = false, unique = true)
	protected String login;
	@Column(name = "password", nullable = false)
	protected String password;
	@Column(name = "role", insertable = false, updatable = false)
	@Enumerated(EnumType.STRING)
	protected Role role;

	protected AbstractUser(Long id, LocalDate birthDate, Gender gender, String email, String telephoneNumber,
						   String residenceAddress, String passportNumber, UUID photoUuid, String name, String login, String password,
						   Role role) {
		super(id, birthDate, gender, email, telephoneNumber, residenceAddress, passportNumber, photoUuid);
		this.name = name;
		this.login = login;
		this.password = password;
		this.role = role;
	}

	protected AbstractUser() {
		super();
	}
}
