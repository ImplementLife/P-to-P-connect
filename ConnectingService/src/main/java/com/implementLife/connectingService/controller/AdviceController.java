package com.implementLife.connectingService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AdviceController {
    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> defaultErrorHandler(HttpServletRequest req, RuntimeException e) throws IllegalArgumentException {
        Map<String, Object> map = new HashMap<>();
        map.put("message", e.getMessage());
        map.put("path", req.getRequestURL());
        map.put("timestamp", new Date().toString());
        if (e instanceof IllegalArgumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else {
            throw e;
        }
    }
}
