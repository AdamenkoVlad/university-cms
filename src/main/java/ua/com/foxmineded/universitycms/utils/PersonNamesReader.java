package ua.com.foxmineded.universitycms.utils;

import java.io.IOException;
import java.util.List;

public interface PersonNamesReader extends FileReader {
	List<String> readManFirstNames() throws IOException;

	List<String> readWomanFirstNames() throws IOException;

	List<String> readLastNames() throws IOException;
}
