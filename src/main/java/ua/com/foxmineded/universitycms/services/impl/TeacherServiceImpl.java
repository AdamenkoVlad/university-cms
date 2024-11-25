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
import ua.com.foxmineded.universitycms.dto.TeacherDto;
import ua.com.foxmineded.universitycms.entities.impl.Teacher;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.TeacherService;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService{
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final TeacherRepository teacherRepository;
	private final AdministratorRepository administratorRepository;
	private final StudentRepository studentRepository;

	@Override
	public Page<TeacherDto> findAll(Pageable pageable) {
		return teacherRepository.findAll(pageable).map(v -> modelMapper.map(v, TeacherDto.class));
	}

	@Override
	public Page<TeacherDto> findAllByName(String name, Pageable pageable) {
		return teacherRepository.findAllByName(name, pageable).map(v -> modelMapper.map(v, TeacherDto.class));
	}

	@Override
	public TeacherDto findById(Long teacherId) throws ServiceException {
		return teacherRepository.findById(teacherId).map(teacher -> modelMapper.map(teacher, TeacherDto.class))
				.orElseThrow(() -> {
					String message = "The teacher with id = %d was not found".formatted(teacherId);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public TeacherDto findByLogin(String login) throws ServiceException {
		return teacherRepository.findByLogin(login).map(teacher -> modelMapper.map(teacher, TeacherDto.class))
				.orElseThrow(() -> {
					String message = "The teacher with login = %s was not found".formatted(login);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public TeacherDto findByPassportNumber(String passportNumber) throws ServiceException {
		return teacherRepository.findByPassportNumber(passportNumber)
				.map(teacher -> modelMapper.map(teacher, TeacherDto.class)).orElseThrow(() -> {
					String message = "The teacher with passport number = %s was not found".formatted(passportNumber);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public TeacherDto findByTelephoneNumber(String telephoneNumber) throws ServiceException {
		return teacherRepository.findByTelephoneNumber(telephoneNumber)
				.map(teacher -> modelMapper.map(teacher, TeacherDto.class)).orElseThrow(() -> {
					String message = "The teacher with telephone number = %s was not found".formatted(telephoneNumber);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public TeacherDto findByCourseName(String courseName) throws ServiceException {
		return teacherRepository.findByCourseName(courseName).map(teacher -> modelMapper.map(teacher, TeacherDto.class))
				.orElseThrow(() -> {
					String message = "The teacher with course name = %s was not found".formatted(courseName);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public TeacherDto findByCourseId(Long courseId) throws ServiceException {
		return teacherRepository.findByCourseId(courseId).map(teacher -> modelMapper.map(teacher, TeacherDto.class))
				.orElseThrow(() -> {
					String message = "The teacher with course id = %d was not found".formatted(courseId);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public TeacherDto findByLessonId(Long lessonId) throws ServiceException {
		return teacherRepository.findByLessonId(lessonId).map(teacher -> modelMapper.map(teacher, TeacherDto.class))
				.orElseThrow(() -> {
					String message = "The teacher with lesson Id = %d was not found".formatted(lessonId);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public TeacherDto save(@Valid TeacherDto teacherDto) throws ServiceDataIntegrityException {
		ServiceDataIntegrityException serviceDataIntegrityException = new ServiceDataIntegrityException();
		if (administratorRepository.findByLogin(teacherDto.getLogin()).isPresent()
				|| studentRepository.findByLogin(teacherDto.getLogin()).isPresent()) {
			String message = "A user with this login already exists";
			log.error(message);
			serviceDataIntegrityException.getExceptions().add(new ServiceDataIntegrityException("login", message));
		} else {
			teacherRepository.findByLogin(teacherDto.getLogin()).ifPresent(value -> {
				if (!Objects.equals(teacherDto.getId(), value.getId())) {
					String message = "A user with this login already exists";
					log.error(message);
					serviceDataIntegrityException.getExceptions()
							.add(new ServiceDataIntegrityException("login", message));
				}
			});
		}
		if (administratorRepository.findByEmail(teacherDto.getEmail()).isPresent()
				|| studentRepository.findByEmail(teacherDto.getEmail()).isPresent()) {
			String message = "A user with this email already exists";
			log.error(message);
			serviceDataIntegrityException.getExceptions().add(new ServiceDataIntegrityException("email", message));
		} else {
			teacherRepository.findByEmail(teacherDto.getEmail()).ifPresent(value -> {
				if (!Objects.equals(teacherDto.getId(), value.getId())) {
					String message = "A user with this email already exists";
					log.error(message);
					serviceDataIntegrityException.getExceptions()
							.add(new ServiceDataIntegrityException("email", message));
				}
			});
		}
		if (administratorRepository.findByTelephoneNumber(teacherDto.getTelephoneNumber()).isPresent()
				|| studentRepository.findByTelephoneNumber(teacherDto.getTelephoneNumber()).isPresent()) {
			String message = "A user with this telephone number already exists";
			log.error(message);
			serviceDataIntegrityException.getExceptions()
					.add(new ServiceDataIntegrityException("telephoneNumber", message));
		} else {
			teacherRepository.findByTelephoneNumber(teacherDto.getTelephoneNumber()).ifPresent(value -> {
				if (!Objects.equals(teacherDto.getId(), value.getId())) {
					String message = "A user with this telephone number already exists";
					log.error(message);
					serviceDataIntegrityException.getExceptions()
							.add(new ServiceDataIntegrityException("telephoneNumber", message));
				}
			});
		}
		if (administratorRepository.findByPassportNumber(teacherDto.getPassportNumber()).isPresent()
				|| studentRepository.findByPassportNumber(teacherDto.getPassportNumber()).isPresent()) {
			String message = "A user with this passport number already exists";
			log.error(message);
			serviceDataIntegrityException.getExceptions()
					.add(new ServiceDataIntegrityException("passportNumber", message));
		} else {
			teacherRepository.findByPassportNumber(teacherDto.getPassportNumber()).ifPresent(value -> {
				if (!Objects.equals(teacherDto.getId(), value.getId())) {
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
		if (Objects.isNull(teacherDto.getId())) {
			teacherDto.setPassword(passwordEncoder.encode(teacherDto.getPassword()));
		}
		TeacherDto result = modelMapper.map(teacherRepository.save(modelMapper.map(teacherDto, Teacher.class)),
				TeacherDto.class);
		String message = "The teacher with id = %d was saved".formatted(result.getId());
		log.info(message);
		return result;
	}

	@Override
	public void deleteById(Long teacherId) throws ServiceException {
		teacherRepository.findById(teacherId).orElseThrow(() -> {
			String message = "The teacher with id = %d was not found".formatted(teacherId);
			log.error(message);
			return new ServiceException(message);
		});
		teacherRepository.deleteById(teacherId);
		String message = "The teacher with id = %d was deleted".formatted(teacherId);
		log.info(message);
	}
}
