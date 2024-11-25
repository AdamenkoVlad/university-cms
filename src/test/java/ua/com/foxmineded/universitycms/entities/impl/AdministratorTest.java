package ua.com.foxmineded.universitycms.entities.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.instancio.Instancio;
import static org.instancio.Select.field;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ua.com.foxmineded.universitycms.enums.Gender;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class AdministratorTest {
	@PersistenceContext
	EntityManager em;

	@Test
	void testToPersistAdministrator() {
		Administrator adminInput = Instancio.of(Administrator.class).ignore(field(Administrator::getId))
				.set(field(Administrator::getGender), Gender.M)
				.set(field(Administrator::getPassportNumber), "1000" + String.valueOf(5000))
				.set(field(Administrator::getEmail),"blablabla@gmail.com")
				.set(field(Administrator::getTelephoneNumber),"12352353")
				.set(field(Administrator::getLogin), "administratorM")
				.set(field(Administrator::getCurrencyMark), "USD")
				.set(field(Administrator::getSalaryAmount), "2500")
				.set(field(Administrator::getBirthDate), LocalDate.of(2000, 12, 25))
				.set(field(Administrator::getEmploymentDate), LocalDate.of(2000, 12, 25))
				.create();;
		assertNull(adminInput.getId());
		em.persist(adminInput);
		em.flush();
		em.clear();
		Administrator adminSaved = em.find(Administrator.class, adminInput.getId());
		assertNotNull(adminSaved.getId());
		assertNotNull(adminSaved.getBirthDate());
		assertNotNull(adminSaved.getEmail());
		assertNotNull(adminSaved.getTelephoneNumber());
		assertNotNull(adminSaved.getResidenceAddress());
		assertNotNull(adminSaved.getPassportNumber());
		assertNotNull(adminSaved.getName());
		assertNotNull(adminSaved.getLogin());
		assertNotNull(adminSaved.getPassword());
		assertNotNull(adminSaved.getRole());
		assertNotNull(adminSaved.getSalaryAmount());
		assertNotNull(adminSaved.getCurrencyMark());
		assertNotNull(adminSaved.getEmploymentDate());
		assertNotNull(adminSaved.getPosition());
		assertNotNull(adminSaved.getWorkingShift());
	}
}
