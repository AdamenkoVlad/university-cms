package ua.com.foxmineded.universitycms.entities.impl;

import java.util.List;
import java.util.Objects;
import org.hibernate.proxy.HibernateProxy;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.com.foxmineded.universitycms.entities.AbstractEntity;

@Entity
@Table(schema = "university", name = "rooms")
@AttributeOverride(name = "id", column = @Column(name = "room_id"))
@ToString(callSuper = true)
@Getter
@Setter
public class Room extends AbstractEntity<Long> {
	@Column(name = "room_number", nullable = false, unique = true)
	private Integer roomNumber;
	@Column(name = "floor", nullable = false)
	private Integer floor;
	@ToString.Exclude
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, mappedBy = "room")
	private List<Lesson> lesson;

	public Room(Long id, Integer roomNumber, Integer floor, List<Lesson> lesson) {
		super(id);
		this.roomNumber = roomNumber;
		this.floor = floor;
		this.lesson = lesson;
	}

	public Room() {
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
				? ((HibernateProxy) obj).getHibernateLazyInitializer().getPersistentClass().getClass()
				: obj.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy
				? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().getClass()
				: this.getClass();
		if (objEffectiveClass != thisEffectiveClass)
			return false;
		Room room = (Room) obj;
		return getId() != null && roomNumber == room.roomNumber && floor == room.floor && Objects.equals(id, room.id);
	}
}
