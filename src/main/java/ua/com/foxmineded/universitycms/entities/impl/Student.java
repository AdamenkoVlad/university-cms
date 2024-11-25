package ua.com.foxmineded.universitycms.entities.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.proxy.HibernateProxy;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.com.foxmineded.universitycms.enums.Gender;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.enums.Specialization;

@Entity
@DiscriminatorValue("STUDENT")
@ToString(callSuper = true)
@Getter
@Setter
public class Student extends User {
	@Column(name = "scholarship_amount", precision = 12, scale = 2, nullable = false)
	private BigDecimal scholarshipAmount;
	@Column(name = "currency_mark", length = 1, nullable = false)
	private String currencyMark;
	@Column(name = "admission_date", nullable = false)
	private LocalDate admissionDate;
	@Column(name = "student_specialization", length = 50, nullable = false)
	@Enumerated(EnumType.STRING)
	private Specialization specialization;
	@Column(name = "student_current_semester", nullable = false)
	private int currentSemester;
	@ToString.Exclude
	@ManyToOne(cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "group_id")
	private Group group;
	@ToString.Exclude
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(schema = "university", name = "student_courses", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
	private List<Course> courses;

	public Student(Long id, LocalDate birthDate, Gender gender, String email, String telephoneNumber,
			String residenceAddress, String passportNumber, UUID photoUuid, String name, String login, String password,
			Role role, BigDecimal scholarshipAmount, String currencyMark, LocalDate admissionDate,
			Specialization specialization, int currentSemester, Group group, List<Course> courses) {
		super(id, birthDate, gender, email, telephoneNumber, residenceAddress, passportNumber, photoUuid, name, login,
				password, role);
		this.scholarshipAmount = scholarshipAmount;
		this.currencyMark = currencyMark;
		this.admissionDate = admissionDate;
		this.specialization = specialization;
		this.currentSemester = currentSemester;
		this.group = group;
		this.courses = courses;
	}

	public Student() {
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
		Student student = (Student) obj;
		return getId() != null && passportNumber == student.passportNumber && currentSemester == student.currentSemester
				&& Objects.equals(id, student.id) && Objects.equals(birthDate, student.birthDate)
				&& Objects.equals(email, student.email) && Objects.equals(telephoneNumber, student.telephoneNumber)
				&& Objects.equals(residenceAddress, student.residenceAddress) && Objects.equals(name, student.name)
				&& Objects.equals(login, student.login) && Objects.equals(password, student.password)
				&& role == student.role && Objects.equals(scholarshipAmount, student.scholarshipAmount)
				&& Objects.equals(currencyMark, student.currencyMark)
				&& Objects.equals(admissionDate, student.admissionDate) && specialization == student.specialization;
	}

	public List<Course> getCourses() {
		if (Objects.isNull(courses)) {
			courses = new ArrayList<>();
		}
		return courses;
	}
}
