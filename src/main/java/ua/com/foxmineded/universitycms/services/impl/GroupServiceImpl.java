package ua.com.foxmineded.universitycms.services.impl;

import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxmineded.universitycms.dao.GroupRepository;
import ua.com.foxmineded.universitycms.dao.StudentRepository;
import ua.com.foxmineded.universitycms.dto.GroupDto;
import ua.com.foxmineded.universitycms.entities.impl.Group;
import ua.com.foxmineded.universitycms.entities.impl.Student;
import ua.com.foxmineded.universitycms.enums.Specialization;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.GroupService;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
	private static final int MAX_STUDENTS = 30;
	private static final int MIN_STUDENTS = 15;

	private final ModelMapper modelMapper;
	private final GroupRepository groupRepository;
	private final StudentRepository studentRepository;

	@Override
	public GroupDto findById(Long groupId) throws ServiceException {
		return groupRepository.findById(groupId).map(group -> modelMapper.map(group, GroupDto.class))
				.orElseThrow(() -> {
					String message = "The group with id = %d was not found".formatted(groupId);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public GroupDto findByGroupName(String groupName) throws ServiceException {
		return groupRepository.findByGroupName(groupName).map(group -> modelMapper.map(group, GroupDto.class))
				.orElseThrow(() -> {
					String message = "The group %s was not found".formatted(groupName);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public GroupDto findByStudentId(Long studentId) throws ServiceException {
		return groupRepository.findByStudentId(studentId).map(v -> modelMapper.map(v, GroupDto.class))
				.orElseThrow(() -> {
					String message = "The group for student with id = %d was not found".formatted(studentId);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public Page<GroupDto> findAll(Pageable pageable) {
		return groupRepository.findAll(pageable).map(v -> modelMapper.map(v, GroupDto.class));
	}

	@Override
	public Page<GroupDto> findAllBySpecialization(String specialization, Pageable pageable) throws ServiceException {
		try {
			return groupRepository
					.findAllBySpecialization(Specialization.valueOf(specialization.toUpperCase()), pageable)
					.map(v -> modelMapper.map(v, GroupDto.class));
		} catch (IllegalArgumentException e) {
			String message = "Invalid specialization %s".formatted(specialization);
			log.error(message);
			throw new ServiceException(message);
		}
	}

	@Override
	public Page<GroupDto> findAllWithLessOrEqualStudents(int studentAmount, Pageable pageable) {
		return groupRepository.findAllWithLessOrEqualStudents(studentAmount, pageable)
				.map(v -> modelMapper.map(v, GroupDto.class));
	}

	@Override
	public GroupDto save(@Valid GroupDto groupDto) throws ServiceDataIntegrityException {
		ServiceDataIntegrityException serviceDataIntegrityException = new ServiceDataIntegrityException();

		groupRepository.findByGroupName(groupDto.getGroupName()).ifPresent(group -> {
			if (!(Objects.equals(group.getId(), groupDto.getId())
					&& Objects.equals(group.getGroupName(), groupDto.getGroupName()))) {
				String message = "The group %s already exists".formatted(groupDto.getGroupName());
				log.error(message);
				serviceDataIntegrityException.getExceptions()
						.add(new ServiceDataIntegrityException("groupName", message));
			}
		});

		if (!serviceDataIntegrityException.getExceptions().isEmpty()) {
			throw serviceDataIntegrityException;
		}
		GroupDto result = modelMapper.map(groupRepository.save(modelMapper.map(groupDto, Group.class)), GroupDto.class);
		String message = "The group with name = %s and id = %d was saved".formatted(result.getGroupName(),
				result.getId());
		log.info(message);
		return result;
	}

	@Override
	public void addStudentToGroupById(Long groupId, Long studentId) throws ServiceException {
		Group group = groupRepository.findById(groupId).orElseThrow(() -> {
			String message = "The group with id = %d was not found".formatted(groupId);
			log.error(message);
			return new ServiceException(message);
		});
		Student student = studentRepository.findById(studentId).orElseThrow(() -> {
			String message = "The student with id = %d was not found".formatted(studentId);
			log.error(message);
			return new ServiceException(message);
		});
		if (Objects.nonNull(student.getGroup())) {
			String message = "The student is already in a group";
			log.error(message);
			throw new ServiceException(message);
		}
		if (!Objects.equals(group.getSpecialization(), student.getSpecialization())) {
			String message = "The group and the student should have the same specialization";
			log.error(message);
			throw new ServiceException(message);
		}
		if (group.getStudents().size() == MAX_STUDENTS) {
			String message = "The addition didn't succeed, too much students in the group";
			log.error(message);
			throw new ServiceException(message);
		}
		student.setGroup(group);
		String message = "The student with id = %d was added to the group with id = %d".formatted(groupId, studentId);
		log.info(message);
	}

	@Override
	public void deleteStudentFromGroupById(Long groupId, Long studentId) throws ServiceException {
		Group group = groupRepository.findById(groupId).orElseThrow(() -> {
			String message = "The group with id = %d was not found".formatted(groupId);
			log.error(message);
			return new ServiceException(message);
		});
		Student student = studentRepository.findById(studentId).orElseThrow(() -> {
			String message = "The student with id = %d was not found".formatted(studentId);
			log.error(message);
			return new ServiceException(message);
		});
		if (!group.getStudents().contains(student)) {
			String message = "The group with id = %d does not contain the student with id = %d".formatted(groupId,
					studentId);
			log.error(message);
			throw new ServiceException(message);
		}
		student.setGroup(null);
		log.info("The student with id = %d was deleted from the group with id = %d".formatted(studentId, groupId));
		if (group.getStudents().size() < MIN_STUDENTS) {
			String message = "The group with id = %d contains too few students %d".formatted(groupId,
					group.getStudents().size());
			log.warn(message);
		}
	}

	@Override
	public void deleteById(Long groupId) throws ServiceException {
		groupRepository.findById(groupId).orElseThrow(() -> {
			String message = "The group with id = %d was not found".formatted(groupId);
			log.error(message);
			return new ServiceException(message);
		});
		groupRepository.deleteById(groupId);
		String message = "The group with id = %d was deleted".formatted(groupId);
		log.info(message);
	}
}
