package ua.com.foxmineded.universitycms.utils.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import lombok.SneakyThrows;
import ua.com.foxmineded.universitycms.utils.DefaultPersonPhotoReader;

@Component
public class DefaultPersonPhotoReaderImpl implements DefaultPersonPhotoReader {
    @Value("classpath:/static/photo/default-person-photo.png")
    private Resource personPhoto;

    @SneakyThrows
    public byte[] readPng() {
        return personPhoto.getInputStream().readAllBytes();
    }
}
