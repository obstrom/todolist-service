package com.obstrom.todolistservice.config;

import com.obstrom.todolistservice.model.User;
import com.obstrom.todolistservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.ObjectHashMapper;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Configuration
public class ApplicationConfig {

    @Value("${application.admin.username}")
    String defaultAdminUsername;

    @Value("${application.admin.password}")
    String defaultAdminPassword;

    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public ObjectHashMapper objectHashMapper() {
        return new ObjectHashMapper();
    }

    @Bean
    public CommandLineRunner setupDefaultAdminUser(UserRepository userRepository) {
        return (args) -> {

            if (defaultAdminUsername == null || defaultAdminPassword == null) {
                log.error("Could not read default login details from application properties", new Exception());
            }

            if (userRepository.findUserByUsername(defaultAdminUsername).isEmpty()) {
                User newAdminUser = User.builder()
                        .username(defaultAdminUsername)
                        .password(defaultAdminPassword)
                        .createdAt(new Date())
                        .build();

                        // TODO - Update this with roles when Spring Security is in place

                userRepository.save(newAdminUser);
                log.info("Default admin user created from properties");
            }

        };
    }

}
