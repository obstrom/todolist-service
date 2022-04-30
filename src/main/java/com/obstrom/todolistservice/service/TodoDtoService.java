package com.obstrom.todolistservice.service;

import com.obstrom.todolistservice.dto.TodoResponseDto;
import com.obstrom.todolistservice.model.Todo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoDtoService {

    private final TodoService todoService;

    public TodoDtoService(TodoService todoService) {
        this.todoService = todoService;
    }

    public List<TodoResponseDto> findAllTodosInSystem() {
        return todoService.findAllTodosInSystem().stream()
                .map(this::mapTodoToResponseDto)
                .toList();
    }

    public List<TodoResponseDto> findAllTodosByUser(String userId) {
        return todoService.findAllTodosByUser(userId).stream()
                .map(this::mapTodoToResponseDto)
                .toList();
    }

    public TodoResponseDto findTodoById(String userId, String todoId) {
        return mapTodoToResponseDto(todoService.findTodoById(userId, todoId));
    }

    public List<TodoResponseDto> findAllActiveTodosByUser(String userId) {
        return todoService.findAllActiveTodosByUser(userId).stream()
                .map(this::mapTodoToResponseDto)
                .toList();
    }

    public TodoResponseDto createTodo(String userId, String message) {
        return mapTodoToResponseDto(todoService.createTodo(userId, message));
    }

    public TodoResponseDto updateTodoMessage(String userId, String todoId, String newMessage) {
        return mapTodoToResponseDto(todoService.updateTodoMessage(userId, todoId, newMessage));
    }

    public TodoResponseDto updateTodoCompletedStatus(String userId, String todoId, boolean isCompleted) {
        return mapTodoToResponseDto(todoService.updateTodoCompletedStatus(userId, todoId, isCompleted));
    }

    public void deleteTodo(String userId, String todoId) {
        todoService.deleteTodo(userId, todoId);
    }

    private TodoResponseDto mapTodoToResponseDto(Todo todo) {
        return new TodoResponseDto(
                todo.getId(),
                todo.getCreatedAt(),
                todo.getUserId(),
                todo.getMessage(),
                todo.getCompleted(),
                todo.getCreatedAt()
        );
    }

}
