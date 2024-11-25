package ua.com.foxmineded.universitycms.entities.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.hibernate.proxy.HibernateProxy;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.com.foxmineded.universitycms.entities.AbstractEmployee;
import ua.com.foxmineded.universitycms.enums.Gender;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.enums.WorkingShift;

@Entity
@DiscriminatorValue("TEACHER")
@ToString(callSuper = true)
@Getter
@Setter
public class Teacher extends AbstractEmployee {
	@Column(name = "teacher_scientific_degree", nullable = false)
	private String scientificDegree;
	@ToString.Exclude
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, mappedBy = "teacher")
	private List<Course> courses;

	public Teacher(Long id, LocalDate birthDate, Gender gender, String email, String telephoneNumber,
			String residenceAddress, String passportNumber, UUID photoUuid, String name, String login, String password,
			Role role, String salaryAmount, String currencyMark, LocalDate employmentDate, String position,
			WorkingShift workingShift, String scientificDegree, List<Course> courses) {
		super(id, birthDate, gender, email, telephoneNumber, residenceAddress, passportNumber, photoUuid, name, login,
				password, role, salaryAmount, currencyMark, employmentDate, position, workingShift);
		this.scientificDegree = scientificDegree;
		this.courses = courses;
	}

	public Teacher() {
		super();
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy
				? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
				: getClass().hashCode();
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Class<?> objEffectiveClass = obj instanceof HibernateProxy
				? ((HibernateProxy) obj).getHibernateLazyInitializer().getPersistentClass()
				: obj.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy
				? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
				: this.getClass();
		if (objEffectiveClass != thisEffectiveClass)
			return false;
		Teacher teacher = (Teacher) obj;
		return getId() != null && id.equals(teacher.id) && birthDate.equals(teacher.birthDate)
				&& email.equals(teacher.email) && telephoneNumber.equals(teacher.telephoneNumber)
				&& residenceAddress.equals(teacher.residenceAddress) && passportNumber == teacher.passportNumber
				&& name.equals(teacher.name) && login.equals(teacher.login) && password.equals(teacher.password)
				&& role == teacher.role && salaryAmount.equals(teacher.salaryAmount)
				&& currencyMark.equals(teacher.currencyMark) && employmentDate.equals(teacher.employmentDate)
				&& position.equals(teacher.position) && workingShift == teacher.workingShift
				&& scientificDegree.equals(teacher.scientificDegree);
	}
}
