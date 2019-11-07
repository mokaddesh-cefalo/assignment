package com.cefalo.assignment.service.business;

import com.cefalo.assignment.exception.DuplicationOfUniqueValueException;
import com.cefalo.assignment.exception.EntityNotFoundException;
import com.cefalo.assignment.model.orm.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserService {
    User postUser(User user) throws DuplicationOfUniqueValueException;
    User findUserByUserName(String userName) throws UsernameNotFoundException;
}
