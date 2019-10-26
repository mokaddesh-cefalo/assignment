package com.cefalo.assignment.service.business;

import com.cefalo.assignment.model.orm.User;

import java.util.Optional;

public interface UserService {
    User postUser(User user) throws Exception;
    Optional<User> findUserByUserName(String userName);
    boolean existsUserByUserName(String userName);
}
