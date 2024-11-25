package ua.com.foxmineded.universitycms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.com.foxmineded.universitycms.enums.Gender;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.enums.Specialization;

@Data
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StudentDto extends UserDto {
	private static final long serialVersionUID = 1L;
	@PositiveOrZero(message = "The scholarship must be positive or zero")
	private BigDecimal scholarshipAmount;
	@Pattern(regexp = "^[A-Z]{3}$", message = "Incorrect currency name")
	private String currencyMark;
	@PastOrPresent(message = "The admission date must be present or in the past")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate admissionDate;
	private Specialization specialization;
	@Positive(message = "The semester number must be positive")
	private Integer currentSemester;
	private Long groupId;

	@Builder(setterPrefix = "with")
	public StudentDto(Long id, LocalDate birthDate, Gender gender, String email, String telephoneNumber,
			String residenceAddress, String passportNumber, UUID photoUuid, String name, String login, String password,
			Role role, BigDecimal scholarshipAmount, String currencyMark, LocalDate admissionDate,
			Specialization specialization, Integer currentSemester, Long groupId) {
		super(id, birthDate, gender, email, telephoneNumber, residenceAddress, passportNumber, photoUuid, name, login,
				password, role);
		this.scholarshipAmount = scholarshipAmount;
		this.currencyMark = currencyMark;
		this.admissionDate = admissionDate;
		this.specialization = specialization;
		this.currentSemester = currentSemester;
		this.groupId = groupId;
	}

	public StudentDto() {
	}
}
