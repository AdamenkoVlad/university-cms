package ua.com.foxmineded.universitycms.services.impl;

import static org.instancio.Select.field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.instancio.Instancio;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxmineded.universitycms.dao.AvatarRepository;
import ua.com.foxmineded.universitycms.dao.TeacherRepository;
import ua.com.foxmineded.universitycms.entities.impl.Avatar;
import ua.com.foxmineded.universitycms.entities.impl.Teacher;
import ua.com.foxmineded.universitycms.enums.Gender;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.services.TeacherGeneratorService;
import ua.com.foxmineded.universitycms.utils.PersonNamesReader;
import ua.com.foxmineded.universitycms.utils.TeacherPhotoReader;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeacherGeneratorServiceImpl implements TeacherGeneratorService {
	private static final int TEACHERS_AMOUNT = 40;
	private static final int FIRST_NAMES_MAN_AMOUNT = 40;
	private static final int FIRST_NAMES_WOMAN_AMOUNT = 40;
	private static final int LAST_NAMES_AMOUNT = 80;
	private final PasswordEncoder passwordEncoder;
	private final TeacherPhotoReader teacherPhotoReader;
	private final PersonNamesReader personNamesReader;
	private final TeacherRepository teacherRepository;
	private final AvatarRepository avatarRepository;

	@SneakyThrows
	@Override
	public List<Teacher> generate() {
		log.info("The teacher generation process was started");
		Random random = new Random();
		List<Teacher> teachers = new ArrayList<>();
		List<String> manNames = generateManNames();
		List<String> womanNames = generateWomanNames();
		for (int i = 0; i < TEACHERS_AMOUNT / 2; i++) {
			Avatar avatar = new Avatar();
			avatar.setAvatarContents(teacherPhotoReader.readManPng());
			avatar.setContentType("image/png");
			avatar.setRole(Role.TEACHER);
			UUID uuid = avatarRepository.save(avatar).getId();
			Teacher teacher = Instancio.of(Teacher.class).ignore(field(Teacher::getId))
					.ignore(field(Teacher::getCourses)).set(field(Teacher::getName), manNames.get(i))
					.set(field(Teacher::getGender), Gender.M)
					.set(field(Teacher::getEmail),
							manNames.get(i).toLowerCase().replace(" ", "") + String.valueOf(i + 3000) + "@gmail.com")
					.set(field(Teacher::getTelephoneNumber),
							"1" + String.valueOf(random.nextInt(100, 1000)) + String.valueOf(random.nextInt(100, 1000))
									+ String.valueOf(i + 3000))
					.set(field(Teacher::getPassportNumber), String.valueOf(i + 3000))
					.set(field(Teacher::getLogin), "teacherM" + String.valueOf(i))
					.set(field(Teacher::getCurrencyMark), "USD").set(field(Teacher::getSalaryAmount), "2500")
					.set(field(Teacher::getBirthDate), LocalDate.of(2000, 12, 25))
					.set(field(Teacher::getEmploymentDate), LocalDate.of(2000, 12, 25))
					.set(field(Teacher::getPhotoUuid), uuid).set(field(Teacher::getPassword),
							passwordEncoder.encode("1234"))
					.create();
			teachers.add(teacher);
		}
		for (int i = 0; i < TEACHERS_AMOUNT / 2; i++) {
			Avatar avatar = new Avatar();
			avatar.setAvatarContents(teacherPhotoReader.readWomanPng());
			avatar.setContentType("image/png");
			avatar.setRole(Role.TEACHER);
			UUID uuid = avatarRepository.save(avatar).getId();
			Teacher teacher = Instancio.of(Teacher.class).ignore(field(Teacher::getId))
					.ignore(field(Teacher::getCourses)).set(field(Teacher::getName), womanNames.get(i))
					.set(field(Teacher::getGender), Gender.W)
					.set(field(Teacher::getEmail),
							manNames.get(i).toLowerCase().replace(" ", "") + String.valueOf(i + 4000) + "@gmail.com")
					.set(field(Teacher::getTelephoneNumber),
							"1" + String.valueOf(random.nextInt(100, 1000)) + String.valueOf(random.nextInt(100, 1000))
									+ String.valueOf(i + 4000))
					.set(field(Teacher::getPassportNumber), "1000" + String.valueOf(i + 4000))
					.set(field(Teacher::getCurrencyMark), "USD").set(field(Teacher::getSalaryAmount), "2500")
					.set(field(Teacher::getBirthDate), LocalDate.of(2000, 12, 25))
					.set(field(Teacher::getEmploymentDate), LocalDate.of(2000, 12, 25))
					.set(field(Teacher::getLogin), "teacherW" + String.valueOf(i))
					.set(field(Teacher::getPhotoUuid), uuid).set(field(Teacher::getPassword),
							passwordEncoder.encode(String.valueOf(random.nextInt(100000) + 100000)))
					.create();
			teachers.add(teacher);
		}
		return teacherRepository.saveAll(teachers);
	}

	@SneakyThrows
	List<String> generateManNames() {
		List<String> firstNames = personNamesReader.readManFirstNames();
		List<String> lastNames = personNamesReader.readLastNames();
		List<String> names = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < TEACHERS_AMOUNT / 2; i++) {
			int firstNameIndex = random.nextInt(FIRST_NAMES_MAN_AMOUNT);
			int lastNameIndex = random.nextInt(LAST_NAMES_AMOUNT);
			String name = firstNames.get(firstNameIndex) + " " + lastNames.get(lastNameIndex);
			names.add(name);
		}
		return names;
	}

	@SneakyThrows
	List<String> generateWomanNames() {
		List<String> firstNames = personNamesReader.readWomanFirstNames();
		List<String> lastNames = personNamesReader.readLastNames();
		List<String> names = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < TEACHERS_AMOUNT / 2; i++) {
			int firstNameIndex = random.nextInt(FIRST_NAMES_WOMAN_AMOUNT);
			int lastNameIndex = random.nextInt(LAST_NAMES_AMOUNT);
			String name = firstNames.get(firstNameIndex) + " " + lastNames.get(lastNameIndex);
			names.add(name);
		}
		return names;
	}

	@Override
	public Long countAll() {
		return teacherRepository.count();
	}

	@Override
	public List<Teacher> findAll() {
		return teacherRepository.findAll();
	}
}
