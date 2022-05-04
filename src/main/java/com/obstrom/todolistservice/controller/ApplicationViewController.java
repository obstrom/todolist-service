package com.obstrom.todolistservice.controller;

import com.obstrom.todolistservice.dto.TodoResponseDto;
import com.obstrom.todolistservice.dto.UserRequestDto;
import com.obstrom.todolistservice.dto.UserResponseDto;
import com.obstrom.todolistservice.error.exception.UniqueFieldConstraintException;
import com.obstrom.todolistservice.model.User;
import com.obstrom.todolistservice.security.UserRole;
import com.obstrom.todolistservice.service.TodoDtoService;
import com.obstrom.todolistservice.service.UserDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class ApplicationViewController {

    private final UserDtoService userDtoService;
    private final TodoDtoService todoDtoService;

    @Autowired
    public ApplicationViewController(UserDtoService userDtoService, TodoDtoService todoDtoService) {
        this.userDtoService = userDtoService;
        this.todoDtoService = todoDtoService;
    }

    @GetMapping
    public String getIndexPage(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();

            if (user.getRole() == UserRole.ROLE_ADMIN) {
                List<UserResponseDto> users = userDtoService.findAllUsers();

                model.addAttribute("users", users);
            }

            if (user.getRole() == UserRole.ROLE_USER) {
                List<TodoResponseDto> activeTodosByUser = todoDtoService.findAllActiveTodosByUserSortedByCreationDate(user.getId());
                List<TodoResponseDto> completedTodosByUser = todoDtoService.findAllCompletedTodosByUserSortedByCompletionDate(user.getId());

                model.addAttribute("todosActive", activeTodosByUser);
                model.addAttribute("todosCompleted", completedTodosByUser);
            }
        }

        return "index.html";
    }

    @GetMapping("login")
    public String getLoginPage() {
        return "login.html";
    }

    @GetMapping("register")
    public String getRegisterPage(Model model) {
        model.addAttribute("user", new UserRequestDto(null, null));
        return "register.html";
    }

    @PostMapping("login")
    public String postLogin() {
        return "index.html";
    }

    @PostMapping("register")
    public String postRegister(@ModelAttribute UserRequestDto user, Model model) {
        try {
            userDtoService.createNewUser(user);
        } catch (UniqueFieldConstraintException e) {
            return "redirect:/register?error=Username+is+already+taken!";
        }

        model.addAttribute("username", user.username());
        return "redirect:/login?register=true";
    }

    @PostMapping("todo/new")
    public String postNewTodo(
            Authentication authentication,
            @RequestParam String todoMessage) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (todoMessage.isBlank())
                return "redirect:/?error=+Task+cant+be+empty!";

            User user = (User) authentication.getPrincipal();
            todoDtoService.createTodo(user.getId(), todoMessage);
        }

        return "redirect:/";
    }

    @PostMapping("todo/complete/{todoId}")
    public String completeTodo(
            Authentication authentication,
            @PathVariable String todoId) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (todoId.isBlank())
                return "redirect:/?error=+Todo+ID+is+missing!";

            User user = (User) authentication.getPrincipal();
            todoDtoService.updateTodoCompletedStatus(user.getId(), todoId, true);
        }

        return "redirect:/";
    }

    @PostMapping("todo/reactivate/{todoId}")
    public String reactivateTodo(
            Authentication authentication,
            @PathVariable String todoId) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (todoId.isBlank())
                return "redirect:/?error=+Todo+ID+is+missing!";

            User user = (User) authentication.getPrincipal();
            todoDtoService.updateTodoCompletedStatus(user.getId(), todoId, false);
        }

        return "redirect:/";
    }

    @PostMapping("todo/delete/{todoId}")
    public String deleteTodo(
            Authentication authentication,
            @PathVariable String todoId) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (todoId.isBlank())
                return "redirect:/?error=+Todo+ID+is+missing!";

            User user = (User) authentication.getPrincipal();
            todoDtoService.deleteTodo(user.getId(), todoId);
        }

        return "redirect:/";
    }
}
