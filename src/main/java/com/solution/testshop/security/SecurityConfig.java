package com.solution.testshop.security;

import com.solution.testshop.model.User;
import com.solution.testshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    AuthenticationSuccessHandler successHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()//добавил из за постмана
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/user-service/").access("hasRole('USER')")
                .antMatchers("/user-service/**").access("hasRole('USER')")
                .antMatchers("/admin-service/").access("hasRole('ADMIN')")
                .antMatchers("/admin-service/**").access("hasRole('ADMIN')")
                .and()
                .formLogin()
                .successHandler(successHandler)
                .and()
                .logout().clearAuthentication(true)
                .invalidateHttpSession(true)
                .and()
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByUsername(username);
            if (user != null)
                return user;
            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }
}
