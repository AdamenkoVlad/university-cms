package ua.com.foxmineded.universitycms.controllers.impl;

import org.instancio.Instancio;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import ua.com.foxmineded.universitycms.dto.LessonDto;
import ua.com.foxmineded.universitycms.dto.StudentDto;
import ua.com.foxmineded.universitycms.dto.TeacherDto;
import ua.com.foxmineded.universitycms.dto.UserDto;
import ua.com.foxmineded.universitycms.enums.Role;
import ua.com.foxmineded.universitycms.services.LessonService;

@WithMockUser(username = "admin", password = "1234", roles = "ADMINISTRATOR")
@WebMvcTest({ TimetableControllerImpl.class })
class TimetableControllerImplTest {
	@Autowired
	MockMvc mockMvc;
	@MockBean
	LessonService lessonService;
	@MockBean
	UserDetailsService userDetailsService;

	@Test
	void testGetTimetableForStudent_AskGetTimetable_TimetableShouldBeReturned() throws Exception {
		StudentDto studentDto = Instancio.of(StudentDto.class).set(field(StudentDto::getRole), Role.STUDENT).create();
		List<LessonDto> lessons = Instancio.ofList(LessonDto.class).size(10).create();
		when(userDetailsService.loadUserByUsername(anyString())).thenReturn((UserDto) studentDto);
		when(lessonService.findAllByStudentId(anyLong())).thenReturn(lessons);

		MockHttpServletRequestBuilder requestBuilder = get("/timetable");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	void testGetTimetableForTeacher_AskGetTimetable_TimetableShouldBeReturned() throws Exception {
		TeacherDto teacherDto = Instancio.of(TeacherDto.class).set(field(TeacherDto::getRole), Role.STUDENT).create();
		List<LessonDto> lessons = Instancio.ofList(LessonDto.class).size(10).create();
		when(userDetailsService.loadUserByUsername(anyString())).thenReturn((UserDto) teacherDto);
		when(lessonService.findAllByStudentId(anyLong())).thenReturn(lessons);

		MockHttpServletRequestBuilder requestBuilder = get("/timetable");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}
}
