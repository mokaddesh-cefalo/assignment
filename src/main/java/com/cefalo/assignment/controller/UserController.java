package com.cefalo.assignment.controller;


import com.cefalo.assignment.model.orm.User;
import com.cefalo.assignment.service.business.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity postUser(@RequestBody User user){
        ResponseEntity responseEntity = null;

        try {
            User userCreated = userService.postUser(user);
            responseEntity = new ResponseEntity(userCreated, HttpStatus.CREATED);
        }catch (Exception e){
            responseEntity = new ResponseEntity(getRootThrowable(e).getMessage(), HttpStatus.BAD_REQUEST);
        }finally {
            return responseEntity;
        }
    }

    private Throwable getRootThrowable(Throwable e) {
        while (e.getCause() != null){
            e = e.getCause();
        }
        return e;
    }

}
