package ua.com.foxmineded.universitycms.services.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.instancio.Instancio;
import static org.instancio.Select.field;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import ua.com.foxmineded.universitycms.config.SecurityConfig;
import ua.com.foxmineded.universitycms.config.TypeMapConfig;
import ua.com.foxmineded.universitycms.dao.StudentRepository;
import ua.com.foxmineded.universitycms.dto.AdministratorDto;
import ua.com.foxmineded.universitycms.dto.StudentDto;
import ua.com.foxmineded.universitycms.dto.TeacherDto;
import ua.com.foxmineded.universitycms.entities.impl.Student;
import ua.com.foxmineded.universitycms.enums.Gender;
import ua.com.foxmineded.universitycms.enums.Specialization;
import ua.com.foxmineded.universitycms.enums.WorkingShift;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.AdministratorService;
import ua.com.foxmineded.universitycms.services.StudentService;
import ua.com.foxmineded.universitycms.services.TeacherService;

@WithMockUser(roles = { "ADMINISTRATOR" })
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { StudentService.class,
		AdministratorService.class, TeacherService.class, TypeMapConfig.class, SecurityConfig.class,
		UserDetailsService.class }))
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = { "/test/sql/clear_tables.sql" })
class StudentServiceImplTest {
	@SpyBean
	StudentRepository studentRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	StudentService studentService;
	@Autowired
	AdministratorService administratorService;
	@Autowired
	TeacherService teacherService;

	@Test
	void testDeleteById_AskDeleteByIdIfIdInvalid_ExceptionShouldArise() {
		Long studentId = 10000L;
		String message = "The student with id = %d was not found".formatted(studentId);
		when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			studentService.deleteById(studentId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@WithMockUser(roles = { "STUDENT" })
	@Test
	void testDeleteById_AskDeleteByIdIfRoleInvalid_ExceptionShouldArise() {
		Long studentId = 10000L;
		assertThrows(AccessDeniedException.class, () -> {
			studentService.deleteById(studentId);
		});
	}

	@Test
	void testDeleteById_AskDeleteById_DeletionShouldSucceed() {
		Long studentId = 10000L;
		Student student = Instancio.of(Student.class).set(field(Student::getId), studentId).create();
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		assertDoesNotThrow(() -> {
			studentService.deleteById(studentId);
		});
	}

	@Test
	void testSave_AskSaveStudentsWithDuplicateLogin_ExceptionShouldArise() {
		String message = "A user with this login already exists";
		String field = "login";
		String login = "admin";
		StudentDto studentDto0 = StudentDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbatovandre@gmail.com").withTelephoneNumber("23432413235")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber("22222222").withName("Andre Kurbatov")
				.withLogin(login).withPassword("22443v").withScholarshipAmount(BigDecimal.valueOf(232))
				.withCurrencyMark("USD").withAdmissionDate(LocalDate.of(1999, 05, 02))
				.withSpecialization(Specialization.COMPUTER_SCIENCE).build();
		StudentDto studentDto1 = StudentDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbadtovandre@gmail.com").withTelephoneNumber("233432413235")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber("22272222").withName("Andre Kurbatov")
				.withLogin(login).withPassword("22443v").withScholarshipAmount(BigDecimal.valueOf(232))
				.withCurrencyMark("USD").withAdmissionDate(LocalDate.of(1999, 05, 02))
				.withSpecialization(Specialization.COMPUTER_SCIENCE).build();
		assertDoesNotThrow(() -> {
			studentService.save(studentDto0);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			studentService.save(studentDto1);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().size());
		((ServiceDataIntegrityException) throwable).getExceptions().forEach(exception -> {
			assertTrue(exception.toString().contains("field=" + field));
			assertTrue(exception.toString().contains("message=" + message));
		});
	}

	@Test
	void testSave_AskSaveStudentWithDuplicateTelephoneNumber_ExceptionShouldArise() {
		String message = "A user with this telephone number already exists";
		String field = "telephoneNumber";
		String telephoneNumber = "1111111111";
		StudentDto studentDto0 = StudentDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbatovandre@gmail.com").withTelephoneNumber(telephoneNumber)
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber("22222222").withName("Andre Kurbatov")
				.withLogin("login0").withPassword("22443v").withScholarshipAmount(BigDecimal.valueOf(232))
				.withCurrencyMark("USD").withAdmissionDate(LocalDate.of(1999, 05, 02))
				.withSpecialization(Specialization.COMPUTER_SCIENCE).build();
		StudentDto studentDto1 = StudentDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbadtovandre@gmail.com").withTelephoneNumber(telephoneNumber)
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber("22272222").withName("Andre Kurbatov")
				.withLogin("login1").withPassword("22443v").withScholarshipAmount(BigDecimal.valueOf(232))
				.withCurrencyMark("USD").withAdmissionDate(LocalDate.of(1999, 05, 02))
				.withSpecialization(Specialization.COMPUTER_SCIENCE).build();
		assertDoesNotThrow(() -> {
			studentService.save(studentDto0);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			studentService.save(studentDto1);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().size());
		((ServiceDataIntegrityException) throwable).getExceptions().forEach(exception -> {
			assertTrue(exception.toString().contains("field=" + field));
			assertTrue(exception.toString().contains("message=" + message));
		});
	}

	@Test
	void testSave_AskSaveStudentWithDuplicateEmail_ExceptionShouldArise() {
		String message = "A user with this email already exists";
		String field = "email";
		String email = "kurbatovandre@gmail.com";
		StudentDto studentDto0 = StudentDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail(email).withTelephoneNumber("2345223")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber("22222222").withName("Andre Kurbatov")
				.withLogin("login0").withPassword("22443v").withScholarshipAmount(BigDecimal.valueOf(232))
				.withCurrencyMark("USD").withAdmissionDate(LocalDate.of(1999, 05, 02))
				.withSpecialization(Specialization.COMPUTER_SCIENCE).build();
		StudentDto studentDto1 = StudentDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail(email).withTelephoneNumber("234243")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber("22272222").withName("Andre Kurbatov")
				.withLogin("login1").withPassword("22443v").withScholarshipAmount(BigDecimal.valueOf(232))
				.withCurrencyMark("USD").withAdmissionDate(LocalDate.of(1999, 05, 02))
				.withSpecialization(Specialization.COMPUTER_SCIENCE).build();
		assertDoesNotThrow(() -> {
			studentService.save(studentDto0);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			studentService.save(studentDto1);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().size());
		((ServiceDataIntegrityException) throwable).getExceptions().forEach(exception -> {
			assertTrue(exception.toString().contains("field=" + field));
			assertTrue(exception.toString().contains("message=" + message));
		});
	}

	@Test
	void testSave_AskSaveStudentWithDuplicatePassportNumber_ExceptionShouldArise() {
		String message = "A user with this passport number already exists";
		String field = "passportNumber";
		String passportNumber = "11111111";
		StudentDto studentDto0 = StudentDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbatovandre@gmail.com").withTelephoneNumber("2345223")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber(passportNumber)
				.withName("Andre Kurbatov").withLogin("login0").withPassword("22443v")
				.withScholarshipAmount(BigDecimal.valueOf(232)).withCurrencyMark("USD")
				.withAdmissionDate(LocalDate.of(1999, 05, 02)).withSpecialization(Specialization.COMPUTER_SCIENCE)
				.build();
		StudentDto studentDto1 = StudentDto.builder().withId(10001L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbatolvandre@gmail.com").withTelephoneNumber("234243")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber(passportNumber)
				.withName("Andre Kurbatov").withLogin("login1").withPassword("22443v")
				.withScholarshipAmount(BigDecimal.valueOf(232)).withCurrencyMark("USD")
				.withAdmissionDate(LocalDate.of(1999, 05, 02)).withSpecialization(Specialization.COMPUTER_SCIENCE)
				.build();
		assertDoesNotThrow(() -> {
			studentService.save(studentDto0);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			studentService.save(studentDto1);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().size());
		((ServiceDataIntegrityException) throwable).getExceptions().forEach(exception -> {
			assertTrue(exception.toString().contains("field=" + field));
			assertTrue(exception.toString().contains("message=" + message));
		});
	}

	@Test
	void testSave_AskSaveStudentifExistsAdministratorWithDuplicatePassportNumber_ExceptionShouldArise() {
		String message = "A user with this passport number already exists";
		String field = "passportNumber";
		String passportNumber = "11111111";
		StudentDto studentDto = StudentDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbatovandre@gmail.com").withTelephoneNumber("2345223")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber(passportNumber)
				.withName("Andre Kurbatov").withLogin("login0").withPassword("22443v")
				.withScholarshipAmount(BigDecimal.valueOf(232)).withCurrencyMark("USD")
				.withAdmissionDate(LocalDate.of(1999, 05, 02)).withSpecialization(Specialization.COMPUTER_SCIENCE)
				.build();
		AdministratorDto administratorDto = AdministratorDto.builder().withId(10001L)
				.withBirthDate(LocalDate.of(2002, 01, 13)).withGender(Gender.M).withEmail("kurbatolvandre@gmail.com")
				.withTelephoneNumber("234243").withResidenceAddress("Via Napoli Pirugia")
				.withPassportNumber(passportNumber).withName("Andre Kurbatov").withLogin("login1")
				.withPassword("22443v").withSalaryAmount("23232").withCurrencyMark("USD")
				.withEmploymentDate(LocalDate.of(1999, 05, 02)).withWorkingShift(WorkingShift.FULL_TIME).build();
		assertDoesNotThrow(() -> {
			studentService.save(studentDto);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			administratorService.save(administratorDto);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().size());
		((ServiceDataIntegrityException) throwable).getExceptions().forEach(exception -> {
			assertTrue(exception.toString().contains("field=" + field));
			assertTrue(exception.toString().contains("message=" + message));
		});
	}

	@Test
	void testSave_AskSaveStudentifExistsTeacherWithDuplicatePassportNumber_ExceptionShouldArise() {
		String message = "A user with this passport number already exists";
		String field = "passportNumber";
		String passportNumber = "11111111";
		StudentDto studentDto = StudentDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbatovandre@gmail.com").withTelephoneNumber("2345223")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber(passportNumber)
				.withName("Andre Kurbatov").withLogin("login0").withPassword("22443v")
				.withScholarshipAmount(BigDecimal.valueOf(232)).withCurrencyMark("USD")
				.withAdmissionDate(LocalDate.of(1999, 05, 02)).withSpecialization(Specialization.COMPUTER_SCIENCE)
				.build();
		TeacherDto teacherDto = TeacherDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbatolvandre@gmail.com").withTelephoneNumber("234243")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber(passportNumber)
				.withName("Andre Kurbatov").withLogin("login1").withPassword("22443v").withSalaryAmount("23232")
				.withCurrencyMark("USD").withEmploymentDate(LocalDate.of(1999, 05, 02))
				.withWorkingShift(WorkingShift.FULL_TIME).build();
		assertDoesNotThrow(() -> {
			teacherService.save(teacherDto);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			studentService.save(studentDto);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().size());
		((ServiceDataIntegrityException) throwable).getExceptions().forEach(exception -> {
			assertTrue(exception.toString().contains("field=" + field));
			assertTrue(exception.toString().contains("message=" + message));
		});
	}

	@Test
	void testSave_AskSaveStudentWithDuplicatePassportNumberEmailTelephoneNumberLogin_ExceptionShouldArise() {
		String login = "admin";
		String telephoneNumber = "1111111111";
		String email = "kurbatovandre@gmail.com";
		String passportNumber = "11111111";
		StudentDto studentDto0 = StudentDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail(email).withTelephoneNumber(telephoneNumber)
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber(passportNumber)
				.withName("Andre Kurbatov").withLogin(login).withPassword("22443v")
				.withScholarshipAmount(BigDecimal.valueOf(232)).withCurrencyMark("USD")
				.withAdmissionDate(LocalDate.of(1999, 05, 02)).withSpecialization(Specialization.COMPUTER_SCIENCE)
				.build();
		StudentDto studentDto1 = StudentDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail(email).withTelephoneNumber(telephoneNumber)
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber(passportNumber)
				.withName("Andre Kurbatov").withLogin(login).withPassword("22443v")
				.withScholarshipAmount(BigDecimal.valueOf(232)).withCurrencyMark("USD")
				.withAdmissionDate(LocalDate.of(1999, 05, 02)).withSpecialization(Specialization.COMPUTER_SCIENCE)
				.build();
		assertDoesNotThrow(() -> {
			studentService.save(studentDto0);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			studentService.save(studentDto1);
		});
		assertEquals(4, ((ServiceDataIntegrityException) throwable).getExceptions().size());
	}
}
