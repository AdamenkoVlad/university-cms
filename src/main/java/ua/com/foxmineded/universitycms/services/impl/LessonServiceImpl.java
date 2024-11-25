package ua.com.foxmineded.universitycms.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toCollection;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxmineded.universitycms.dao.LessonRepository;
import ua.com.foxmineded.universitycms.dao.RoomRepository;
import ua.com.foxmineded.universitycms.dto.LessonDto;
import ua.com.foxmineded.universitycms.entities.impl.Lesson;
import ua.com.foxmineded.universitycms.entities.impl.Room;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.LessonService;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final ModelMapper modelMapper;
    private final LessonRepository lessonRepository;
    private final RoomRepository roomRepository;

    @Override
    public Page<LessonDto> findAll(Pageable pageable) {
        return lessonRepository.findAll(pageable).map(v -> modelMapper.map(v, LessonDto.class));
    }

    @Override
    public Page<LessonDto> findAllByLessonDate(LocalDate lessonDate, Pageable pageable) {
        return lessonRepository.findAllByLessonDate(lessonDate, pageable).map(v -> modelMapper.map(v, LessonDto.class));
    }

    @Override
    public Page<LessonDto> findAllByCourseId(Long courseId, Pageable pageable) {
        return lessonRepository.findAllByCourseId(courseId, pageable).map(v -> modelMapper.map(v, LessonDto.class));
    }

    @Override
    public Page<LessonDto> findAllByCourseName(String courseName, Pageable pageable) {
        return lessonRepository.findAllByCourseName(courseName, pageable).map(v -> modelMapper.map(v, LessonDto.class));
    }

    @Override
    public Page<LessonDto> findAllByRoomId(Long roomId, Pageable pageable) {
        return lessonRepository.findAllByRoomId(roomId, pageable).map(v -> modelMapper.map(v, LessonDto.class));
    }

    @Override
    public List<LessonDto> findAllByStudentId(Long studentId) {
        return lessonRepository.findAllByStudentId(studentId).stream().map(v -> modelMapper.map(v, LessonDto.class))
                .collect(toCollection(ArrayList::new));
    }

    @Override
    public List<LessonDto> findAllByTeacherId(Long teacherId) {
        return lessonRepository.findAllByTeacherId(teacherId).stream().map(v -> modelMapper.map(v, LessonDto.class))
                .collect(toCollection(ArrayList::new));
    }

    @Override
    public LessonDto findById(Long lessonId) throws ServiceException {
        return lessonRepository.findById(lessonId).map(lesson -> modelMapper.map(lesson, LessonDto.class))
                .orElseThrow(() -> {
                    String message = "The lesson with id = %d was not found".formatted(lessonId);
                    log.error(message);
                    return new ServiceException(message);
                });
    }

    @Override
    public void addRoomById(Long lessonId, Long roomId) throws ServiceException {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> {
            String message = "The lesson with id = %d was not found".formatted(lessonId);
            log.error(message);
            return new ServiceException(message);
        });
        Room room = roomRepository.findById(roomId).orElseThrow(() -> {
            String message = "The room with id = %d was not found".formatted(roomId);
            log.error(message);
            return new ServiceException(message);
        });
        if (Objects.nonNull(lesson.getRoom())) {
            String message = "The lesson already has a room";
            log.error(message);
            throw new ServiceException(message);
        }
        lesson.setRoom(room);
        String message = "The room with id = %d was added to lesson with id = %d".formatted(roomId, lessonId);
        log.info(message);
    }

    @Override
    public LessonDto save(@Valid LessonDto lessonDto) throws ServiceException {
        LessonDto result = modelMapper.map(lessonRepository.save(modelMapper.map(lessonDto, Lesson.class)),
                LessonDto.class);
        String message = "The lesson with id = %d was saved".formatted(result.getId());
        log.info(message);
        return result;
    }

    @Override
    public void deleteById(Long lessonId) throws ServiceException {
        lessonRepository.findById(lessonId).orElseThrow(() -> {
            String message = "The lesson with id = %d was not found".formatted(lessonId);
            log.error(message);
            return new ServiceException(message);
        });
        lessonRepository.deleteById(lessonId);
        String message = "The lesson with id = %d was deleted".formatted(lessonId);
        log.info(message);
    }
}
