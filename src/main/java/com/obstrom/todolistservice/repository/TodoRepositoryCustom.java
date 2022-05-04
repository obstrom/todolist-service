package com.obstrom.todolistservice.repository;

import com.obstrom.todolistservice.model.Todo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepositoryCustom {
    List<Todo> findAllByUserId(String userId);
    List<Todo> findAllByUserIdAndIsActive(String userId);
    List<Todo> findAllByUserIdAndIsCompleted(String userId);
}
