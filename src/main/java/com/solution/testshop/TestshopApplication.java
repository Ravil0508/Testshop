package com.solution.testshop;

import com.solution.testshop.model.User;
import com.solution.testshop.repository.UserRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication()
@OpenAPIDefinition(
        info = @Info(
                title = "TestShop API",
                version = "1.0.0",
                description = "Мини интернет-магазин",
                contact = @Contact(email = "ganievrav@rambler.ru", name = "Ravil Ganiev")))
@SecurityScheme(name = "/v3/api-docs", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class TestshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestshopApplication.class, args);
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner dataLoader(UserRepository userRepo) {
        return args -> {
            userRepo.save(new User(1l, "Admin", passwordEncoder.encode("admin"), User.Role.ROLE_ADMIN));
            userRepo.save(new User(2l, "User", passwordEncoder.encode("user"), User.Role.ROLE_USER));
            userRepo.save(new User(3l, "User2", passwordEncoder.encode("user2"), User.Role.ROLE_USER));
        };
    }
}
