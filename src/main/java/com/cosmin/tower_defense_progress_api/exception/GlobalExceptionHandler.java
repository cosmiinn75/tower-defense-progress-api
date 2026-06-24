package com.cosmin.tower_defense_progress_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExistingUsernameException.class)
    public ResponseEntity<Map<String,String>> existingUsernameHandler(ExistingUsernameException exc) {

        Map<String,String> response = new HashMap<>();
        response.put("error" , "Conflict");
        response.put("message" , exc.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String,String>> invalidCredentialsHandler(InvalidCredentialsException exc) {

        Map<String,String> response = new HashMap<>();
        response.put("error" , "Unauthorized");
        response.put("message" , exc.getMessage());

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(InvalidLevelException.class)
    public ResponseEntity<Map<String,String>> invalidLevelHandler(InvalidLevelException exc) {

        Map<String,String> response = new HashMap<>();
        response.put("error" , "Bad request");
        response.put("message" , exc.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(LevelLockedException.class)
    public ResponseEntity<Map<String,String>> levelLockedHandler(LevelLockedException exc) {

        Map<String,String> response = new HashMap<>();
        response.put("error" , "Forbidden");
        response.put("message" , exc.getMessage());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(LevelProgressNotFoundException.class)
    public ResponseEntity<Map<String,String>> levelProgressNotFoundHandler(LevelProgressNotFoundException exc) {

        Map<String,String> response = new HashMap<>();
        response.put("error" , "Not found");
        response.put("message" , exc.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PlayerProgressNotFoundException.class)
    public ResponseEntity<Map<String,String>> playerProgressNotFoundHandler(PlayerProgressNotFoundException exc) {

        Map<String,String> response = new HashMap<>();
        response.put("error" , "Not found");
        response.put("message" , exc.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> validationHandler(MethodArgumentNotValidException exc){
        Map<String,String> response = new HashMap<>();

        for(FieldError fieldError : exc.getBindingResult().getFieldErrors()) {
            response.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

}
