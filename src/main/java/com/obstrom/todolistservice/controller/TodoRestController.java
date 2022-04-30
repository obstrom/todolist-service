package com.obstrom.todolistservice.controller;

import com.obstrom.todolistservice.dto.TodoResponseDto;
import com.obstrom.todolistservice.service.TodoDtoService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping
public class TodoRestController {

    private final TodoDtoService todoDtoService;

    public TodoRestController(TodoDtoService todoDtoService) {
        this.todoDtoService = todoDtoService;
    }

    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> getAllTodos() {
        List<TodoResponseDto> responseDtos = todoDtoService.findAllTodosInSystem();

        return ResponseEntity.ok().body(responseDtos);
    }

    @GetMapping(path = "{userId}")
    public ResponseEntity<List<TodoResponseDto>> getAllTodosByUser(@PathVariable @NotBlank String userId) {
        List<TodoResponseDto> responseDtos = todoDtoService.findAllTodosByUser(userId);

        return ResponseEntity.ok().body(responseDtos);
    }

    @GetMapping(path = "{userId}/active")
    public ResponseEntity<List<TodoResponseDto>> getAllActiveTodosByUser(@PathVariable @NotBlank String userId) {
        List<TodoResponseDto> responseDtos = todoDtoService.findAllActiveTodosByUser(userId);

        return ResponseEntity.ok().body(responseDtos);
    }

    @GetMapping(path = "{userId}/{todoId}")
    public ResponseEntity<TodoResponseDto> getTodoByIdAndUser(
            @PathVariable @NotBlank String userId,
            @PathVariable @NotBlank String todoId) {
        TodoResponseDto responseDto = todoDtoService.findTodoById(userId, todoId);

        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping(path = "{userId}")
    public ResponseEntity<TodoResponseDto> createTodo(
            @PathVariable @NotBlank String userId,
            @RequestBody @NotBlank String message) {
        TodoResponseDto responseDto = todoDtoService.createTodo(userId, message);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping(path = "{userId}/{todoId/message}")
    public ResponseEntity<TodoResponseDto> updateTodoMessage(
            @PathVariable @NotBlank String userId,
            @PathVariable @NotBlank String todoId,
            @RequestBody @NotBlank String message) {
        TodoResponseDto responseDto = todoDtoService.updateTodoMessage(userId, todoId, message);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping(path = "{userId}/{todoId/completed}")
    public ResponseEntity<TodoResponseDto> updateTodoCompleted(
            @PathVariable @NotBlank String userId,
            @PathVariable @NotBlank String todoId,
            @RequestBody @NotNull Boolean isCompleted) {
        TodoResponseDto responseDto = todoDtoService.updateTodoCompletedStatus(userId, todoId, isCompleted);

        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping(path = "{userId}/{todoId}")
    public void deleteTodo(
            @PathVariable @NotBlank String userId,
            @PathVariable @NotBlank String todoId) {
        todoDtoService.deleteTodo(userId, todoId);
    }

}
