package com.cefalo.assignment.utils;

import com.cefalo.assignment.model.orm.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResponseEntityCreationImpl implements ResponseEntityCreation {

    @Override
    public <T> ResponseEntity makeResponseEntity(T t, HttpStatus httpStatus){
        return new ResponseEntity(t, httpStatus);
    }

    @Override
    public ResponseEntity makeResponseEntity(HttpStatus httpStatus){
        return new ResponseEntity(httpStatus);
    }

    @Override
    public ResponseEntity makeResponseEntity(Optional optional, HttpStatus inSuccess, HttpStatus inFailure){
        return  (optional.isPresent()) ? makeResponseEntity(optional.get(), inSuccess) : makeResponseEntity(inFailure);
    }

    @Override
    public <T extends User> ResponseEntity makeResponseEntityOfStoryListFromUser
            (Optional<T> optional, HttpStatus inSuccess, HttpStatus inFailure){
        return  (optional.isPresent()) ? makeResponseEntity(optional.get().getStories(), inSuccess) : makeResponseEntity(inFailure);
    }

}
