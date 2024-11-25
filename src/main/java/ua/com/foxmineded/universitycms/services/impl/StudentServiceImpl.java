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
import ua.com.foxmineded.universitycms.dto.StudentDto;
import ua.com.foxmineded.universitycms.entities.impl.Student;
import ua.com.foxmineded.universitycms.enums.Specialization;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.StudentService;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final StudentRepository studentRepository;
	private final AdministratorRepository administratorRepository;
	private final TeacherRepository teacherRepository;



	@Override
	public StudentDto findByPassportNumber(String passportNumber) throws ServiceException {
		return studentRepository.findByPassportNumber(passportNumber)
				.map(student -> modelMapper.map(student, StudentDto.class)).orElseThrow(() -> {
					String message = "The student with passport number = %s was not found".formatted(passportNumber);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public StudentDto findByLogin(String login) throws ServiceException {
		return studentRepository.findByLogin(login).map(student -> modelMapper.map(student, StudentDto.class))
				.orElseThrow(() -> {
					String message = "The student with login = %s was not found".formatted(login);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public StudentDto findByEmail(String email) throws ServiceException {
		return studentRepository.findByEmail(email).map(student -> modelMapper.map(student, StudentDto.class))
				.orElseThrow(() -> {
					String message = "The student with email = %s was not found".formatted(email);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public StudentDto findByTelephoneNumber(String telephoneNumber) throws ServiceException {
		return studentRepository.findByTelephoneNumber(telephoneNumber)
				.map(student -> modelMapper.map(student, StudentDto.class)).orElseThrow(() -> {
					String message = "The student with telephone number = %s was not found".formatted(telephoneNumber);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public StudentDto findById(Long studentId) throws ServiceException {
		return studentRepository.findById(studentId).map(student -> modelMapper.map(student, StudentDto.class))
				.orElseThrow(() -> {
					String message = "The student with id = %d was not found".formatted(studentId);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public Page<StudentDto> findAll(Pageable pageable) {
		return studentRepository.findAll(pageable).map(v -> modelMapper.map(v, StudentDto.class));
	}

	@Override
	public Page<StudentDto> findAllByName(String name, Pageable pageable) {
		return studentRepository.findAllByName(name, pageable).map(v -> modelMapper.map(v, StudentDto.class));
	}

	@Override
	public Page<StudentDto> findAllByGroupName(String groupName, Pageable pageable) {
		return studentRepository.findAllByGroupName(groupName, pageable).map(v -> modelMapper.map(v, StudentDto.class));
	}

	@Override
	public Page<StudentDto> findAllByCourseName(String courseName, Pageable pageable) {
		return studentRepository.findAllByCourseName(courseName, pageable)
				.map(v -> modelMapper.map(v, StudentDto.class));
	}

	@Override
	public Page<StudentDto> findAllByTeacherName(String teacherName, Pageable pageable) {
		return studentRepository.findAllByTeacherName(teacherName, pageable)
				.map(v -> modelMapper.map(v, StudentDto.class));
	}

	@Override
	public Page<StudentDto> findAllBySpecialization(String specialization, Pageable pageable) throws ServiceException {
		try {
			return studentRepository
					.findAllBySpecialization(Specialization.valueOf(specialization.toUpperCase()), pageable)
					.map(v -> modelMapper.map(v, StudentDto.class));
		} catch (IllegalArgumentException e) {
			String message = "Invalid specialization %s".formatted(specialization);
			log.error(message);
			throw new ServiceException(message);
		}
	}

	@Override
	public StudentDto save(@Valid StudentDto studentDto) throws ServiceDataIntegrityException {
		ServiceDataIntegrityException serviceDataIntegrityException = new ServiceDataIntegrityException();
		if (administratorRepository.findByLogin(studentDto.getLogin()).isPresent()
				|| teacherRepository.findByLogin(studentDto.getLogin()).isPresent()) {
			String message = "A user with this login already exists";
			log.error(message);
			serviceDataIntegrityException.getExceptions().add(new ServiceDataIntegrityException("login", message));
		} else {
			studentRepository.findByLogin(studentDto.getLogin()).ifPresent(value -> {
				if (!Objects.equals(studentDto.getId(), value.getId())) {
					String message = "A user with this login already exists";
					log.error(message);
					serviceDataIntegrityException.getExceptions()
							.add(new ServiceDataIntegrityException("login", message));
				}
			});
		}
		if (administratorRepository.findByEmail(studentDto.getEmail()).isPresent()
				|| teacherRepository.findByEmail(studentDto.getEmail()).isPresent()) {
			String message = "A user with this email already exists";
			log.error(message);
			serviceDataIntegrityException.getExceptions().add(new ServiceDataIntegrityException("email", message));
		} else {
			studentRepository.findByEmail(studentDto.getEmail()).ifPresent(value -> {
				if (!Objects.equals(studentDto.getId(), value.getId())) {
					String message = "A user with this email already exists";
					log.error(message);
					serviceDataIntegrityException.getExceptions()
							.add(new ServiceDataIntegrityException("email", message));
				}
			});
		}
		if (administratorRepository.findByTelephoneNumber(studentDto.getTelephoneNumber()).isPresent()
				|| teacherRepository.findByTelephoneNumber(studentDto.getTelephoneNumber()).isPresent()) {
			String message = "A user with this telephone number already exists";
			log.error(message);
			serviceDataIntegrityException.getExceptions()
					.add(new ServiceDataIntegrityException("telephoneNumber", message));
		} else {
			studentRepository.findByTelephoneNumber(studentDto.getTelephoneNumber()).ifPresent(value -> {
				if (!Objects.equals(studentDto.getId(), value.getId())) {
					String message = "A user with this telephone number already exists";
					log.error(message);
					serviceDataIntegrityException.getExceptions()
							.add(new ServiceDataIntegrityException("telephoneNumber", message));
				}
			});
		}
		if (administratorRepository.findByPassportNumber(studentDto.getPassportNumber()).isPresent()
				|| teacherRepository.findByPassportNumber(studentDto.getPassportNumber()).isPresent()) {
			String message = "A user with this passport number already exists";
			log.error(message);
			serviceDataIntegrityException.getExceptions()
					.add(new ServiceDataIntegrityException("passportNumber", message));
		} else {
			studentRepository.findByPassportNumber(studentDto.getPassportNumber()).ifPresent(value -> {
				if (!Objects.equals(studentDto.getId(), value.getId())) {
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
		if (Objects.isNull(studentDto.getId())) {
			studentDto.setPassword(passwordEncoder.encode(studentDto.getPassword()));
		}
		StudentDto result = modelMapper.map(studentRepository.save(modelMapper.map(studentDto, Student.class)),
				StudentDto.class);
		String message = "The student with id = %d was saved".formatted(result.getId());
		log.info(message);
		return result;
	}

	@Override
	public void deleteById(Long studentId) throws ServiceException {
		studentRepository.findById(studentId).orElseThrow(() -> {
			String message = "The student with id = %d was not found".formatted(studentId);
			log.error(message);
			return new ServiceException(message);
		});
		studentRepository.deleteById(studentId);
		String message = "The student with id = %d was deleted".formatted(studentId);
		log.info(message);
	}
}
