package ua.com.foxmineded.universitycms.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@ToString
@Getter
@Setter
public abstract class AbstractEntity<T> implements Entity<T> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected T id;

	protected AbstractEntity(T id) {
		this.id = id;
	}

	protected AbstractEntity() {
	}
}
