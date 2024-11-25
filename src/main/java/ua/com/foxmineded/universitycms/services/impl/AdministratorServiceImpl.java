package ua.com.foxmineded.universitycms.services.impl;

import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxmineded.universitycms.dao.AdministratorRepository;
import ua.com.foxmineded.universitycms.dao.StudentRepository;
import ua.com.foxmineded.universitycms.dao.TeacherRepository;
import ua.com.foxmineded.universitycms.dto.AdministratorDto;
import ua.com.foxmineded.universitycms.entities.impl.Administrator;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.AdministratorService;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class AdministratorServiceImpl implements AdministratorService {
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final AdministratorRepository administratorRepository;
	private final StudentRepository studentRepository;
	private final TeacherRepository teacherRepository;

	@Override
	public Page<AdministratorDto> findAll(Pageable pageable) {
		return administratorRepository.findAll(pageable).map(v -> modelMapper.map(v, AdministratorDto.class));
	}

	@Override
	public AdministratorDto findById(Long administratorId) throws ServiceException {
		return administratorRepository.findById(administratorId)
				.map(administrator -> modelMapper.map(administrator, AdministratorDto.class)).orElseThrow(() -> {
					String message = "The administrator with id = %d was not found".formatted(administratorId);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public AdministratorDto findByLogin(String login) throws ServiceException {
		return administratorRepository.findByLogin(login)
				.map(administrator -> modelMapper.map(administrator, AdministratorDto.class)).orElseThrow(() -> {
					String message = "The administrator with login = %s was not found".formatted(login);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public AdministratorDto findByPassportNumber(String passportNumber) throws ServiceException {
		return administratorRepository.findByPassportNumber(passportNumber)
				.map(administrator -> modelMapper.map(administrator, AdministratorDto.class)).orElseThrow(() -> {
					String message = "The administrator with passport number = %s was not found"
							.formatted(passportNumber);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public Page<AdministratorDto> findAllByName(String name, Pageable pageable) {
		return administratorRepository.findAllByName(name, pageable)
				.map(v -> modelMapper.map(v, AdministratorDto.class));
	}

	@Override
	public AdministratorDto findByTelephoneNumber(String telephoneNumber) throws ServiceException {
		return administratorRepository.findByTelephoneNumber(telephoneNumber)
				.map(administrator -> modelMapper.map(administrator, AdministratorDto.class)).orElseThrow(() -> {
					String message = "The administrator with telephone number = %s was not found"
							.formatted(telephoneNumber);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public AdministratorDto save(@Valid AdministratorDto administratorDto) throws ServiceDataIntegrityException {
		ServiceDataIntegrityException serviceDataIntegrityException = new ServiceDataIntegrityException();
		if (studentRepository.findByLogin(administratorDto.getLogin()).isPresent()
				|| teacherRepository.findByLogin(administratorDto.getLogin()).isPresent()) {
			String message = "A user with this login already exists";
			log.error(message);
			serviceDataIntegrityException.getExceptions().add(new ServiceDataIntegrityException("login", message));
		} else {
			administratorRepository.findByLogin(administratorDto.getLogin()).ifPresent(value -> {
				if (!Objects.equals(administratorDto.getId(), value.getId())) {
					String message = "A user with this login already exists";
					log.error(message);
					serviceDataIntegrityException.getExceptions()
							.add(new ServiceDataIntegrityException("login", message));
				}
			});
		}
		if (studentRepository.findByEmail(administratorDto.getEmail()).isPresent()
				|| teacherRepository.findByEmail(administratorDto.getEmail()).isPresent()) {
			String message = "A user with this email already exists";
			log.error(message);
			serviceDataIntegrityException.getExceptions().add(new ServiceDataIntegrityException("email", message));
		} else {
			administratorRepository.findByEmail(administratorDto.getEmail()).ifPresent(value -> {
				if (!Objects.equals(administratorDto.getId(), value.getId())) {
					String message = "A user with this email already exists";
					log.error(message);
					serviceDataIntegrityException.getExceptions()
							.add(new ServiceDataIntegrityException("email", message));
				}
			});

		}
		if (studentRepository.findByTelephoneNumber(administratorDto.getTelephoneNumber()).isPresent()
				|| teacherRepository.findByTelephoneNumber(administratorDto.getTelephoneNumber()).isPresent()) {
			String message = "A user with this telephone number already exists";
			log.error(message);
			serviceDataIntegrityException.getExceptions()
					.add(new ServiceDataIntegrityException("telephoneNumber", message));
		} else {
			administratorRepository.findByTelephoneNumber(administratorDto.getTelephoneNumber()).ifPresent(value -> {
				if (!Objects.equals(administratorDto.getId(), value.getId())) {
					String message = "A user with this telephone number already exists";
					log.error(message);
					serviceDataIntegrityException.getExceptions()
							.add(new ServiceDataIntegrityException("telephoneNumber", message));
				}
			});
		}
		if (studentRepository.findByPassportNumber(administratorDto.getPassportNumber()).isPresent()
				|| teacherRepository.findByPassportNumber(administratorDto.getPassportNumber()).isPresent()) {
			String message = "A user with this passport number already exists";
			log.error(message);
			serviceDataIntegrityException.getExceptions()
					.add(new ServiceDataIntegrityException("passportNumber", message));
		} else {
			administratorRepository.findByPassportNumber(administratorDto.getPassportNumber()).ifPresent(value -> {
				if (!(Objects.equals(administratorDto.getId(), value.getId()))) {
					String message = "A user with this passport number already exists";
					log.error(message);
					serviceDataIntegrityException.getExceptions()
							.add(new ServiceDataIntegrityException("passportNumber", message));
				}
			});
		}
		if (!serviceDataIntegrityException.getExceptions().isEmpty()) {
			throw serviceDataIntegrityException;
		}
		if (Objects.isNull(administratorDto.getId())) {
			administratorDto.setPassword(passwordEncoder.encode(administratorDto.getPassword()));
		}
		AdministratorDto result = modelMapper.map(
				administratorRepository.save(modelMapper.map(administratorDto, Administrator.class)),
				AdministratorDto.class);
		String message = "The administrator with id = %d was saved".formatted(result.getId());
		log.info(message);
		return result;
	}

	@Override
	public void deleteById(Long administratorId) throws ServiceException {
		administratorRepository.findById(administratorId).orElseThrow(() -> {
			String message = "The administrator with id = %d was not found".formatted(administratorId);
			log.error(message);
			return new ServiceException(message);
		});
		administratorRepository.deleteById(administratorId);
		String message = "The administrator with id = %d was deleted".formatted(administratorId);
		log.info(message);
	}
}
