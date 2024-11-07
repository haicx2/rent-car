package org.example.rentcar.exception;

import org.example.rentcar.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.sql.SQLException;

@ControllerAdvice
public class GlobalHandlingException {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse(ex.getMessage(),null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(ex.getMessage(),null));
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<APIResponse> handleUserAlreadyExistException(AlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new APIResponse(ex.getMessage(),null));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<APIResponse> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new APIResponse(ex.getMessage(),null));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<APIResponse> handleIllegalStateException(SQLException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(ex.getMessage(),null));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<APIResponse> handleIOException(IOException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(ex.getMessage(),null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new APIResponse(ex.getMessage(),null));
    }
}
