package ua.com.foxmineded.universitycms.utils;

public interface TeacherPhotoReader extends FileReader {
	byte[] readManPng();

	byte[] readWomanPng();
}
