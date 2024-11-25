package ua.com.foxmineded.universitycms.exceptions;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ServiceDataIntegrityException extends AppException {
	private static final long serialVersionUID = 1L;
	@EqualsAndHashCode.Include
	private String field;
	@EqualsAndHashCode.Include
	private String message;
	private Set<ServiceDataIntegrityException> exceptions = new HashSet<>();

	public ServiceDataIntegrityException(String field, String message) {
		this.field = field;
		this.message = message;
	}
}
