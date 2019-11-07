package com.cefalo.assignment.utils;

import com.cefalo.assignment.exception.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface ResponseEntityCreation {
    <T> ResponseEntity buildResponseEntity(T t, HttpStatus httpStatus);
    ResponseEntity buildResponseEntity(HttpStatus httpStatus);
    ResponseEntity<ApiError> buildResponseEntity(ApiError apiError);
}
