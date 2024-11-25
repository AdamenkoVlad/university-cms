package ua.com.foxmineded.universitycms.dto;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.com.foxmineded.universitycms.enums.Gender;
import ua.com.foxmineded.universitycms.enums.Role;

@ToString(callSuper = true)
@Getter
@Setter
public abstract class AbstractUserDto extends AbstractPersonDto {
    private static final long serialVersionUID = 1L;
    @Pattern(regexp = "^[a-zA-Z]+ [a-zA-Z]+$", message = "The name is incorrect")
    protected String name;
    @Size(min = 0, max = 50, message = "The login is too long")
    protected String login;
    protected String password;
    protected Role role;

    protected AbstractUserDto(Long id, LocalDate birthDate, Gender gender, String email, String telephoneNumber,
							  String residenceAddress, String passportNumber, UUID photoUuid, String name, String login, String password,
							  Role role) {
        super(id, birthDate, gender, email, telephoneNumber, residenceAddress, passportNumber, photoUuid);
        this.name = name;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    protected AbstractUserDto() {
        super();
    }
}
