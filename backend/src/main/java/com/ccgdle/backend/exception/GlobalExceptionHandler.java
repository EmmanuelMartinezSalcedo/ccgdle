package com.ccgdle.backend.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.ccgdle.backend.exception.attempt.MaximumAttemptsExceededException;
import com.ccgdle.backend.exception.game.GameModeNotFoundException;
import com.ccgdle.backend.exception.game.GameNotFoundException;
import com.ccgdle.backend.exception.game.UserGameAlreadyExistsException;
import com.ccgdle.backend.exception.game.UserGameNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final String ERROR_KEY = "error";

  @ExceptionHandler(MaximumAttemptsExceededException.class)
  public ResponseEntity<Map<String, String>> handleMaximumAttemptsExceeded(Exception ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(ERROR_KEY, ex.getMessage()));
  }

  @ExceptionHandler(GameNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleGameNotFound(Exception ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_KEY, ex.getMessage()));
  }

  @ExceptionHandler(GameModeNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleGameModeNotFound(Exception ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_KEY, ex.getMessage()));
  }

  @ExceptionHandler(UserGameNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleUserGameNotFound(Exception ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_KEY, ex.getMessage()));
  }

  @ExceptionHandler(UserGameAlreadyExistsException.class)
  public ResponseEntity<Map<String, String>> handleUserGameAlreadyExists(Exception ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(ERROR_KEY, ex.getMessage()));
  }
}
