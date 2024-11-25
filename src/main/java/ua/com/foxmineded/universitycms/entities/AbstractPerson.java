package ua.com.foxmineded.universitycms.entities;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.com.foxmineded.universitycms.enums.Gender;

@MappedSuperclass
@ToString(callSuper = true)
@Getter
@Setter
public abstract class AbstractPerson extends AbstractEntity<Long> {
	@Column(name = "birth_date", nullable = false)
	protected LocalDate birthDate;
	@Column(name = "gender", nullable = false)
	@Enumerated(EnumType.STRING)
	protected Gender gender;
	@Column(name = "email", nullable = false, unique = true)
	protected String email;
	@Column(name = "telephone_number", length = 30, nullable = false, unique = true)
	protected String telephoneNumber;
	@Column(name = "residence_address", length = 100, nullable = false)
	protected String residenceAddress;
	@Column(name = "passport_number", unique = true, nullable = false)
	protected String passportNumber;
	@Column(name = "photo_uuid")
	protected UUID photoUuid;

	protected AbstractPerson(Long id, LocalDate birthDate, Gender gender, String email, String telephoneNumber,
			String residenceAddress, String passportNumber, UUID photoUuid) {
		super(id);
		this.birthDate = birthDate;
		this.gender = gender;
		this.email = email;
		this.telephoneNumber = telephoneNumber;
		this.residenceAddress = residenceAddress;
		this.passportNumber = passportNumber;
		this.photoUuid = photoUuid;
	}

	protected AbstractPerson() {
		super();
	}
}
