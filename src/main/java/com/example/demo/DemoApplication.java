package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.model.enums.UserType;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class DemoApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public DemoApplication(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {

        if (userRepository.count() == 0) {
            User user = User.builder()
                    .email("admin")
                    .name("admin")
                    .password(passwordEncoder.encode("admin"))
                    .userType(UserType.ADMIN)
                    .build();
            userRepository.save(user);
        }
    }

}
