package ua.com.foxmineded.universitycms.utils.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.core.io.Resource;
import lombok.SneakyThrows;
import ua.com.foxmineded.universitycms.utils.AdministratorPhotoReader;

@Component
public class AdministratorPhotoReaderImpl implements AdministratorPhotoReader {
    @Value("classpath:static/photo/administrator.png")
    private Resource resource;

    @SneakyThrows
    @Override
    public byte[] readPng() {
        return resource.getInputStream().readAllBytes();
    }
}
