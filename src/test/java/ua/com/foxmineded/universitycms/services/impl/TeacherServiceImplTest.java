package ua.com.foxmineded.universitycms.services.impl;

import static org.instancio.Select.field;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.instancio.Instancio;
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
import ua.com.foxmineded.universitycms.dao.TeacherRepository;
import ua.com.foxmineded.universitycms.dto.AdministratorDto;
import ua.com.foxmineded.universitycms.dto.StudentDto;
import ua.com.foxmineded.universitycms.dto.TeacherDto;
import ua.com.foxmineded.universitycms.entities.impl.Teacher;
import ua.com.foxmineded.universitycms.enums.Gender;
import ua.com.foxmineded.universitycms.enums.Specialization;
import ua.com.foxmineded.universitycms.enums.WorkingShift;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.AdministratorService;
import ua.com.foxmineded.universitycms.services.StudentService;
import ua.com.foxmineded.universitycms.services.TeacherService;

@WithMockUser(roles = { "ADMINISTRATOR" })
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { TeacherService.class,
		AdministratorService.class, StudentService.class, TypeMapConfig.class, SecurityConfig.class,
		UserDetailsService.class }))
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = { "/test/sql/clear_tables.sql" })
class TeacherServiceImplTest {
	@SpyBean
	TeacherRepository teacherRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	TeacherService teacherService;
	@Autowired
	AdministratorService administratorService;
	@Autowired
	StudentService studentService;

	@Test
	void testDeleteById_AskDeleteByIdIfIdInvalid_ExceptionShouldArise() {
		Long teacherId = 10000L;
		String message = "The teacher with id = %d was not found".formatted(teacherId);
		when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			teacherService.deleteById(teacherId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@WithMockUser(roles = { "STUDENT" })

	@Test
	void testDeleteById_AskDeleteByIdIfRoleInvalid_ExceptionShouldArise() {
		Long teacherId = 10000L;
		assertThrows(AccessDeniedException.class, () -> {
			teacherService.deleteById(teacherId);
		});
	}

	@Test
	void testDeleteById_AskDeleteById_DeletionShouldSucceed() {
		Long teacherId = 10000L;
		Teacher teacher = Instancio.of(Teacher.class).set(field(Teacher::getId), teacherId).create();
		when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
		assertDoesNotThrow(() -> {
			teacherService.deleteById(teacherId);
		});
	}

	@Test
	void testSave_AskSaveTeachersWithDuplicateLogin_ExceptionShouldArise() {
		String message = "A user with this login already exists";
		String field = "login";
		String login = "admin";
		TeacherDto teacherDto0 = TeacherDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbatovandre@gmail.com").withTelephoneNumber("23432413235")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber("22222222").withName("Andre Kurbatov")
				.withLogin(login).withPassword("22443v").withSalaryAmount("2343").withCurrencyMark("USD")
				.withEmploymentDate(LocalDate.of(1999, 05, 02)).withScientificDegree("ldldfdjf").build();
		TeacherDto teacherDto1 = TeacherDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbadtovandre@gmail.com").withTelephoneNumber("233432413235")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber("22272222").withName("Andre Kurbatov")
				.withLogin(login).withPassword("22443v").withSalaryAmount("34324").withCurrencyMark("USD")
				.withEmploymentDate(LocalDate.of(1999, 05, 02)).withScientificDegree("dfdsfd").build();
		assertDoesNotThrow(() -> {
			teacherService.save(teacherDto0);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			teacherService.save(teacherDto1);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().size());
		((ServiceDataIntegrityException) throwable).getExceptions().forEach(exception -> {
			assertTrue(exception.toString().contains("field=" + field));
			assertTrue(exception.toString().contains("message=" + message));
		});
	}

	@Test
	void testSave_AskSaveTeacherWithDuplicateTelephoneNumber_ExceptionShouldArise() {
		String message = "A user with this telephone number already exists";
		String field = "telephoneNumber";
		String telephoneNumber = "1111111111";
		TeacherDto teacherDto0 = TeacherDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbatovandre@gmail.com").withTelephoneNumber(telephoneNumber)
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber("22222222").withName("Andre Kurbatov")
				.withLogin("login0").withPassword("22443v").withSalaryAmount("23434").withCurrencyMark("USD")
				.withEmploymentDate(LocalDate.of(1999, 05, 02)).withScientificDegree("243243").build();
		TeacherDto teacherDto1 = TeacherDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbadtovandre@gmail.com").withTelephoneNumber(telephoneNumber)
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber("22272222").withName("Andre Kurbatov")
				.withLogin("login1").withPassword("22443v").withSalaryAmount("2423432").withCurrencyMark("USD")
				.withEmploymentDate(LocalDate.of(1999, 05, 02)).withScientificDegree("234235").build();
		assertDoesNotThrow(() -> {
			teacherService.save(teacherDto0);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			teacherService.save(teacherDto1);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().size());
		((ServiceDataIntegrityException) throwable).getExceptions().forEach(exception -> {
			assertTrue(exception.toString().contains("field=" + field));
			assertTrue(exception.toString().contains("message=" + message));
		});
	}

	@Test
	void testSave_AskSaveTeacherWithDuplicateEmail_ExceptionShouldArise() {
		String message = "A user with this email already exists";
		String field = "email";
		String email = "kurbatovandre@gmail.com";
		TeacherDto teacherDto0 = TeacherDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail(email).withTelephoneNumber("2345223")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber("22222222").withName("Andre Kurbatov")
				.withLogin("login0").withPassword("22443v").withSalaryAmount("234324").withCurrencyMark("USD")
				.withEmploymentDate(LocalDate.of(1999, 05, 02)).withScientificDegree("sdfsf").build();
		TeacherDto teacherDto1 = TeacherDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail(email).withTelephoneNumber("234243")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber("22272222").withName("Andre Kurbatov")
				.withLogin("login1").withPassword("22443v").withSalaryAmount("23434").withCurrencyMark("USD")
				.withEmploymentDate(LocalDate.of(1999, 05, 02)).withScientificDegree("sdfsdfd").build();
		assertDoesNotThrow(() -> {
			teacherService.save(teacherDto0);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			teacherService.save(teacherDto1);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().size());
		((ServiceDataIntegrityException) throwable).getExceptions().forEach(exception -> {
			assertTrue(exception.toString().contains("field=" + field));
			assertTrue(exception.toString().contains("message=" + message));
		});
	}

	@Test
	void testSave_AskSaveTeacherWithDuplicatePassportNumber_ExceptionShouldArise() {
		String message = "A user with this passport number already exists";
		String field = "passportNumber";
		String passportNumber = "11111111";
		TeacherDto teacherDto0 = TeacherDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbatovandre@gmail.com").withTelephoneNumber("2345223")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber(passportNumber)
				.withName("Andre Kurbatov").withLogin("login0").withPassword("22443v").withSalaryAmount("2342343")
				.withCurrencyMark("USD").withEmploymentDate(LocalDate.of(1999, 05, 02)).withScientificDegree("sdfdsf")
				.build();
		TeacherDto teacherDto1 = TeacherDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbatolvandre@gmail.com").withTelephoneNumber("234243")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber(passportNumber)
				.withName("Andre Kurbatov").withLogin("login1").withPassword("22443v").withSalaryAmount("234234")
				.withCurrencyMark("USD").withEmploymentDate(LocalDate.of(1999, 05, 02)).withScientificDegree("fdsdfdf")
				.build();
		assertDoesNotThrow(() -> {
			teacherService.save(teacherDto0);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			teacherService.save(teacherDto1);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().size());
		((ServiceDataIntegrityException) throwable).getExceptions().forEach(exception -> {
			assertTrue(exception.toString().contains("field=" + field));
			assertTrue(exception.toString().contains("message=" + message));
		});
	}

	@Test
	void testSave_AskSaveTeacherIfExistsStudentWithDuplicatePassportNumber_ExceptionShouldArise() {
		String message = "A user with this passport number already exists";
		String field = "passportNumber";
		String passportNumber = "11111111";
		TeacherDto teacherDto = TeacherDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbatovandre@gmail.com").withTelephoneNumber("2345223")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber(passportNumber)
				.withName("Andre Kurbatov").withLogin("login0").withPassword("22443v").withSalaryAmount("2342343")
				.withCurrencyMark("USD").withEmploymentDate(LocalDate.of(1999, 05, 02)).withScientificDegree("sdfdsf")
				.build();
		StudentDto studentDto = StudentDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbatolvandre@gmail.com").withTelephoneNumber("234243")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber(passportNumber)
				.withName("Andre Kurbatov").withLogin("login1").withPassword("22443v")
				.withScholarshipAmount(BigDecimal.valueOf(23432)).withCurrencyMark("USD")
				.withAdmissionDate(LocalDate.of(1999, 05, 02)).withSpecialization(Specialization.ART).build();
		assertDoesNotThrow(() -> {
			studentService.save(studentDto);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			teacherService.save(teacherDto);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().size());
		((ServiceDataIntegrityException) throwable).getExceptions().forEach(exception -> {
			assertTrue(exception.toString().contains("field=" + field));
			assertTrue(exception.toString().contains("message=" + message));
		});
	}

	@Test
	void testSave_AskSaveTeacherIfExistsAdministratorWithDuplicatePassportNumber_ExceptionShouldArise() {
		String message = "A user with this passport number already exists";
		String field = "passportNumber";
		String passportNumber = "11111111";
		TeacherDto teacherDto = TeacherDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("kurbatovandre@gmail.com").withTelephoneNumber("2345223")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber(passportNumber)
				.withName("Andre Kurbatov").withLogin("login0").withPassword("22443v").withSalaryAmount("2342343")
				.withCurrencyMark("USD").withEmploymentDate(LocalDate.of(1999, 05, 02)).withScientificDegree("sdfdsf")
				.build();
		AdministratorDto administratorDto = AdministratorDto.builder().withId(10000L)
				.withBirthDate(LocalDate.of(2002, 01, 13)).withGender(Gender.M).withEmail("kurbatolvandre@gmail.com")
				.withTelephoneNumber("234243").withResidenceAddress("Via Napoli Pirugia")
				.withPassportNumber(passportNumber).withName("Andre Kurbatov").withLogin("login1")
				.withPassword("22443v").withSalaryAmount("23432").withCurrencyMark("USD")
				.withEmploymentDate(LocalDate.of(1999, 05, 02)).withWorkingShift(WorkingShift.PART_TIME).build();
		assertDoesNotThrow(() -> {
			administratorService.save(administratorDto);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			teacherService.save(teacherDto);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().size());
		((ServiceDataIntegrityException) throwable).getExceptions().forEach(exception -> {
			assertTrue(exception.toString().contains("field=" + field));
			assertTrue(exception.toString().contains("message=" + message));
		});
	}

	@Test
	void testSave_AskSaveTeacherWithDuplicatePassportNumberEmailTelephoneNumberLogin_ExceptionShouldArise() {
		String login = "admin";
		String telephoneNumber = "1111111111";
		String email = "kurbatovandre@gmail.com";
		String passportNumber = "11111111";
		TeacherDto teacherDto0 = TeacherDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail(email).withTelephoneNumber(telephoneNumber)
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber(passportNumber)
				.withName("Andre Kurbatov").withLogin(login).withPassword("22443v").withSalaryAmount("234324")
				.withCurrencyMark("USD").withEmploymentDate(LocalDate.of(1999, 05, 02)).withScientificDegree("slfjdslk")
				.build();
		TeacherDto teacherDto1 = TeacherDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail(email).withTelephoneNumber(telephoneNumber)
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber(passportNumber)
				.withName("Andre Kurbatov").withLogin(login).withPassword("22443v").withSalaryAmount("2343")
				.withCurrencyMark("USD").withEmploymentDate(LocalDate.of(1999, 05, 02)).withScientificDegree("sfsdfd")
				.build();
		assertDoesNotThrow(() -> {
			teacherService.save(teacherDto0);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			teacherService.save(teacherDto1);
		});
		assertEquals(4, ((ServiceDataIntegrityException) throwable).getExceptions().size());
	}

}
