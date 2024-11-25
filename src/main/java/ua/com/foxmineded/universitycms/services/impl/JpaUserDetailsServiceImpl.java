package ua.com.foxmineded.universitycms.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxmineded.universitycms.dao.UserRepository;
import ua.com.foxmineded.universitycms.dto.UserDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class JpaUserDetailsServiceImpl implements UserDetailsService {
	private final ModelMapper modelMapper;
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		return userRepository.findByLogin(login).map(value -> modelMapper.map(value, UserDto.class)).orElseThrow(() -> {
			String message = "The user with login %s was not found".formatted(login);
			log.error(message);
			return new UsernameNotFoundException(message);
		});
	}
}
