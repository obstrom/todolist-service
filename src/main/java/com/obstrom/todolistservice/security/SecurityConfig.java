package com.obstrom.todolistservice.security;

import com.obstrom.todolistservice.model.User;
import com.obstrom.todolistservice.repository.UserRepository;
import com.obstrom.todolistservice.service.UserDetailsService;
import com.obstrom.todolistservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${application.admin.username}")
    String defaultAdminUsername;

    @Value("${application.admin.password}")
    String defaultAdminPassword;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner setupDefaultAdminUser(UserRepository userRepository) {
        return (args) -> {

            if (defaultAdminUsername == null || defaultAdminPassword == null) {
                log.error("Could not read default login details from application properties", new Exception());
            }

            if (userRepository.findUserByUsername(defaultAdminUsername).isEmpty()) {
                User newAdminUser = User.builder()
                        .id(null)
                        .createdAt(new Date())
                        .username(defaultAdminUsername)
                        .password(passwordEncoder().encode(defaultAdminPassword))
                        .role(UserRole.ROLE_ADMIN)
                        .build();

                userRepository.save(newAdminUser);
                log.info("Default admin user created from properties");
            }

        };
    }

}
