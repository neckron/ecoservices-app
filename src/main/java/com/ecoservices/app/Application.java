package com.ecoservices.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.ecoservices.app.model.Role;
import com.ecoservices.app.security.repository.RoleRepository;


@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableMongoRepositories("com.ecoservices.app")

public class Application {

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(RoleRepository roleRepository) {

        return args -> {

            Role adminRole = roleRepository.findByRole(ADMIN);
            if (adminRole == null) {
                Role newAdminRole = new Role();
                newAdminRole.setRole(ADMIN);
                roleRepository.save(newAdminRole);
            }

            Role userRole = roleRepository.findByRole(USER);
            if (userRole == null) {
                Role newUserRole = new Role();
                newUserRole.setRole(USER);
                roleRepository.save(newUserRole);
            }
        };

    }

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setProtocol("SMTP");
        javaMailSender.setHost("127.0.0.1");
        javaMailSender.setPort(25);
        return javaMailSender;
    }

}
