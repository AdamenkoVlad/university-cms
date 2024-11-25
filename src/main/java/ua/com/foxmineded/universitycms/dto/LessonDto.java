package ua.com.foxmineded.universitycms.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder(setterPrefix = "with")
public class LessonDto extends AbstractDto<Long> {
	private static final long serialVersionUID = 1L;
	@FutureOrPresent(message = "Lesson date cannot be in the past")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate lessonDate;
	@Positive(message = "The lesson number must be positive")
	private Integer lessonNumber;
	private Integer roomNumber;
	private Long roomId;
	private String courseName;
	private Long courseId;

	@Builder(setterPrefix = "with")
	public LessonDto(Long id, LocalDate lessonDate, Integer lessonNumber, Integer roomNumber, Long roomId,
			String courseName, Long courseId) {
		super(id);
		this.lessonDate = lessonDate;
		this.lessonNumber = lessonNumber;
		this.roomNumber = roomNumber;
		this.roomId = roomId;
		this.courseName = courseName;
		this.courseId = courseId;
	}

	public LessonDto() {
	}
}
