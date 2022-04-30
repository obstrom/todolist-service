package com.obstrom.todolistservice.error;

import com.obstrom.todolistservice.error.exception.EntityNotFoundException;
import com.obstrom.todolistservice.error.exception.UniqueFieldConstraintException;
import io.lettuce.core.RedisConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class, UniqueFieldConstraintException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> onSimpleCustomException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(RedisConnectionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<String> onRedisConnectionException() {
        return ResponseEntity.internalServerError().body("Service response error - try again later or contact admin");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Map<String, String>> onConstraintViolationException(ConstraintViolationException e) {
        HashMap<String, String> violations = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> violations.put(
                violation.getPropertyPath().toString(),
                violation.getMessage()
        ));
        return ResponseEntity.badRequest().body(violations);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Map<String, String>> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HashMap<String, String> violations = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(violation -> violations.put(
                violation.getField(),
                violation.getDefaultMessage()
        ));
        return ResponseEntity.badRequest().body(violations);
    }

}
