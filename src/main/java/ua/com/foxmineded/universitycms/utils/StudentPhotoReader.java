package ua.com.foxmineded.universitycms.utils;

public interface StudentPhotoReader extends FileReader {
	byte[] readManPng();

	byte[] readWomanPng();
}
