package ua.com.foxmineded.universitycms.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public abstract class AbstractEntityDto implements Dto {
	private static final long serialVersionUID = 1L;
	protected Long id;

	protected AbstractEntityDto(Long id) {
		this.id = id;
	}

	protected AbstractEntityDto() {
		super();
	}
}
