package ua.com.foxmineded.universitycms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
	private final UserDetailsService jpaUserDetailsService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(
						authz -> authz.requestMatchers("/login/**", "/js/**", "/css/**", "/webjars/**", "/photo/**")
								.permitAll().requestMatchers("/administrators/**").hasRole("ADMINISTRATOR")
								.requestMatchers("/teachers/**").hasAnyRole("ADMINISTRATOR", "TEACHER")
								.requestMatchers("/students/**").hasAnyRole("ADMINISTRATOR", "TEACHER", "STUDENT")
								.requestMatchers(HttpMethod.POST).hasAnyRole("ADMINISTRATOR", "TEACHER")
								.requestMatchers(HttpMethod.GET).authenticated().anyRequest().denyAll())
				.userDetailsService(jpaUserDetailsService)
				.formLogin(formLogin -> formLogin.loginPage("/login").defaultSuccessUrl("/", true))
				.logout(Customizer.withDefaults()).csrf(Customizer.withDefaults()).build();
	}

	@Bean
	public AuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(jpaUserDetailsService);
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
