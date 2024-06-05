package com.capstone.bidmarkit.exception;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 예제 10.11
@RestControllerAdvice //(basePackages = "com.springboot.valid_exception")
public class CustomExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleException(RuntimeException e,
                                                               HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", e.getMessage());
        e.printStackTrace();

        return new ResponseEntity<>(map, responseHeaders, httpStatus);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleException(MethodArgumentNotValidException e,
                                                               HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", e.getMessage());
        e.printStackTrace();

        return new ResponseEntity<>(map, responseHeaders, httpStatus);
    }
}
