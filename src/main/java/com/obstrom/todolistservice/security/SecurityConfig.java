package com.obstrom.todolistservice.security;

import com.obstrom.todolistservice.model.User;
import com.obstrom.todolistservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${application.admin.username}")
    String defaultAdminUsername;

    @Value("${application.admin.password}")
    String defaultAdminPassword;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/", true)
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/")
                .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").permitAll().anyRequest().authenticated()
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
