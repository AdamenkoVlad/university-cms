package ua.com.foxmineded.universitycms.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder(setterPrefix = "with")
public class RoomDto extends AbstractDto<Long> {
	private static final long serialVersionUID = 1L;
	@Positive(message = "The room number must be positive")
	private Integer roomNumber;
	@Positive(message = "The floor number must be positive")
	private Integer floor;

	@Builder(setterPrefix = "with")
	public RoomDto(Long id, Integer roomNumber, Integer floor) {
		super(id);
		this.roomNumber = roomNumber;
		this.floor = floor;
	}

	public RoomDto() {
	}
}
