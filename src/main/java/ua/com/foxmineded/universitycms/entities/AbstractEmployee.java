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
import ua.com.foxmineded.universitycms.entities.impl.User;
import ua.com.foxmineded.universitycms.enums.Gender;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.enums.WorkingShift;

@MappedSuperclass
@ToString(callSuper = true)
@Getter
@Setter
public class AbstractEmployee extends User {
	@Column(name = "salary_amount", nullable = false)
	protected String salaryAmount;
	@Column(name = "currency_mark", nullable = false)
	protected String currencyMark;
	@Column(name = "employment_date", nullable = false)
	protected LocalDate employmentDate;
	@Column(name = "employee_position", length = 50, nullable = false)
	protected String position;
	@Column(name = "employee_working_shift", length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	protected WorkingShift workingShift;

	protected AbstractEmployee(Long id, LocalDate birthDate, Gender gender, String email, String telephoneNumber,
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

	protected AbstractEmployee() {
		super();
	}
}
