package ua.com.foxmineded.universitycms.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.com.foxmineded.universitycms.enums.Gender;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.enums.WorkingShift;

@Data
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AdministratorDto extends AbstractEmployeeDto {
	private static final long serialVersionUID = 1L;

	@Builder(setterPrefix = "with")
	public AdministratorDto(Long id, LocalDate birthDate, Gender gender, String email, String telephoneNumber,
			String residenceAddress, String passportNumber, UUID photoUuid, String name, String login, String password,
			Role role, String salaryAmount, String currencyMark, LocalDate employmentDate, String position,
			WorkingShift workingShift) {
		super(id, birthDate, gender, email, telephoneNumber, residenceAddress, passportNumber, photoUuid, name, login,
				password, role, salaryAmount, currencyMark, employmentDate, position, workingShift);
	}
}
