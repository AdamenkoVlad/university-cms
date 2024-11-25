package ua.com.foxmineded.universitycms.exceptions;

public class ServiceException extends AppException {
	private static final long serialVersionUID = -6682513590970301569L;

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
