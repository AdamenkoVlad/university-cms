package ua.com.foxmineded.universitycms.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.com.foxmineded.universitycms.enums.Role;

@Data
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder(setterPrefix = "with")
public class AvatarDto extends AbstractDto<UUID> {
	private static final long serialVersionUID = 1L;
	private byte[] avatarContents;
	private String fileName;
	private String contentType;
	private Role role;

	@Builder(setterPrefix = "with")
	public AvatarDto(UUID id, byte[] avatarContents, String fileName, String contentType, Role role) {
		super(id);
		this.avatarContents = avatarContents;
		this.fileName = fileName;
		this.contentType = contentType;
		this.role = role;
	}

	public AvatarDto() {
	}
}
