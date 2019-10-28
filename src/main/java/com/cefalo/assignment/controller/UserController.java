package com.cefalo.assignment.controller;


import com.cefalo.assignment.model.orm.User;
import com.cefalo.assignment.service.business.UserService;
import com.cefalo.assignment.utils.ExceptionHandlerUtil;
import com.cefalo.assignment.utils.ResponseEntityCreation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ExceptionHandlerUtil exceptionHandlerUtil;

    @Autowired
    ResponseEntityCreation responseEntityCreation;

    @PostMapping
    public ResponseEntity postUser(@RequestBody User user){
        try {
            return responseEntityCreation
                    .makeResponseEntity(userService.postUser(user), HttpStatus.CREATED);
        }catch (Exception e){
            return responseEntityCreation
                    .makeResponseEntity(exceptionHandlerUtil.getRootThrowableMessage(e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userName}")
    public ResponseEntity getUser(@PathVariable String userName){
        return  responseEntityCreation
                .makeResponseEntity(userService.findUserByUserName(userName), HttpStatus.OK, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{userName}/stories")
    public ResponseEntity getUserStories(@PathVariable String userName){
        Optional<User> user = userService.findUserByUserName(userName);
        return responseEntityCreation
                .makeResponseEntityOfStoryListFromUser(user, HttpStatus.OK, HttpStatus.NOT_FOUND);
    }

}
