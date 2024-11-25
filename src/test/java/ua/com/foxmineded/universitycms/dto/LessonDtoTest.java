package ua.com.foxmineded.universitycms.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.com.foxmineded.universitycms.entities.impl.Lesson;

@SpringBootTest(classes = ModelMapper.class)
class LessonDtoTest {
	@Autowired
	ModelMapper modelMapper;

	@Test
	void testMapEntityToDto() {
		Lesson lesson = Instancio.create(Lesson.class);
		LessonDto lessonDto = modelMapper.map(lesson, LessonDto.class);

		assertEquals(lesson.getId(), lessonDto.getId());
		assertEquals(lesson.getLessonDate(), lessonDto.getLessonDate());
		assertEquals(lesson.getLessonNumber(), lessonDto.getLessonNumber());
		assertEquals(lesson.getRoom().getId(), lessonDto.getRoomId());
		assertEquals(lesson.getCourse().getId(), lessonDto.getCourseId());
	}

	@Test
	void testMapDtoToEntity() {
		LessonDto lessonDto = Instancio.create(LessonDto.class);
		Lesson lesson = modelMapper.map(lessonDto, Lesson.class);

		assertEquals(lessonDto.getId(), lesson.getId());
		assertEquals(lessonDto.getLessonDate(), lesson.getLessonDate());
		assertEquals(lessonDto.getLessonNumber(), lesson.getLessonNumber());
		assertEquals(lessonDto.getRoomId(), lesson.getRoom().getId());
		assertEquals(lessonDto.getCourseId(), lesson.getCourse().getId());
	}
}
