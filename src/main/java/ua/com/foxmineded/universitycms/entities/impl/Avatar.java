package ua.com.foxmineded.universitycms.entities.impl;

import java.util.Objects;
import java.util.UUID;
import org.hibernate.proxy.HibernateProxy;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.com.foxmineded.universitycms.entities.AbstractEntity;
import ua.com.foxmineded.universitycms.enums.Role;

@Entity
@ToString(callSuper = true)
@Table(name = "avatars", schema = "university")
@Getter
@Setter
public class Avatar extends AbstractEntity<UUID> {
	@Column(name = "avatar_contents")
	private byte[] avatarContents;
	@Column(name = "file_name")
	private String fileName;
	@Column(name = "content_type")
	private String contentType;
	@Enumerated(EnumType.STRING)
	@Column(name = "user_role")
	private Role role;

	public Avatar(UUID id, byte[] avatarContents, String fileName, String contentType, Role role) {
		super(id);
		this.avatarContents = avatarContents;
		this.fileName = fileName;
		this.contentType = contentType;
		this.role = role;
	}

	public Avatar() {
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
				? ((HibernateProxy) obj).getHibernateLazyInitializer().getPersistentClass()
				: obj.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy
				? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
				: this.getClass();
		if (objEffectiveClass != thisEffectiveClass)
			return false;
		Avatar avatar = (Avatar) obj;
		return getId() != null && Objects.equals(getId(), avatar.getId()) && Objects.equals(fileName, avatar.fileName)
				&& Objects.equals(contentType, avatar.contentType)
				&& Objects.equals(avatarContents, avatar.avatarContents) && Objects.equals(role, avatar.role);
	}
}
