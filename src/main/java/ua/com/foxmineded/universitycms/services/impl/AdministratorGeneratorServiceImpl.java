package ua.com.foxmineded.universitycms.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxmineded.universitycms.dao.AdministratorRepository;
import ua.com.foxmineded.universitycms.dao.AvatarRepository;
import ua.com.foxmineded.universitycms.entities.impl.Administrator;
import ua.com.foxmineded.universitycms.entities.impl.Avatar;
import ua.com.foxmineded.universitycms.enums.Gender;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.services.AdministratorGeneratorService;
import ua.com.foxmineded.universitycms.utils.AdministratorPhotoReader;
import ua.com.foxmineded.universitycms.utils.PersonNamesReader;
import org.instancio.*;
import static org.instancio.Select.field;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdministratorGeneratorServiceImpl implements AdministratorGeneratorService {
	private static final int TOTAL_ADMINISTRATORS_AMOUNT = 10;
	private static final int FIRST_MAN_NAMES_AMOUNT = 20;
	private static final int LAST_NAMES_AMOUNT = 20;
	private final PasswordEncoder passwordEncoder;
	private final PersonNamesReader personNamesReader;
	private final AdministratorPhotoReader administratorPhotoReader;
	private final AdministratorRepository administratorRepository;
	private final AvatarRepository avatarRepository;

	@Override
	public List<Administrator> generate() {
		log.info("The administrators generation process was started");
		Random random = new Random();
		List<String> names = generateFullNames();
		List<Administrator> administrators = new ArrayList<>();
		Avatar avatarUser = new Avatar();
		avatarUser.setAvatarContents(administratorPhotoReader.readPng());
		avatarUser.setContentType("image/png");
		avatarUser.setRole(Role.ADMINISTRATOR);
		UUID uuidUser = avatarRepository.save(avatarUser).getId();
		Administrator administratorUser = Instancio.of(Administrator.class).ignore(field(Administrator::getId))
				.set(field(Administrator::getName), "Ben Affleck").set(field(Administrator::getGender), Gender.M)
				.set(field(Administrator::getPassportNumber), String.valueOf(76541234))
				.set(field(Administrator::getEmail), "benaffleck@gmail.com")
				.set(field(Administrator::getTelephoneNumber), "134534643345463")
				.set(field(Administrator::getLogin), "admin").set(field(Administrator::getPhotoUuid), uuidUser)
				.set(field(Administrator::getCurrencyMark), "USD").set(field(Administrator::getSalaryAmount), "2500")
				.set(field(Administrator::getBirthDate), LocalDate.of(2000, 12, 25))
				.set(field(Administrator::getEmploymentDate), LocalDate.of(2000, 12, 25))
				.set(field(Administrator::getPassword), passwordEncoder.encode("1234")).create();
		administrators.add(administratorUser);
		for (int i = 0; i < TOTAL_ADMINISTRATORS_AMOUNT; i++) {
			Avatar avatar = new Avatar();
			avatar.setAvatarContents(administratorPhotoReader.readPng());
			avatar.setContentType("image/png");
			avatar.setRole(Role.ADMINISTRATOR);
			UUID uuid = avatarRepository.save(avatar).getId();
			Administrator administrator = Instancio.of(Administrator.class).ignore(field(Administrator::getId))
					.set(field(Administrator::getName), names.get(i)).set(field(Administrator::getGender), Gender.M)
					.set(field(Administrator::getPassportNumber), "1000" + String.valueOf(i + 5000))
					.set(field(Administrator::getEmail),
							names.get(i).toLowerCase().replace(" ", "") + String.valueOf(i + 5000) + "@gmail.com")
					.set(field(Administrator::getTelephoneNumber),
							"1" + String.valueOf(random.nextInt(100, 1000)) + String.valueOf(random.nextInt(100, 1000))
									+ String.valueOf(i + 5000))
					.set(field(Administrator::getLogin), "administratorM" + String.valueOf(i))
					.set(field(Administrator::getCurrencyMark), "USD")
					.set(field(Administrator::getSalaryAmount), "2500")
					.set(field(Administrator::getBirthDate), LocalDate.of(2000, 12, 25))
					.set(field(Administrator::getEmploymentDate), LocalDate.of(2000, 12, 25))
					.set(field(Administrator::getPhotoUuid), uuid).set(field(Administrator::getPassword),
							passwordEncoder.encode(String.valueOf(random.nextInt(100000) + 100000)))
					.create();
			administrators.add(administrator);
		}
		return administratorRepository.saveAll(administrators);
	}

	@SneakyThrows
	List<String> generateFullNames() {
		List<String> firstNames = personNamesReader.readManFirstNames();
		List<String> lastNames = personNamesReader.readLastNames();
		List<String> names = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < TOTAL_ADMINISTRATORS_AMOUNT; i++) {
			int firstNameIndex = random.nextInt(FIRST_MAN_NAMES_AMOUNT);
			int lastNameIndex = random.nextInt(LAST_NAMES_AMOUNT);
			String name = firstNames.get(firstNameIndex) + " " + lastNames.get(lastNameIndex);
			names.add(name);
		}
		return names;
	}

	@Override
	public Long countAll() {
		return administratorRepository.count();
	}

	@Override
	public List<Administrator> findAll() {
		return administratorRepository.findAll();
	}
}
