package com.cefalo.assignment.controller;


import com.cefalo.assignment.model.orm.User;
import com.cefalo.assignment.service.business.UserService;
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

    @GetMapping("/{userId}")
    public ResponseEntity getUser(@PathVariable String userId){
        Optional<User> user = userService.findUserByUserName(userId);
        return  (user.isPresent()) ? new ResponseEntity(user.get(), HttpStatus.OK)
                : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{userId}/stories")
    public ResponseEntity getUserStories(@PathVariable String userId){
        Optional<User> user = userService.findUserByUserName(userId);
        return  (user.isPresent()) ? new ResponseEntity(user.get().getStories(), HttpStatus.OK)
                : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    private Throwable getRootThrowable(Throwable e) {
        while (e.getCause() != null){
            e = e.getCause();
        }
        return e;
    }

}
