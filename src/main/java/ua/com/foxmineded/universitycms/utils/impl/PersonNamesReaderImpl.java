package ua.com.foxmineded.universitycms.utils.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ua.com.foxmineded.universitycms.utils.PersonNamesReader;

@Component
public class PersonNamesReaderImpl implements PersonNamesReader {
    @Value("classpath:/txt/man-names.txt")
    private Resource resourceManNames;
    @Value("classpath:/txt/woman-names.txt")
    private Resource resourceWomenNames;
    @Value("classpath:/txt/last-names.txt")
    private Resource resourceLastNames;

    @SneakyThrows
    @Override
    public List<String> readManFirstNames() {
        try (InputStream inputStream = resourceManNames.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            List<String> lines = reader.lines().collect(Collectors.toList());
            return lines;
        }
    }

    @SneakyThrows
    @Override
    public List<String> readWomanFirstNames() {
        try (InputStream inputStream = resourceWomenNames.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            List<String> lines = reader.lines().collect(Collectors.toList());
            return lines;
        }
    }

    @SneakyThrows
    @Override
    public List<String> readLastNames() {
        try (InputStream inputStream = resourceLastNames.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            List<String> lines = reader.lines().collect(Collectors.toList());
            return lines;
        }
    }
}
