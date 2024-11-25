package ua.com.foxmineded.universitycms.utils;

import java.io.IOException;
import java.util.List;

public interface CoursesNamesReader extends FileReader {
	List<String> readEconomicCourses() throws IOException;

	List<String> readComputerScienceCourses() throws IOException;

	List<String> readMedicineCourses() throws IOException;

	List<String> readArtCourses() throws IOException;
}
