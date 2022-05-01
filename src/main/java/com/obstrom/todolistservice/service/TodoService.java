package com.obstrom.todolistservice.service;

import com.obstrom.todolistservice.error.exception.EntityNotFoundException;
import com.obstrom.todolistservice.model.Todo;
import com.obstrom.todolistservice.repository.TodoRepository;
import com.obstrom.todolistservice.utility.AppUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TodoService {

    TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAllTodosByUser(String userId) {
        Iterable<Todo> iterable = todoRepository.findAllByUserId(userId);
        return AppUtility.convertIterableToList(iterable);
    }

    public Todo findTodoById(String userId, String todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No todo with ID '%s' found", todoId)));
        validateTodoBelongsToUser(userId, todoId, todo);

        return todo;
    }

    public List<Todo> findAllActiveTodosByUser(String userId) {
        return todoRepository.findAllByUserIdAndIsActive(userId);
    }

    public Todo createTodo(String userId, String message) {
        Todo todo = Todo.builder()
                .id(null) // generate in database
                .userId(userId)
                .createdAt(new Date())
                .message(message)
                .completed(false)
                .build();

        return todoRepository.save(todo);
    }

    public Todo updateTodoMessage(String userId, String todoId, String newMessage) {
        Todo todo = findTodoById(userId, todoId);
        todo.setMessage(newMessage);

        return todoRepository.save(todo);
    }

    public Todo updateTodoCompletedStatus(String userId, String todoId, boolean isCompleted) {
        Todo todo = findTodoById(userId, todoId);

        if (isCompleted) {
            todo.setCompleted(true);
            todo.setCompletedAt(new Date());
        } else {
            todo.setCompleted(false);
            todo.setCompletedAt(null);
        }

        return todoRepository.save(todo);
    }

    public void deleteTodo(String userId, String todoId) {
        todoRepository.delete(findTodoById(userId, todoId));
    }

    private void validateTodoBelongsToUser(String userId, String todoId, Todo todo) {
        // TODO - Change exception to Spring Security based, throw Forbidden
        if (!todo.getUserId().equals(userId))
            throw new IllegalStateException(String.format("Todo with ID '%s' belongs to another user", todoId));
    }

}
