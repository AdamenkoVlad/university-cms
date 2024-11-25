package ua.com.foxmineded.universitycms.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.com.foxmineded.universitycms.entities.impl.Administrator;
import ua.com.foxmineded.universitycms.utils.AdministratorPhotoReader;
import ua.com.foxmineded.universitycms.utils.impl.AdministratorPhotoReaderImpl;

@SpringBootTest(classes = { ModelMapper.class, AdministratorPhotoReaderImpl.class })
class AdministratorDtoTest {
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	AdministratorPhotoReader administratorPhotoReader;

	@Test
	void testToMapEntityToDto() {
		Administrator administrator = Instancio.create(Administrator.class);
		AdministratorDto administratorDto = modelMapper.map(administrator, AdministratorDto.class);

		assertEquals(administrator.getId(), administratorDto.getId());
		assertEquals(administrator.getBirthDate(), administratorDto.getBirthDate());
		assertEquals(administrator.getEmail(), administratorDto.getEmail());
		assertEquals(administrator.getTelephoneNumber(), administratorDto.getTelephoneNumber());
		assertEquals(administrator.getResidenceAddress(), administratorDto.getResidenceAddress());
		assertEquals(administrator.getPassportNumber(), administratorDto.getPassportNumber());
		assertEquals(administrator.getName(), administratorDto.getName());
		assertEquals(administrator.getLogin(), administratorDto.getLogin());
		assertEquals(administrator.getPassword(), administratorDto.getPassword());
		assertEquals(administrator.getRole(), administratorDto.getRole());
		assertEquals(administrator.getSalaryAmount(), administratorDto.getSalaryAmount());
		assertEquals(administrator.getCurrencyMark(), administratorDto.getCurrencyMark());
		assertEquals(administrator.getEmploymentDate(), administratorDto.getEmploymentDate());
		assertEquals(administrator.getPosition(), administratorDto.getPosition());
		assertEquals(administrator.getWorkingShift(), administratorDto.getWorkingShift());

	}

	@Test
	void testToMapDtoToEntity() {
		AdministratorDto administratorDto = Instancio.create(AdministratorDto.class);
		Administrator administrator = modelMapper.map(administratorDto, Administrator.class);

		assertEquals(administratorDto.getId(), administrator.getId());
		assertEquals(administratorDto.getBirthDate(), administrator.getBirthDate());
		assertEquals(administratorDto.getEmail(), administrator.getEmail());
		assertEquals(administratorDto.getTelephoneNumber(), administrator.getTelephoneNumber());
		assertEquals(administratorDto.getResidenceAddress(), administrator.getResidenceAddress());
		assertEquals(administratorDto.getPassportNumber(), administrator.getPassportNumber());
		assertEquals(administratorDto.getName(), administrator.getName());
		assertEquals(administratorDto.getLogin(), administrator.getLogin());
		assertEquals(administratorDto.getPassword(), administrator.getPassword());
		assertEquals(administratorDto.getRole(), administrator.getRole());
		assertEquals(administratorDto.getSalaryAmount(), administrator.getSalaryAmount());
		assertEquals(administratorDto.getCurrencyMark(), administrator.getCurrencyMark());
		assertEquals(administratorDto.getEmploymentDate(), administrator.getEmploymentDate());
		assertEquals(administratorDto.getPosition(), administrator.getPosition());
		assertEquals(administratorDto.getWorkingShift(), administrator.getWorkingShift());
	}
}
