package com.cefalo.assignment.utils;

import com.cefalo.assignment.exception.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseEntityCreationImpl implements ResponseEntityCreation {

    @Override
    public <T> ResponseEntity<?> buildResponseEntity(T t, HttpStatus httpStatus){
        return ResponseEntity.status(httpStatus).body(t);
    }

    @Override
    public ResponseEntity<?> buildResponseEntity(HttpStatus httpStatus){
        return ResponseEntity.status(httpStatus).body(null);
    }

    @Override
    public ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
