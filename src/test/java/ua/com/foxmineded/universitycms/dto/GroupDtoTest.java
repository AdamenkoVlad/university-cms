package ua.com.foxmineded.universitycms.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ua.com.foxmineded.universitycms.config.TypeMapConfig;
import ua.com.foxmineded.universitycms.entities.impl.Group;

@SpringBootTest(classes = TypeMapConfig.class)
class GroupDtoTest {
	@Autowired
	ModelMapper modelMapper;

	@Test
	void testMapEntityToDto() {
		Group group = Instancio.create(Group.class);
		GroupDto groupDto = modelMapper.map(group, GroupDto.class);
		assertEquals(group.getId(), groupDto.getId());
		assertEquals(group.getGroupName(), groupDto.getGroupName());
		assertEquals(group.getSpecialization(), groupDto.getSpecialization());
	}

	@Test
	void testMapDtoToEntity() {
		GroupDto groupDto = Instancio.create(GroupDto.class);
		Group group = modelMapper.map(groupDto, Group.class);
		assertEquals(groupDto.getId(), group.getId());
		assertEquals(groupDto.getGroupName(), group.getGroupName());
		assertEquals(groupDto.getSpecialization(), group.getSpecialization());
	}
}
