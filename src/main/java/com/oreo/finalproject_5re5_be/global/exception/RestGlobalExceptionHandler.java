package com.oreo.finalproject_5re5_be.global.exception;

import com.oreo.finalproject_5re5_be.global.dto.response.ResponseDto;
import com.oreo.finalproject_5re5_be.project.exception.InvalidProjectNameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

@RestControllerAdvice
public class RestGlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDto<String>> handleMethodException(DataNotFoundException ex) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage()).toResponseEntity();
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ResponseDto<String>> handleDataNotFoundException(DataNotFoundException ex) {
        return new ResponseDto<>(HttpStatus.NOT_FOUND.value(), ex.getMessage()).toResponseEntity();
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ResponseDto<String>> handleMultipartException(MultipartException ex) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage()).toResponseEntity();
    }

    @ExceptionHandler(InvalidProjectNameException.class)
    public ResponseEntity<String> handleInvalidProjectNameException(InvalidProjectNameException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
