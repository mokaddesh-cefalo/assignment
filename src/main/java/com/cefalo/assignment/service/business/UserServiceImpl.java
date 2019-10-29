package com.cefalo.assignment.service.business;

import com.cefalo.assignment.model.orm.User;
import com.cefalo.assignment.service.orm.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public boolean existsUserByUserName(String userName){
        return userRepository.existsUserByUserName(userName);
    }

    @Override
    public User postUser(User user) throws Exception{
        if(existsUserByUserName(user.getUserName())) throw new Exception("User Name already exists");
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUserByUserName(String userName){
        return userRepository.findById(userName);
    }

}
