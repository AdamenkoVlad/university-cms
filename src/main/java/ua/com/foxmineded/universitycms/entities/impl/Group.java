package ua.com.foxmineded.universitycms.entities.impl;

import java.util.List;
import java.util.Objects;
import org.hibernate.proxy.HibernateProxy;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.com.foxmineded.universitycms.entities.AbstractEntity;
import ua.com.foxmineded.universitycms.enums.Specialization;

@Entity
@Table(schema = "university", name = "groups")
@AttributeOverride(name = "id", column = @Column(name = "group_id"))
@ToString(callSuper = true)
@Getter
@Setter
public class Group extends AbstractEntity<Long> {
	@Column(name = "group_name", length = 5, nullable = false, unique = true)
	private String groupName;
	@Column(name = "specialization", length = 50, nullable = false)
	@Enumerated(EnumType.STRING)
	private Specialization specialization;
	@ToString.Exclude
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, mappedBy = "group")
	private List<Student> students;

	public Group(Long id, String groupName, Specialization specialization, List<Student> students) {
		super(id);
		this.groupName = groupName;
		this.specialization = specialization;
		this.students = students;
	}

	public Group() {
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
		Group group = (Group) obj;
		return getId() != null && Objects.equals(id, group.id) && Objects.equals(groupName, group.groupName)
				&& Objects.equals(specialization, group.specialization);
	}
}
