package ua.com.foxmineded.universitycms.entities.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.hibernate.proxy.HibernateProxy;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.com.foxmineded.universitycms.entities.AbstractEntity;
import ua.com.foxmineded.universitycms.enums.Specialization;

@Entity
@Table(schema = "university", name = "courses")
@AttributeOverride(name = "id", column = @Column(name = "course_id"))
@ToString(callSuper = true)
@Getter
@Setter
public class Course extends AbstractEntity<Long> {
	@Column(name = "specialization", length = 50, nullable = false)
	@Enumerated(EnumType.STRING)
	private Specialization specialization;
	@Column(name = "course_name", length = 100, nullable = false, unique = true)
	private String courseName;
	@Column(name = "credit_hours", nullable = false)
	private int creditHours;
	@Column(name = "course_description", unique = true)
	private String courseDescription;
	@ToString.Exclude
	@ManyToMany(mappedBy = "courses", cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	private List<Student> students;
	@ToString.Exclude
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;
	@ToString.Exclude
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, mappedBy = "course")
	private List<Lesson> lessons;

	public Course(Long id, Specialization specialization, String courseName, int creditHours, String courseDescription,
			List<Student> students, Teacher teacher, List<Lesson> lessons) {
		super(id);
		this.specialization = specialization;
		this.courseName = courseName;
		this.creditHours = creditHours;
		this.courseDescription = courseDescription;
		this.students = students;
		this.teacher = teacher;
		this.lessons = lessons;
	}

	public Course() {
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
		Course course = (Course) obj;
		return getId() != null && Double.compare(course.creditHours, creditHours) == 0 && Objects.equals(id, course.id)
				&& Objects.equals(specialization, course.specialization)
				&& Objects.equals(courseName, course.courseName)
				&& Objects.equals(courseDescription, course.courseDescription);
	}

	public List<Student> getStudents() {
		if (Objects.isNull(students)) {
			students = new ArrayList<>();
		}
		return students;
	}
}
