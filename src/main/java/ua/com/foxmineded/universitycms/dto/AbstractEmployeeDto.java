package ua.com.foxmineded.universitycms.dto;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.com.foxmineded.universitycms.enums.Gender;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.enums.WorkingShift;

@ToString(callSuper = true)
@Getter
@Setter
public abstract class AbstractEmployeeDto extends UserDto {
	private static final long serialVersionUID = 1L;
	@Pattern(regexp = "^\\d+(\\.\\d+)?$", message = "Invalid salary")
	protected String salaryAmount;
	@Pattern(regexp = "^[A-Z]{3}$", message = "Incorrect currency name")
	protected String currencyMark;
	@PastOrPresent(message = "The employment date must be in the past or in the present")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	protected LocalDate employmentDate;
	@Size(min = 0, max = 50, message = "The position name is too long")
	protected String position;
	protected WorkingShift workingShift;

	protected AbstractEmployeeDto(Long id, LocalDate birthDate, Gender gender, String email, String telephoneNumber,
			String residenceAddress, String passportNumber, UUID photoUuid, String name, String login, String password,
			Role role, String salaryAmount, String currencyMark, LocalDate employmentDate, String position,
			WorkingShift workingShift) {
		super(id, birthDate, gender, email, telephoneNumber, residenceAddress, passportNumber, photoUuid, name, login,
				password, role);
		this.salaryAmount = salaryAmount;
		this.currencyMark = currencyMark;
		this.employmentDate = employmentDate;
		this.position = position;
		this.workingShift = workingShift;
	}

	protected AbstractEmployeeDto() {
	}
}
