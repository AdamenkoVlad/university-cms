package ua.com.foxmineded.universitycms.services.impl;

import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxmineded.universitycms.dao.RoomRepository;
import ua.com.foxmineded.universitycms.dto.RoomDto;
import ua.com.foxmineded.universitycms.entities.impl.Room;
import ua.com.foxmineded.universitycms.exceptions.ServiceDataIntegrityException;
import ua.com.foxmineded.universitycms.exceptions.ServiceException;
import ua.com.foxmineded.universitycms.services.RoomService;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
	private final ModelMapper modelMapper;
	private final RoomRepository roomRepository;

	@Override
	public Page<RoomDto> findAll(Pageable pageable) {
		return roomRepository.findAll(pageable).map(v -> modelMapper.map(v, RoomDto.class));
	}

	@Override
	public Page<RoomDto> findAllByFloor(int floor, Pageable pageable) {
		return roomRepository.findAllByFloor(floor, pageable).map(v -> modelMapper.map(v, RoomDto.class));
	}

	@Override
	public RoomDto findById(Long roomId) throws ServiceException {
		return roomRepository.findById(roomId).map(room -> modelMapper.map(room, RoomDto.class)).orElseThrow(() -> {
			String message = "The room with id = %d was not found".formatted(roomId);
			log.error(message);
			return new ServiceException(message);
		});
	}

	@Override
	public RoomDto findByRoomNumber(int roomNumber) throws ServiceException {
		return roomRepository.findByRoomNumber(roomNumber).map(room -> modelMapper.map(room, RoomDto.class))
				.orElseThrow(() -> {
					String message = "The room with number = %d was not found".formatted(roomNumber);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public RoomDto findByLessonId(Long lessonId) throws ServiceException {
		return roomRepository.findByLessonId(lessonId).map(room -> modelMapper.map(room, RoomDto.class))
				.orElseThrow(() -> {
					String message = "The room with lesson id = %d was not found".formatted(lessonId);
					log.error(message);
					return new ServiceException(message);
				});
	}

	@Override
	public RoomDto save(@Valid RoomDto roomDto) throws ServiceDataIntegrityException {
		ServiceDataIntegrityException serviceDataIntegrityException = new ServiceDataIntegrityException();
		roomRepository.findByRoomNumber(roomDto.getRoomNumber()).ifPresent(value -> {
			if (!Objects.equals(value.getId(), roomDto.getId())) {
				String message = "The room with number = %d already exists".formatted(roomDto.getRoomNumber());
				log.error(message);
				serviceDataIntegrityException.getExceptions()
						.add(new ServiceDataIntegrityException("roomNumber", message));
			}
		});
		if (!serviceDataIntegrityException.getExceptions().isEmpty()) {
			throw serviceDataIntegrityException;
		}
		RoomDto result = modelMapper.map(roomRepository.save(modelMapper.map(roomDto, Room.class)), RoomDto.class);
		String message = "The room with id = %d was saved".formatted(result.getId());
		log.info(message);
		return result;
	}

	@Override
	public void deleteById(Long roomId) throws ServiceException {
		roomRepository.findById(roomId).orElseThrow(() -> {
			String message = "The room with id = %d was not found".formatted(roomId);
			log.error(message);
			return new ServiceException(message);
		});
		roomRepository.deleteById(roomId);
		String message = "The room with id = %d was deleted".formatted(roomId);
		log.info(message);
	}
}
