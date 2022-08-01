package com.dynamic.search.jpa.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {


    @ExceptionHandler(NoSuchFieldError.class)
    public ResponseEntity<Object> handleNoSuchFieldError(NoSuchFieldError e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}
