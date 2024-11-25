package ua.com.foxmineded.universitycms.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import ua.com.foxmineded.universitycms.dto.AdministratorDto;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;

public interface AdministratorService {
	@Secured("ROLE_ADMINISTRATOR")
	@Transactional(readOnly = true)
	Page<AdministratorDto> findAll(Pageable pageable);

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional(readOnly = true)
	AdministratorDto findById(Long administratorId) throws ServiceException;

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional(readOnly = true)
	AdministratorDto findByLogin(String login) throws ServiceException;

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional(readOnly = true)
	AdministratorDto findByPassportNumber(String passportNumber) throws ServiceException;

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional(readOnly = true)
	Page<AdministratorDto> findAllByName(String name, Pageable pageable);

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional(readOnly = true)
	AdministratorDto findByTelephoneNumber(String telephoneNumber) throws ServiceException;

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional
	AdministratorDto save(@Valid AdministratorDto administratorDto) throws ServiceDataIntegrityException;

	@Secured("ROLE_ADMINISTRATOR")
	@Transactional
	void deleteById(Long administratorId) throws ServiceException;
}
