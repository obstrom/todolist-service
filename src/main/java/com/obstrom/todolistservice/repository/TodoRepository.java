package com.obstrom.todolistservice.repository;

import com.obstrom.todolistservice.model.Todo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends CrudRepository<Todo, String>, TodoRepositoryCustom {
}
