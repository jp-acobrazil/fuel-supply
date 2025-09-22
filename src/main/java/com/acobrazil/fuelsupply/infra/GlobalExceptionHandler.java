package com.acobrazil.fuelsupply.infra;

import com.acobrazil.fuelsupply.models.exceptions.DriverNotFoundException;
import com.acobrazil.fuelsupply.models.exceptions.VehicleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentials(DriverNotFoundException ex) {
        return getMapResponseEntity(ex.getMessage(), ex);
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentials(VehicleNotFoundException ex) {
        return getMapResponseEntity(ex.getMessage(), ex);
    }

    private ResponseEntity<Map<String, Object>> getMapResponseEntity(String message, Object ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Not Found");
        body.put("message", message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
