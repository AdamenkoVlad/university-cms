package ua.com.foxmineded.universitycms.dto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.com.foxmineded.universitycms.enums.Gender;
import ua.com.foxmineded.universitycms.enums.Role;

@ToString(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
public class UserDto extends AbstractPersonDto implements UserDetails {
	private static final long serialVersionUID = 1L;
	@Pattern(regexp = "^[a-zA-Z]+ [a-zA-Z]+$", message = "The name is incorrect")
	protected String name;
	@Size(min = 0, max = 50, message = "The login is too long")
	protected String login;
	protected String password;
	protected Role role;

	protected UserDto(Long id, LocalDate birthDate, Gender gender, String email, String telephoneNumber,
			String residenceAddress, String passportNumber, UUID photoUuid, String name, String login, String password,
			Role role) {
		super(id, birthDate, gender, email, telephoneNumber, residenceAddress, passportNumber, photoUuid);
		this.name = name;
		this.login = login;
		this.password = password;
		this.role = role;
	}

	protected UserDto() {
		super();
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return login;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
