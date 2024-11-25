package ua.com.foxmineded.universitycms.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxmineded.universitycms.dao.GroupRepository;
import ua.com.foxmineded.universitycms.entities.impl.Group;
import ua.com.foxmineded.universitycms.enums.Specialization;
import ua.com.foxmineded.universitycms.services.GroupGeneratorService;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupGeneratorServiceImpl implements GroupGeneratorService {
	private Random random = new Random();
	private final GroupRepository groupRepository;

	@Override
	public List<Group> generate() {
		log.info("The group generation process was started");
		List<Group> groups = new ArrayList<>();
		List<String> names = generateFinalName();
		int i = 0;
		for (; i < 7; i++) {
			groups.add(new Group(null, names.get(i), Specialization.ART, null));
		}
		for (; i < 14; i++) {
			groups.add(new Group(null, names.get(i), Specialization.COMPUTER_SCIENCE, null));
		}
		for (; i < 21; i++) {
			groups.add(new Group(null, names.get(i), Specialization.ECONOMICS, null));
		}
		for (; i < 27; i++) {
			groups.add(new Group(null, names.get(i), Specialization.MEDICINE, null));
		}
		return groupRepository.saveAll(groups);
	}

	List<String> generateFinalName() {
		Set<String> groups = new HashSet<>();
		char char0, char1, char2, char3;
		while (groups.size() < 27) {
			char0 = returnRandomAlphabeticCharacter();
			char1 = returnRandomAlphabeticCharacter();
			char2 = returnRandomNumericCharacter();
			char3 = returnRandomNumericCharacter();
			char[] arr = { char0, char1, '-', char2, char3 };
			groups.add(new String(arr));
		}
		return new ArrayList<>(groups);
	}

	Character returnRandomAlphabeticCharacter() {
		return (char) (65 + random.nextInt(26));
	}

	Character returnRandomNumericCharacter() {
		return (char) (48 + random.nextInt(10));
	}

	@Override
	public Long countAll() {
		return groupRepository.count();
	}

	@Override
	public List<Group> findAll() {
		return groupRepository.findAll();
	}
}
