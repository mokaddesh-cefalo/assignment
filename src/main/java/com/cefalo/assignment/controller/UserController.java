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
    private final UserService userService;
    private final ExceptionHandlerUtil exceptionHandlerUtil;
    private final ResponseEntityCreation responseEntityCreation;

    @Autowired
    public UserController(UserService userService, ExceptionHandlerUtil exceptionHandlerUtil,
                          ResponseEntityCreation responseEntityCreation){
        this.userService = userService;
        this.exceptionHandlerUtil = exceptionHandlerUtil;
        this.responseEntityCreation = responseEntityCreation;
    }

    @PostMapping
    public ResponseEntity<?>  postUser(@RequestBody User user){
        try {
            return responseEntityCreation
                    .makeResponseEntity(userService.postUser(user), HttpStatus.CREATED);
        }catch (Exception e){
            return responseEntityCreation
                    .makeResponseEntity(exceptionHandlerUtil.getRootThrowableMessage(e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{user-name}")
    public ResponseEntity<?>  getUser(@PathVariable(value ="user-name" ) String userName){
        return  responseEntityCreation
                .makeResponseEntity(userService.findUserByUserName(userName), HttpStatus.OK, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{user-name}/stories")
    public ResponseEntity<?>  getUserStories(@PathVariable(value ="user-name" ) String userName){
        Optional<User> user = userService.findUserByUserName(userName);
        return responseEntityCreation
                .makeResponseEntityOfStoryListFromUser(user, HttpStatus.OK, HttpStatus.NOT_FOUND);
    }

}
