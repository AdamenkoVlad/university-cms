package ua.com.foxmineded.universitycms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication  
@EnableScheduling
public class UniversityCmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniversityCmsApplication.class, args);
	}
}
