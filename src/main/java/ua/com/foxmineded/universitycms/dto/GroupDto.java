package ua.com.foxmineded.universitycms.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.com.foxmineded.universitycms.enums.Specialization;

@Data
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder(setterPrefix = "with")
public class GroupDto extends AbstractDto<Long> {
	private static final long serialVersionUID = 1L;
	@Pattern(regexp = "^[a-zA-Z]{2}-\\d{2}$", message = "Incorrect group name")
	private String groupName;
	private Specialization specialization;

	@Builder(setterPrefix = "with")
	public GroupDto(Long id, String groupName, Specialization specialization) {
		super(id);
		this.groupName = groupName;
		this.specialization = specialization;
	}

	public GroupDto() {
	}
}
