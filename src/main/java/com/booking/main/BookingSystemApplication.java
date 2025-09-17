package com.booking.main;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.booking.entity.User;
import com.booking.enums.Role;
import com.booking.repository.UserRepository;

@SpringBootApplication
@ComponentScan(basePackages = "com.booking")
@EnableJpaRepositories(basePackages = "com.booking.repository")
@EntityScan(basePackages = "com.booking.entity")
public class BookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingSystemApplication.class, args);
	}

	  // Seed admin and user
    @Bean
    CommandLineRunner runner(UserRepository userRepo, BCryptPasswordEncoder encoder) {
        return args -> {
            if (userRepo.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole(Role.ROLE_ADMIN);
                admin.setEnabled(true);
                userRepo.save(admin);
            }
            if (userRepo.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(encoder.encode("user123"));
                user.setRole(Role.ROLE_USER); 
                user.setEnabled(true);
                userRepo.save(user);
            }
        };
    }
}
