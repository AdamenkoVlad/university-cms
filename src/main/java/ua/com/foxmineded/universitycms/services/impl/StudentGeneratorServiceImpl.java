package ua.com.foxmineded.universitycms.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import static java.util.stream.Collectors.toCollection;
import org.instancio.Instancio;
import static org.instancio.Select.field;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxmineded.universitycms.dao.AvatarRepository;
import ua.com.foxmineded.universitycms.dao.StudentRepository;
import ua.com.foxmineded.universitycms.entities.impl.Avatar;
import ua.com.foxmineded.universitycms.entities.impl.Group;
import ua.com.foxmineded.universitycms.entities.impl.Student;
import ua.com.foxmineded.universitycms.enums.Gender;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.enums.Specialization;
import ua.com.foxmineded.universitycms.services.StudentGeneratorService;
import ua.com.foxmineded.universitycms.utils.PersonNamesReader;
import ua.com.foxmineded.universitycms.utils.StudentPhotoReader;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentGeneratorServiceImpl implements StudentGeneratorService {
	private static final int STUDENTS_AMOUNT_IN_GROUP = 20;
	private static final int TOTAL_STUDENTS_AMOUNT = 800;
	private static final int FIRST_NAMES_MAN_AMOUNT = 40;
	private static final int FIRST_NAMES_WOMAN_AMOUNT = 40;
	private static final int LAST_NAMES_AMOUNT = 80;
	private final PasswordEncoder passwordEncoder;
	private final PersonNamesReader personNamesReader;
	private final StudentPhotoReader studentPhotoReader;
	private final StudentRepository studentRepository;
	private final AvatarRepository avatarRepository;

	@SneakyThrows
	@Override
	public List<Student> generateWithGroups(List<Group> groups) {
		log.info("The student generation process was started ");
		List<Student> students = createStudents();
		log.info("The students were generated");
		log.info("The allocation process student to group was started ");
		students = allocateToGroups(students, groups);
		return studentRepository.saveAll(students);
	}

	@SneakyThrows
	List<Student> createStudents(){
		Random random = new Random();
		List<Student> resultStudents = new ArrayList<>();
		List<String> manNames = generateManNames();
		List<String> womanNames = generateWomanNames();
		for (int i = 0; i < TOTAL_STUDENTS_AMOUNT / 2; i++) {
			Avatar avatar = new Avatar();
			avatar.setAvatarContents(studentPhotoReader.readManPng());
			avatar.setContentType("image/png");
			avatar.setRole(Role.STUDENT);
			UUID uuid = avatarRepository.save(avatar).getId();
			Student student = Instancio.of(Student.class).ignore(field(Student::getId)).ignore(field(Student::getGroup))
					.ignore(field(Student::getCourses))
					.set(field(Student::getEmail),
							manNames.get(i).toLowerCase().replace(" ", "") + String.valueOf(i + 1000) + "@gmail.com")
					.set(field(Student::getTelephoneNumber),
							"1" + String.valueOf(random.nextInt(100, 1000)) + String.valueOf(random.nextInt(100, 1000))
									+ String.valueOf(i + 1000))
					.set(field(Student::getName), manNames.get(i)).set(field(Student::getGender), Gender.M)
					.set(field(Student::getPassportNumber), "1000" + String.valueOf(i + 1000))
					.set(field(Student::getLogin), "userM" + String.valueOf(i)).set(field(Student::getPhotoUuid), uuid)
					.set(field(Student::getCurrencyMark), "USD")
					.set(field(Student::getScholarshipAmount), BigDecimal.valueOf(2500))
					.set(field(Student::getBirthDate), LocalDate.of(2000, 12, 25))
					.set(field(Student::getAdmissionDate), LocalDate.of(2000, 12, 25))
					.set(field(Student::getPassword), passwordEncoder.encode("1234")).create();
			resultStudents.add(student);
		}
		for (int i = 0; i < TOTAL_STUDENTS_AMOUNT / 2; i++) {
			Avatar avatar = new Avatar();
			avatar.setAvatarContents(studentPhotoReader.readWomanPng());
			avatar.setContentType("image/png");
			avatar.setRole(Role.STUDENT);
			UUID uuid = avatarRepository.save(avatar).getId();
			Student student = Instancio.of(Student.class).ignore(field(Student::getId)).ignore(field(Student::getGroup))
					.ignore(field(Student::getCourses))
					.set(field(Student::getEmail),
							womanNames.get(i).toLowerCase().replace(" ", "") + String.valueOf(i + 2000) + "@gmail.com")
					.set(field(Student::getTelephoneNumber),
							"1" + String.valueOf(random.nextInt(100, 1000)) + String.valueOf(random.nextInt(100, 1000))
									+ String.valueOf(i + 2000))
					.set(field(Student::getName), womanNames.get(i)).set(field(Student::getGender), Gender.W)
					.set(field(Student::getPassportNumber), "1000" + String.valueOf(i + 2000))
					.set(field(Student::getLogin), "userW" + String.valueOf(i)).set(field(Student::getPhotoUuid), uuid)
					.set(field(Student::getCurrencyMark), "USD")
					.set(field(Student::getScholarshipAmount), BigDecimal.valueOf(2500))
					.set(field(Student::getBirthDate), LocalDate.of(2000, 12, 25))
					.set(field(Student::getAdmissionDate), LocalDate.of(2000, 12, 25)).set(field(Student::getPassword),
							passwordEncoder.encode(String.valueOf(random.nextInt(100000) + 100000)))
					.create();
			resultStudents.add(student);
		}
		return studentRepository.saveAll(resultStudents);
	}

	@SneakyThrows
	List<String> generateManNames() {
		List<String> firstNames = personNamesReader.readManFirstNames();
		List<String> lastNames = personNamesReader.readLastNames();
		List<String> names = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < TOTAL_STUDENTS_AMOUNT / 2; i++) {
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
		for (int i = 0; i < TOTAL_STUDENTS_AMOUNT / 2; i++) {
			int firstNameIndex = random.nextInt(FIRST_NAMES_WOMAN_AMOUNT);
			int lastNameIndex = random.nextInt(LAST_NAMES_AMOUNT);
			String name = firstNames.get(firstNameIndex) + " " + lastNames.get(lastNameIndex);
			names.add(name);
		}
		return names;
	}

	List<Student> allocateToGroups(List<Student> students, List<Group> groups) {
		List<Group> artGroups = groups.stream().filter(group -> group.getSpecialization() == Specialization.ART)
				.collect(toCollection(ArrayList::new));
		List<Student> artStudents = students.stream()
				.filter(student -> student.getSpecialization() == Specialization.ART)
				.collect(toCollection(ArrayList::new));

		List<Group> computerScienceGroups = groups.stream()
				.filter(group -> group.getSpecialization() == Specialization.COMPUTER_SCIENCE)
				.collect(toCollection(ArrayList::new));
		List<Student> computerScienceStudents = students.stream()
				.filter(student -> student.getSpecialization() == Specialization.COMPUTER_SCIENCE)
				.collect(toCollection(ArrayList::new));

		List<Group> economicGroups = groups.stream()
				.filter(group -> group.getSpecialization() == Specialization.ECONOMICS)
				.collect(toCollection(ArrayList::new));
		List<Student> economicStudents = students.stream()
				.filter(student -> student.getSpecialization() == Specialization.ECONOMICS)
				.collect(toCollection(ArrayList::new));

		List<Group> medicineGroups = groups.stream()
				.filter(group -> group.getSpecialization() == Specialization.MEDICINE)
				.collect(toCollection(ArrayList::new));
		List<Student> medicineStudents = students.stream()
				.filter(student -> student.getSpecialization() == Specialization.MEDICINE)
				.collect(toCollection(ArrayList::new));

		int counterA = 0;
		for (int i = 0; i < artGroups.size(); i++) {
			for (int j = 0; j < STUDENTS_AMOUNT_IN_GROUP; j++) {
				artStudents.get(counterA).setGroup(artGroups.get(i));
				counterA++;
			}
		}

		int counterC = 0;
		for (int i = 0; i < computerScienceGroups.size(); i++) {
			for (int j = 0; j < STUDENTS_AMOUNT_IN_GROUP; j++) {
				computerScienceStudents.get(counterC).setGroup(computerScienceGroups.get(i));
				counterC++;
			}
		}

		int counterE = 0;
		for (int i = 0; i < economicGroups.size(); i++) {
			for (int j = 0; j < STUDENTS_AMOUNT_IN_GROUP; j++) {
				economicStudents.get(counterE).setGroup(economicGroups.get(i));
				counterE++;
			}
		}

		int counterM = 0;
		for (int i = 0; i < medicineGroups.size(); i++) {
			for (int j = 0; j < STUDENTS_AMOUNT_IN_GROUP; j++) {
				medicineStudents.get(counterM).setGroup(medicineGroups.get(i));
				counterM++;
			}
		}
		artStudents.addAll(computerScienceStudents);
		artStudents.addAll(economicStudents);
		artStudents.addAll(medicineStudents);
		return studentRepository.saveAll(artStudents);
	}

	@Override
	public Long countAll() {
		return studentRepository.count();
	}

	@Override
	public List<Student> findAll() {
		return studentRepository.findAll();
	}
}
