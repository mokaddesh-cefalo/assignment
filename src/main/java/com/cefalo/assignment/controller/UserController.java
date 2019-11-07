package com.cefalo.assignment.controller;


import com.cefalo.assignment.exception.DuplicationOfUniqueValueException;
import com.cefalo.assignment.exception.EntityNotFoundException;
import com.cefalo.assignment.model.orm.User;
import com.cefalo.assignment.service.business.UserService;
import com.cefalo.assignment.utils.ExceptionHandlerUtil;
import com.cefalo.assignment.utils.ResponseEntityCreation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static Logger logger = LogManager.getLogger("com");
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
    public ResponseEntity<?>  postUser(@RequestBody @Valid User user) throws MethodArgumentNotValidException, DuplicationOfUniqueValueException {
        return responseEntityCreation
                .buildResponseEntity(userService.postUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/{user-name}")
    public ResponseEntity<?>  getUser(@PathVariable(value ="user-name" ) String userName) throws EntityNotFoundException {
        return  responseEntityCreation
                .buildResponseEntity(userService.findUserByUserName(userName), HttpStatus.OK);
    }

    @GetMapping("/{user-name}/stories")
    public ResponseEntity<?>  getUserStories(@PathVariable(value ="user-name" ) String userName) throws EntityNotFoundException{
        User user = userService.findUserByUserName(userName);

        return responseEntityCreation
                .buildResponseEntity(user.getStories(), HttpStatus.OK);
    }

}
