package com.cefalo.assignment.service.business;

import com.cefalo.assignment.model.orm.User;
import com.cefalo.assignment.service.orm.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public User postUser(User user) throws Exception{
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUserByUserName(String userName){
        return userRepository.findUserByUserName(userName);
    }

}
