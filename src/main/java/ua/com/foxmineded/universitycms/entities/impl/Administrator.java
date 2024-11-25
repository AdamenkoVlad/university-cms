package ua.com.foxmineded.universitycms.entities.impl;

import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.proxy.HibernateProxy;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.com.foxmineded.universitycms.entities.AbstractEmployee;
import ua.com.foxmineded.universitycms.enums.Gender;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.enums.WorkingShift;

@Entity
@DiscriminatorValue("ADMINISTRATOR")
@ToString(callSuper = true)
@Getter
@Setter
public class Administrator extends AbstractEmployee {
	public Administrator(Long id, LocalDate birthDate, Gender gender, String email, String telephoneNumber,
			String residenceAddress, String passportNumber, UUID photoUuid, String name, String login, String password,
			Role role, String salaryAmount, String currencyMark, LocalDate employmentDate, String position,
			WorkingShift workingShift) {
		super(id, birthDate, gender, email, telephoneNumber, residenceAddress, passportNumber, photoUuid, name, login,
				password, role, salaryAmount, currencyMark, employmentDate, position, workingShift);
	}

	public Administrator() {
		super();
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy
				? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
				: this.getClass().hashCode();
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Class<?> objEffectiveClass = obj instanceof HibernateProxy
				? ((HibernateProxy) obj).getHibernateLazyInitializer().getPersistentClass().getClass()
				: obj.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy
				? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().getClass()
				: this.getClass();
		if (objEffectiveClass != thisEffectiveClass)
			return false;
		Administrator administrator = (Administrator) obj;
		return getId() != null && id.equals(administrator.id) && birthDate.equals(administrator.birthDate)
				&& email.equals(administrator.email) && telephoneNumber.equals(administrator.telephoneNumber)
				&& residenceAddress.equals(administrator.residenceAddress)
				&& passportNumber == administrator.passportNumber && name.equals(administrator.name)
				&& login.equals(administrator.login) && password.equals(administrator.password)
				&& role == administrator.role && salaryAmount.equals(administrator.salaryAmount)
				&& currencyMark.equals(administrator.currencyMark)
				&& employmentDate.equals(administrator.employmentDate) && position.equals(administrator.position)
				&& workingShift == administrator.workingShift;
	}
}
