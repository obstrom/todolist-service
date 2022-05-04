package com.obstrom.todolistservice.controller;

import com.obstrom.todolistservice.dto.TodoResponseDto;
import com.obstrom.todolistservice.model.User;
import com.obstrom.todolistservice.service.TodoDtoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping("api/v1/todo")
public class TodoRestController {

    private final TodoDtoService todoDtoService;

    public TodoRestController(TodoDtoService todoDtoService) {
        this.todoDtoService = todoDtoService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<TodoResponseDto>> getAllTodos(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<TodoResponseDto> responseDtos = todoDtoService.findAllTodosByUser(user.getId());

        return ResponseEntity.ok().body(responseDtos);
    }

    @GetMapping(path = "active")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<TodoResponseDto>> getAllActiveTodos(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<TodoResponseDto> responseDtos = todoDtoService.findAllActiveTodosByUserSortedByCreationDate(user.getId());

        return ResponseEntity.ok().body(responseDtos);
    }

    @GetMapping(path = "completed")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<TodoResponseDto>> getAllCompletedTodos(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<TodoResponseDto> responseDtos = todoDtoService.findAllCompletedTodosByUserSortedByCompletionDate(user.getId());

        return ResponseEntity.ok().body(responseDtos);
    }

    @GetMapping(path = "id/{todoId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TodoResponseDto> getTodoById(
            @PathVariable @NotBlank String todoId,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        TodoResponseDto responseDto = todoDtoService.findTodoById(user.getId(), todoId);

        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TodoResponseDto> createTodo(
            @RequestBody @NotBlank String message,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        TodoResponseDto responseDto = todoDtoService.createTodo(user.getId(), message);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping(path = "{todoId}/message")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TodoResponseDto> updateTodoMessage(
            @PathVariable @NotBlank String todoId,
            @RequestBody @NotBlank String message,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        TodoResponseDto responseDto = todoDtoService.updateTodoMessage(user.getId(), todoId, message);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping(path = "{todoId}/completed")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TodoResponseDto> updateTodoCompleted(
            @PathVariable @NotBlank String todoId,
            @RequestBody @NotNull Boolean isCompleted,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        TodoResponseDto responseDto = todoDtoService.updateTodoCompletedStatus(user.getId(), todoId, isCompleted);

        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping(path = "{todoId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteTodo(
            @PathVariable @NotBlank String todoId,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        todoDtoService.deleteTodo(user.getId(), todoId);
    }

}
