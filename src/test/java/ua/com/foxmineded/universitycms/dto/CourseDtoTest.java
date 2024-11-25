package ua.com.foxmineded.universitycms.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.com.foxmineded.universitycms.entities.impl.Course;

@SpringBootTest(classes = ModelMapper.class)
class CourseDtoTest {
	@Autowired
	ModelMapper modelMapper;

	@Test
	void testMapEntityToDto() {
		Course course = Instancio.create(Course.class);
		CourseDto courseDto = modelMapper.map(course, CourseDto.class);

		assertEquals(course.getId(), courseDto.getId());
		assertEquals(course.getSpecialization(), courseDto.getSpecialization());
		assertEquals(course.getCourseName(), courseDto.getCourseName());
		assertEquals(course.getCreditHours(), courseDto.getCreditHours());
		assertEquals(course.getCourseDescription(), courseDto.getCourseDescription());
		assertEquals(course.getTeacher().getId(), courseDto.getTeacherId());
	}

	@Test
	void testMapDtoToEntity() {
		CourseDto courseDto = Instancio.create(CourseDto.class);
		Course course = modelMapper.map(courseDto, Course.class);

		assertEquals(courseDto.getId(), course.getId());
		assertEquals(courseDto.getSpecialization(), course.getSpecialization());
		assertEquals(courseDto.getCourseName(), course.getCourseName());
		assertEquals(courseDto.getCreditHours(), course.getCreditHours());
		assertEquals(courseDto.getCourseDescription(), course.getCourseDescription());
		assertEquals(courseDto.getTeacherId(), course.getTeacher().getId());
	}
}
