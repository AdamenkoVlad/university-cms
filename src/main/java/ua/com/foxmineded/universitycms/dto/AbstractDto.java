package ua.com.foxmineded.universitycms.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public abstract class AbstractDto<T> implements Dto {
	private static final long serialVersionUID = 1L;
	protected T id;

	protected AbstractDto(T id) {
		this.id = id;
	}

	protected AbstractDto() {
		super();
	}
}
