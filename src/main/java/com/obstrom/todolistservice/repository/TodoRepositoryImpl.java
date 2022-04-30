package com.obstrom.todolistservice.repository;

import com.obstrom.todolistservice.model.Todo;
import io.lettuce.core.RedisConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.ObjectHashMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
        RedisConnection redisConnection;

        // Null-check RedisConnection
        try {
            redisConnection = Objects.requireNonNull(stringRedisTemplate.getConnectionFactory()).getConnection();
        } catch (NullPointerException e) {
            log.error("Error connecting to Redis");
            throw new RedisConnectionException("Connection is null");
        }

        // Get keys from set of all todos for user
        String query = String.format("todo:userId:%s", userId);
        Set<String> foundTodoKeys = stringRedisTemplate.opsForSet().members(query);

        // Stop and return empty if nothing is found
        if (foundTodoKeys == null || foundTodoKeys.isEmpty()) return List.of();

        // Retrieve each entry as byte hashmap, convert with object mapper, map to list
        return foundTodoKeys.stream()
                .map(key -> {
                    String hashQuery = String.format("todo:%s", key);
                    Map<byte[], byte[]> resultsByteMap = redisConnection.hGetAll(hashQuery.getBytes());
                    if (resultsByteMap == null) return null;
                    return objectHashMapper.fromHash(resultsByteMap, Todo.class);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Todo> findAllByUserIdAndIsActive(String userId) {
        return findAllByUserId(userId).stream()
                .filter(todo -> todo.getCompleted() != null && !todo.getCompleted())
                .collect(Collectors.toList());
    }

}
