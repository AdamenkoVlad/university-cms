package ua.com.foxmineded.universitycms.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.com.foxmineded.universitycms.dto.LessonDto;
import ua.com.foxmineded.universitycms.entities.impl.Lesson;

@Configuration
public class TypeMapConfig {
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		PropertyMap<LessonDto, Lesson> lessonToEntityPropertyMap = new PropertyMap<>() {
			@Override
			protected void configure() {
				skip(destination.getRoom());
				skip(destination.getCourse());
			}
		};

		modelMapper.addMappings(lessonToEntityPropertyMap);
		return new ModelMapper();
	}
}
