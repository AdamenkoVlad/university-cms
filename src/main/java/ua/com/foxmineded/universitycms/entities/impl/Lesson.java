package ua.com.foxmineded.universitycms.entities.impl;

import java.time.LocalDate;
import java.util.Objects;
import org.hibernate.proxy.HibernateProxy;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.com.foxmineded.universitycms.entities.AbstractEntity;

@Entity
@Table(schema = "university", name = "lessons")
@AttributeOverride(name = "id", column = @Column(name = "lesson_id"))
@ToString(callSuper = true)
@Getter
@Setter
public class Lesson extends AbstractEntity<Long> {
	@Column(name = "lesson_date", nullable = false)
	private LocalDate lessonDate;
	@Column(name = "lesson_number", nullable = false)
	private Integer lessonNumber;
	@ToString.Exclude
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "room_id")
	private Room room;
	@ToString.Exclude
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "course_id")
	private Course course;

	public Lesson(Long id, LocalDate lessonDate, Integer lessonNumber, Room room, Course course) {
		super(id);
		this.lessonDate = lessonDate;
		this.lessonNumber = lessonNumber;
		this.room = room;
		this.course = course;
	}

	public Lesson() {
		super();
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy
				? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
				: this.hashCode();
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
		Lesson lesson = (Lesson) obj;
		return getId() != null && lessonNumber == lesson.lessonNumber && Objects.equals(id, lesson.id)
				&& Objects.equals(lessonDate, lesson.lessonDate);
	}
}
