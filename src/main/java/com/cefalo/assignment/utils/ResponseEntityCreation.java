package com.cefalo.assignment.utils;

import com.cefalo.assignment.model.orm.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface ResponseEntityCreation {
    <T> ResponseEntity makeResponseEntity(T t, HttpStatus httpStatus);
    ResponseEntity makeResponseEntity(HttpStatus httpStatus);
    ResponseEntity makeResponseEntity(Optional optional, HttpStatus inSuccess, HttpStatus inFailure);
    <T extends User> ResponseEntity makeResponseEntityOfStoryListFromUser(Optional<T> optional, HttpStatus inSuccess, HttpStatus inFailure);
}
