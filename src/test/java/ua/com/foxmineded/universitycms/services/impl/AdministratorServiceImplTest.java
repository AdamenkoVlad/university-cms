package ua.com.foxmineded.universitycms.services.impl;

import org.junit.jupiter.api.Test;
import org.instancio.Instancio;
import static org.instancio.Select.field;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
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
import ua.com.foxmineded.universitycms.dao.AdministratorRepository;
import ua.com.foxmineded.universitycms.dto.AdministratorDto;
import ua.com.foxmineded.universitycms.dto.StudentDto;
import ua.com.foxmineded.universitycms.dto.TeacherDto;
import ua.com.foxmineded.universitycms.entities.impl.Administrator;
import ua.com.foxmineded.universitycms.enums.Gender;
import ua.com.foxmineded.universitycms.enums.Specialization;
import ua.com.foxmineded.universitycms.enums.WorkingShift;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.AdministratorService;
import ua.com.foxmineded.universitycms.services.StudentService;
import ua.com.foxmineded.universitycms.services.TeacherService;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		AdministratorService.class, StudentService.class, TeacherService.class, TypeMapConfig.class,
		SecurityConfig.class, UserDetailsService.class }))
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = { "/test/sql/clear_tables.sql" })
class AdministratorServiceImplTest {
	@SpyBean
	AdministratorRepository administratorRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	AdministratorService administratorService;
	@Autowired
	StudentService studentService;
	@Autowired
	TeacherService teacherService;

	@WithMockUser(roles = { "ADMINISTRATOR" })
	@Test
	void testDeleteById_AskDeleteByIdIfAdministratorIdInvalid_ExceptionShouldArise() {
		Long administratorId = 10000L;
		String message = "The administrator with id = %d was not found".formatted(administratorId);
		when(administratorRepository.findById(administratorId)).thenReturn(Optional.empty());
		Throwable throwable = assertThrows(ServiceException.class, () -> {
			administratorService.deleteById(administratorId);
		});
		assertEquals(message, throwable.getMessage());
	}

	@WithMockUser(roles = { "STUDENT", "TEACHER" })
	@Test
	void testDeleteById_AskDeleteByIdIfRoleInvalid_ExceptionShouldArise() {
		Long administratorId = 10000L;
		assertThrows(AccessDeniedException.class, () -> {
			administratorService.deleteById(administratorId);
		});
	}

	@WithMockUser(roles = { "ADMINISTRATOR" })
	@Test
	void testDeleteById_AskDeleteById_DeletionShouldSucceed() {
		Long administratorId = 10000L;
		Administrator administrator = Instancio.of(Administrator.class)
				.set(field(Administrator::getId), administratorId).create();
		when(administratorRepository.findById(administratorId)).thenReturn(Optional.of(administrator));
		assertDoesNotThrow(() -> {
			administratorService.deleteById(administratorId);
		});
	}

	@WithMockUser(roles = { "ADMINISTRATOR" })
	@Test
	void testSave_AskSaveAdministratorWithDuplicateLogin_ExceptionShouldArise() {
		String message = "A user with this login already exists";
		String field = "login";
		String login = "admin";
		AdministratorDto administratorDto0 = AdministratorDto.builder().withId(10000L)
				.withBirthDate(LocalDate.of(2002, 01, 13)).withGender(Gender.M).withEmail("kurbatovandre@gmail.com")
				.withTelephoneNumber("23432413235").withResidenceAddress("Via Napoli Pirugia")
				.withPassportNumber("22222222").withName("Andre Kurbatov").withLogin(login).withPassword("22443v")
				.withSalaryAmount("0").withCurrencyMark("USD").withEmploymentDate(LocalDate.of(1999, 05, 02))
				.withPosition("lfjs").withWorkingShift(WorkingShift.FULL_TIME).build();
		AdministratorDto administratorDto1 = AdministratorDto.builder().withId(10000L)
				.withBirthDate(LocalDate.of(2002, 01, 13)).withGender(Gender.M).withEmail("kurbatdovandre@gmail.com")
				.withTelephoneNumber("232432413235").withResidenceAddress("Via Napoli Pirugia")
				.withPassportNumber("11111111").withName("Andre Kurbatov").withLogin(login).withPassword("22443v")
				.withSalaryAmount("0").withCurrencyMark("USD").withEmploymentDate(LocalDate.of(1999, 05, 02))
				.withPosition("lfjs").withWorkingShift(WorkingShift.FULL_TIME).build();
		assertDoesNotThrow(() -> {
			administratorService.save(administratorDto0);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			administratorService.save(administratorDto1);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().size());
		((ServiceDataIntegrityException) throwable).getExceptions().forEach(exception -> {
			assertTrue(exception.toString().contains("field=" + field));
			assertTrue(exception.toString().contains("message=" + message));
		});
	}

	@WithMockUser(roles = { "ADMINISTRATOR" })
	@Test
	void testSave_AskSaveAdministratorWithDuplicateTelephoneNumber_ExceptionShouldArise() {
		String message = "A user with this telephone number already exists";
		String field = "telephoneNumber";
		String telephoneNumber = "1111111111";
		AdministratorDto administratorDto0 = AdministratorDto.builder().withId(10000L)
				.withBirthDate(LocalDate.of(2002, 01, 13)).withGender(Gender.M).withEmail("kurbatovandre@gmail.com")
				.withTelephoneNumber(telephoneNumber).withResidenceAddress("Via Napoli Pirugia")
				.withPassportNumber("22222222").withName("Andre Kurbatov").withLogin("admin1").withPassword("22443v")
				.withSalaryAmount("0").withCurrencyMark("USD").withEmploymentDate(LocalDate.of(1999, 05, 02))
				.withPosition("lfjs").withWorkingShift(WorkingShift.FULL_TIME).build();
		AdministratorDto administratorDto1 = AdministratorDto.builder().withId(10000L)
				.withBirthDate(LocalDate.of(2002, 01, 13)).withGender(Gender.M).withEmail("kurbatdovandre@gmail.com")
				.withTelephoneNumber(telephoneNumber).withResidenceAddress("Via Napoli Pirugia")
				.withPassportNumber("11111111").withName("Andre Kurbatov").withLogin("admin2").withPassword("22443v")
				.withSalaryAmount("0").withCurrencyMark("USD").withEmploymentDate(LocalDate.of(1999, 05, 02))
				.withPosition("lfjs").withWorkingShift(WorkingShift.FULL_TIME).build();
		assertDoesNotThrow(() -> {
			administratorService.save(administratorDto0);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			administratorService.save(administratorDto1);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().size());
		((ServiceDataIntegrityException) throwable).getExceptions().forEach(exception -> {
			assertTrue(exception.toString().contains("field=" + field));
			assertTrue(exception.toString().contains("message=" + message));
		});
	}

	@WithMockUser(roles = { "ADMINISTRATOR" })
	@Test
	void testSave_AskSaveAdministratorWithDuplicateEmail_ExceptionShouldArise() {
		String message = "A user with this email already exists";
		String field = "email";
		String email = "kurbatovandre@gmail.com";
		AdministratorDto administratorDto0 = AdministratorDto.builder().withId(10000L)
				.withBirthDate(LocalDate.of(2002, 01, 13)).withGender(Gender.M).withEmail(email)
				.withTelephoneNumber("11111234324324").withResidenceAddress("Via Napoli Pirugia")
				.withPassportNumber("22222222").withName("Andre Kurbatov").withLogin("admin1").withPassword("22443v")
				.withSalaryAmount("0").withCurrencyMark("USD").withEmploymentDate(LocalDate.of(1999, 05, 02))
				.withPosition("lfjs").withWorkingShift(WorkingShift.FULL_TIME).build();
		AdministratorDto administratorDto1 = AdministratorDto.builder().withId(10000L)
				.withBirthDate(LocalDate.of(2002, 01, 13)).withGender(Gender.M).withEmail(email)
				.withTelephoneNumber("234252353").withResidenceAddress("Via Napoli Pirugia")
				.withPassportNumber("11111111").withName("Andre Kurbatov").withLogin("admin2").withPassword("22443v")
				.withSalaryAmount("0").withCurrencyMark("USD").withEmploymentDate(LocalDate.of(1999, 05, 02))
				.withPosition("lfjs").withWorkingShift(WorkingShift.FULL_TIME).build();
		assertDoesNotThrow(() -> {
			administratorService.save(administratorDto0);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			administratorService.save(administratorDto1);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().size());
		((ServiceDataIntegrityException) throwable).getExceptions().forEach(exception -> {
			assertTrue(exception.toString().contains("field=" + field));
			assertTrue(exception.toString().contains("message=" + message));
		});
	}

	@WithMockUser(roles = { "ADMINISTRATOR" })
	@Test
	void testSave_AskSaveAdministratorWithDuplicatePassportNumber_ExceptionShouldArise() {
		String message = "A user with this passport number already exists";
		String field = "passportNumber";
		String passportNumber = "11111111";
		AdministratorDto administratorDto0 = AdministratorDto.builder().withId(10000L)
				.withBirthDate(LocalDate.of(2002, 01, 13)).withGender(Gender.M).withEmail("kurbatovandre@gmail.com")
				.withTelephoneNumber("11111234324324").withResidenceAddress("Via Napoli Pirugia")
				.withPassportNumber(passportNumber).withName("Andre Kurbatov").withLogin("admin1")
				.withPassword("22443v").withSalaryAmount("0").withCurrencyMark("USD")
				.withEmploymentDate(LocalDate.of(1999, 05, 02)).withPosition("lfjs")
				.withWorkingShift(WorkingShift.FULL_TIME).build();
		AdministratorDto administratorDto1 = AdministratorDto.builder().withId(10000L)
				.withBirthDate(LocalDate.of(2002, 01, 13)).withGender(Gender.M).withEmail("andre@gmail.com")
				.withTelephoneNumber("234252353").withResidenceAddress("Via Napoli Pirugia")
				.withPassportNumber(passportNumber).withName("Andre Kurbatov").withLogin("admin2")
				.withPassword("22443v").withSalaryAmount("0").withCurrencyMark("USD")
				.withEmploymentDate(LocalDate.of(1999, 05, 02)).withPosition("lfjs")
				.withWorkingShift(WorkingShift.FULL_TIME).build();
		assertDoesNotThrow(() -> {
			administratorService.save(administratorDto0);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			administratorService.save(administratorDto1);
		});
		assertEquals(1, ((ServiceDataIntegrityException) throwable).getExceptions().size());
		((ServiceDataIntegrityException) throwable).getExceptions().forEach(exception -> {
			assertTrue(exception.toString().contains("field=" + field));
			assertTrue(exception.toString().contains("message=" + message));
		});
	}

	@WithMockUser(roles = { "ADMINISTRATOR" })
	@Test
	void testSave_AskSaveAdministratorIfExistsStudentWithDuplicatePassportNumber_ExceptionShouldArise() {
		String message = "A user with this passport number already exists";
		String field = "passportNumber";
		String passportNumber = "11111111";
		AdministratorDto administratorDto = AdministratorDto.builder().withId(10000L)
				.withBirthDate(LocalDate.of(2002, 01, 13)).withGender(Gender.M).withEmail("kurbatovandre@gmail.com")
				.withTelephoneNumber("11111234324324").withResidenceAddress("Via Napoli Pirugia")
				.withPassportNumber(passportNumber).withName("Andre Kurbatov").withLogin("admin1")
				.withPassword("22443v").withSalaryAmount("0").withCurrencyMark("USD")
				.withEmploymentDate(LocalDate.of(1999, 05, 02)).withPosition("lfjs")
				.withWorkingShift(WorkingShift.FULL_TIME).build();
		StudentDto studentDto = StudentDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("andre@gmail.com").withTelephoneNumber("234252353")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber(passportNumber)
				.withName("Andre Kurbatov").withLogin("admin2").withPassword("22443v")
				.withScholarshipAmount(BigDecimal.valueOf(2343)).withCurrencyMark("USD")
				.withAdmissionDate(LocalDate.of(1999, 05, 02)).withSpecialization(Specialization.ART).build();
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

	@WithMockUser(roles = { "ADMINISTRATOR" })
	@Test
	void testSave_AskSaveAdministratorIfExistsTeacherWithDuplicatePassportNumber_ExceptionShouldArise() {
		String message = "A user with this passport number already exists";
		String field = "passportNumber";
		String passportNumber = "11111111";
		AdministratorDto administratorDto = AdministratorDto.builder().withId(10000L)
				.withBirthDate(LocalDate.of(2002, 01, 13)).withGender(Gender.M).withEmail("kurbatovandre@gmail.com")
				.withTelephoneNumber("11111234324324").withResidenceAddress("Via Napoli Pirugia")
				.withPassportNumber(passportNumber).withName("Andre Kurbatov").withLogin("admin1")
				.withPassword("22443v").withSalaryAmount("0").withCurrencyMark("USD")
				.withEmploymentDate(LocalDate.of(1999, 05, 02)).withPosition("lfjs")
				.withWorkingShift(WorkingShift.FULL_TIME).build();
		TeacherDto teacherDto = TeacherDto.builder().withId(10000L).withBirthDate(LocalDate.of(2002, 01, 13))
				.withGender(Gender.M).withEmail("andre@gmail.com").withTelephoneNumber("234252353")
				.withResidenceAddress("Via Napoli Pirugia").withPassportNumber(passportNumber)
				.withName("Andre Kurbatov").withLogin("admin2").withPassword("22443v").withSalaryAmount("2434")
				.withCurrencyMark("USD").withEmploymentDate(LocalDate.of(1999, 05, 02))
				.withWorkingShift(WorkingShift.FULL_TIME).build();
		assertDoesNotThrow(() -> {
			teacherService.save(teacherDto);
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

	@WithMockUser(roles = { "ADMINISTRATOR" })
	@Test
	void testSave_AskSaveAdministratorWithDuplicatePassportNumberEmailTelephoneNumberLogin_ExceptionShouldArise() {
		String login = "admin";
		String telephoneNumber = "1111111111";
		String email = "kurbatovandre@gmail.com";
		String passportNumber = "11111111";
		AdministratorDto administratorDto0 = AdministratorDto.builder().withId(10000L)
				.withBirthDate(LocalDate.of(2002, 01, 13)).withGender(Gender.M).withEmail(email)
				.withTelephoneNumber(telephoneNumber).withResidenceAddress("Via Napoli Pirugia")
				.withPassportNumber(passportNumber).withName("Andre Kurbatov").withLogin(login).withPassword("22443v")
				.withSalaryAmount("0").withCurrencyMark("USD").withEmploymentDate(LocalDate.of(1999, 05, 02))
				.withPosition("lfjs").withWorkingShift(WorkingShift.FULL_TIME).build();
		AdministratorDto administratorDto1 = AdministratorDto.builder().withId(10000L)
				.withBirthDate(LocalDate.of(2002, 01, 13)).withGender(Gender.M).withEmail(email)
				.withTelephoneNumber(telephoneNumber).withResidenceAddress("Via Napoli Pirugia")
				.withPassportNumber(passportNumber).withName("Andre Kurbatov").withLogin(login).withPassword("22443v")
				.withSalaryAmount("0").withCurrencyMark("USD").withEmploymentDate(LocalDate.of(1999, 05, 02))
				.withPosition("lfjs").withWorkingShift(WorkingShift.FULL_TIME).build();
		assertDoesNotThrow(() -> {
			administratorService.save(administratorDto0);
		});
		Throwable throwable = assertThrows(ServiceDataIntegrityException.class, () -> {
			administratorService.save(administratorDto1);
		});
		assertEquals(4, ((ServiceDataIntegrityException) throwable).getExceptions().size());
	}
}
