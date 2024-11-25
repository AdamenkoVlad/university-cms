package ua.com.foxmineded.universitycms.utils.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import lombok.SneakyThrows;
import ua.com.foxmineded.universitycms.utils.TeacherPhotoReader;

@Component
public class TeacherPhotoReaderImpl implements TeacherPhotoReader {
    @Value("classpath:/static/photo/teacher-man.png")
    private Resource resourceManPhoto;
    @Value("classpath:/static/photo/teacher-woman.png")
    private Resource resourceWomanPhoto;

    @SneakyThrows
    @Override
    public byte[] readManPng() {
        return resourceManPhoto.getInputStream().readAllBytes();
    }

    @SneakyThrows
    @Override
    public byte[] readWomanPng() {
        return resourceWomanPhoto.getInputStream().readAllBytes();
    }
}
