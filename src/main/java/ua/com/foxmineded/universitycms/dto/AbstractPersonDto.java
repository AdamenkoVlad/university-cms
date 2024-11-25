package ua.com.foxmineded.universitycms.dto;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.com.foxmineded.universitycms.enums.Gender;

@ToString(callSuper = true)
@Getter
@Setter
public abstract class AbstractPersonDto extends AbstractDto<Long> {
	private static final long serialVersionUID = 1L;
	@Past(message = "The birth date must be in the past")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	protected LocalDate birthDate;
	protected Gender gender;
	@Email(message = "Email is incorrect")
	protected String email;
	@Pattern(regexp = "^\\d+$", message = "The telephone number must contain only digits")
	protected String telephoneNumber;
	@Size(min = 0, max = 100, message = "The residence address is too long")
	protected String residenceAddress;
	@Pattern(regexp = "^\\d{8}$", message = "Passport number should have exactly 8 digits")
	protected String passportNumber;
	protected UUID photoUuid;

	protected AbstractPersonDto(Long id, LocalDate birthDate, Gender gender, String email, String telephoneNumber,
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

	protected AbstractPersonDto() {
		super();
	}
}
