package ua.com.foxmineded.universitycms.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.instancio.Instancio;
import static org.instancio.Select.field;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import ua.com.foxmineded.universitycms.config.AppConfig;
import ua.com.foxmineded.universitycms.config.SecurityConfig;
import ua.com.foxmineded.universitycms.config.TypeMapConfig;
import ua.com.foxmineded.universitycms.dao.GroupRepository;
import ua.com.foxmineded.universitycms.dao.StudentRepository;
import ua.com.foxmineded.universitycms.dto.GroupDto;
import ua.com.foxmineded.universitycms.entities.impl.Group;
import ua.com.foxmineded.universitycms.entities.impl.Student;
import ua.com.foxmineded.universitycms.enums.Specialization;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.GroupService;

@WithMockUser(roles = { "ADMINISTRATOR" })
@SpringBootTest(classes = { GroupServiceImpl.class, TypeMapConfig.class, SecurityConfig.class, AppConfig.class })
@ActiveProfiles("test")
class GroupServiceImplTest {
	@MockBean
	GroupRepository groupRepository;
	@MockBean
	StudentRepository studentRepository;
	@MockBean
	UserDetailsService userDetailsService;
	@Autowired
	GroupService groupService;
	@Autowired
	ModelMapper modelMapper;

	@Test
	void testAddStudentToGroupById_AskAddValidStudentToValideGroup_AdditionSucceed() {
		Long groupId = 10000L;
		Long studentId = 10000L;
		Group group = Instancio.of(Group.class).set(field(Group::getId), groupId)
				.set(field(Group::getSpecialization), Specialization.ART)
				.set(field(Group::getStudents),
						Instancio.ofList(Student.class).size(20)
								.generate(field(Student::getId), gen -> gen.longs().range(10001L, 10500L)).create())
				.create();
		Student student = Instancio.of(Student.class).set(field(Student::getId), studentId)
				.ignore(field(Student::getGroup)).set(field(Student::getSpecialization), Specialization.ART).create();
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		assertDoesNotThrow(() -> {
			groupService.addStudentToGroupById(groupId, studentId);
		});
	}

	@Test
	void testAddStudentToGroupById_AskAddIfInvalidGroup_ExceptionShouldArise() {
		Long groupId = 10000L;
		Long studentId = 10000L;
		String message = "The group with id = %d was not found".formatted(groupId);
		when(groupRepository.findById(groupId)).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			groupService.addStudentToGroupById(groupId, studentId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddStudentToGroupById_AskAddIfInvalidStudent_ExceptionShouldArise() {
		Long groupId = 10000L;
		Long studentId = 10000L;
		Group group = Instancio.of(Group.class).set(field(Group::getId), groupId).create();
		String message = "The student with id = %d was not found".formatted(groupId);
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			groupService.addStudentToGroupById(groupId, studentId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddStudentToGroupById_AskAddIfGroupHasMoreOrEqualThan30Students_ExceptionShouldArise() {
		Long groupId = 10000L;
		Long studentId = 10000L;
		Group group = Instancio.of(Group.class).set(field(Group::getId), groupId)
				.set(field(Group::getSpecialization), Specialization.ART)
				.set(field(Group::getStudents),
						Instancio.ofList(Student.class).size(30)
								.generate(field(Student::getId), gen -> gen.longs().range(10001L, 10500L)).create())
				.create();
		Student student = Instancio.of(Student.class).set(field(Student::getId), studentId)
				.ignore(field(Student::getGroup)).set(field(Student::getSpecialization), Specialization.ART).create();
		String message = "The addition didn't succeed, too much students in the group";
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			groupService.addStudentToGroupById(groupId, studentId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddStudentToGroupById_AskAddIfGroupWithInvalidSpecialization_ExceptionShouldArise() {
		Long groupId = 10000L;
		Long studentId = 10000L;
		Group group = Instancio.of(Group.class).set(field(Group::getId), groupId)
				.set(field(Group::getSpecialization), Specialization.COMPUTER_SCIENCE).create();
		Student student = Instancio.of(Student.class).set(field(Student::getId), studentId)
				.ignore(field(Student::getGroup)).set(field(Student::getSpecialization), Specialization.ART).create();
		String message = "The group and the student should have the same specialization";
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			groupService.addStudentToGroupById(groupId, studentId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testAddStudentToGroupById_AskAddIfStudentAlreadyInGroup_ExceptionShouldArise() {
		Long groupId = 10000L;
		Long studentId = 10000L;
		Group group = Instancio.of(Group.class).set(field(Group::getId), groupId)
				.set(field(Group::getSpecialization), Specialization.ART)
				.set(field(Group::getStudents),
						Instancio.ofList(Student.class).size(20)
								.generate(field(Student::getId), gen -> gen.longs().range(10001L, 10500L)).create())
				.create();
		Student student = Instancio.of(Student.class).set(field(Student::getId), studentId)
				.set(field(Student::getSpecialization), Specialization.ART).create();
		String message = "The student is already in a group";
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			groupService.addStudentToGroupById(groupId, studentId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testDeleteStudentFromGroupById_AskDeleteIfInvalidStudent_ExceptionShouldArise() {
		Long groupId = 10000L;
		Long studentId = 10000L;
		Group group = Instancio.of(Group.class).set(field(Group::getId), groupId).create();
		String message = "The student with id = %d was not found".formatted(groupId);
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			groupService.deleteStudentFromGroupById(groupId, studentId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testDeleteStudentFromGroupById_AskDeleteIfInvalidGroup_ExceptionShouldArise() {
		Long groupId = 10000L;
		Long studentId = 10000L;
		String message = "The group with id = %d was not found".formatted(groupId);
		when(groupRepository.findById(groupId)).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			groupService.deleteStudentFromGroupById(groupId, studentId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testDeleteStudentFromGroupById_AskDeleteIfStudentIsNotInGroup_ExceptionShouldArise() {
		Long groupId = 10000L;
		Long studentId = 10000L;
		String message = "The group with id = %d does not contain the student with id = %d".formatted(groupId,
				studentId);
		Student student = Instancio.of(Student.class).set(field(Student::getId), studentId)
				.set(field(Student::getSpecialization), Specialization.ART).create();
		Group group = Instancio.of(Group.class).set(field(Group::getId), groupId)
				.set(field(Group::getSpecialization), Specialization.ART)
				.set(field(Group::getStudents),
						Instancio.ofList(Student.class).size(20)
								.generate(field(Student::getId), gen -> gen.longs().range(10001L, 10500L))
								.set(field(Student::getSpecialization), Specialization.ART).create())
				.create();
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			groupService.deleteStudentFromGroupById(groupId, studentId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testDeleteStudentFromGroupById_AskDeleteValidStudentFromValidGroup_DeletionShouldSucceed() {
		Long groupId = 10000L;
		Long studentId = 10000L;
		Student student = Instancio.of(Student.class).set(field(Student::getId), studentId)
				.set(field(Student::getSpecialization), Specialization.ART).create();
		Group group = Instancio.of(Group.class).set(field(Group::getId), groupId)
				.set(field(Group::getSpecialization), Specialization.ART)
				.set(field(Group::getStudents),
						Instancio.ofList(Student.class).size(20)
								.generate(field(Student::getId), gen -> gen.longs().range(10001L, 10500L))
								.set(field(Student::getSpecialization), Specialization.ART).create())
				.create();
		group.getStudents().add(student);
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		assertDoesNotThrow(() -> {
			groupService.deleteStudentFromGroupById(groupId, studentId);
		});
	}

	@Test
	void testDeleteById_AskDeleteByIdIfIdInvalid_ExceptionShouldArise() {
		Long groupId = 10001L;
		String message = "The group with id = %d was not found".formatted(groupId);
		when(groupRepository.findById(groupId)).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			groupService.deleteById(groupId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@Test
	void testDeleteById_AskDeleteById_DeletionShouldSucceed() {
		Long groupId = 10001L;
		Group group = Instancio.of(Group.class).set(field(Group::getId), groupId).create();
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		assertDoesNotThrow(() -> {
			groupService.deleteById(groupId);
		});
	}

	@WithMockUser(roles = { "STUDENT" })
	@Test
	void testDeleteById_AskDeleteByIdIfRoleInvalid_ExceptionShouldArise() {
		Long groupId = 10001L;
		assertThrows(AccessDeniedException.class, () -> {
			groupService.deleteById(groupId);
		});
	}

	@Test
	void testSave_AskSaveGroupIfGroupNameAlreadyExists_ExceptionShouldBeThrown() {
		Group group = Instancio.create(Group.class);
		GroupDto groupDto = Instancio.of(GroupDto.class).set(field(GroupDto::getId), group.getId()).create();
		String message = "The group %s already exists".formatted(groupDto.getGroupName());
		when(groupRepository.findByGroupName(groupDto.getGroupName())).thenReturn(Optional.of(group));
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			groupService.save(groupDto);
		});
		assertEquals(message,
				((ServiceDataIntegrityException) throwable).getExceptions().stream().toList().get(0).getMessage());
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().size());
	}
}
