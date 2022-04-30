package com.obstrom.todolistservice.repository;

import com.obstrom.todolistservice.model.Todo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.ObjectHashMapper;

import java.util.List;

@Slf4j
public class TodoRepositoryImpl implements TodoRepositoryCustom {

    private final RedisTemplate<String, String> stringRedisTemplate;
    private final ObjectHashMapper objectHashMapper;

    public TodoRepositoryImpl(RedisTemplate<String, String> stringRedisTemplate, ObjectHashMapper objectHashMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectHashMapper = objectHashMapper;
    }

    @Override
    public List<Todo> findAllByUserId(String userId) {
        // TODO
        return List.of();
    }

    @Override
    public List<Todo> findAllByUserIdAndIsActive(String userId) {
        // TODO
        return List.of();
    }

}
