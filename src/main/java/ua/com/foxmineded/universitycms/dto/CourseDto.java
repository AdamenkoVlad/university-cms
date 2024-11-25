package ua.com.foxmineded.universitycms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.com.foxmineded.universitycms.enums.Specialization;

@Data
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder(setterPrefix = "with")
public class CourseDto extends AbstractDto<Long> {
	private static final long serialVersionUID = 1L;
	private Specialization specialization;
	@NotBlank(message = "Course name cannot be blank")
	private String courseName;
	@PositiveOrZero(message = "Credit hours cannot be negative")
	private Integer creditHours;
	private String courseDescription;
	private Long teacherId;

	@Builder(setterPrefix = "with")
	public CourseDto(Long id, Specialization specialization, String courseName, Integer creditHours,
			String courseDescription, Long teacherId) {
		super(id);
		this.specialization = specialization;
		this.courseName = courseName;
		this.creditHours = creditHours;
		this.courseDescription = courseDescription;
		this.teacherId = teacherId;
	}

	public CourseDto() {
		super();
	}
}
