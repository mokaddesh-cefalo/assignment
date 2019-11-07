package com.cefalo.assignment.service.business;

import com.cefalo.assignment.exception.DuplicationOfUniqueValueException;
import com.cefalo.assignment.model.orm.User;
import com.cefalo.assignment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public User postUser(User user) throws DuplicationOfUniqueValueException {

        if(userRepository.existsUserByUserName(user.getUserName()))
            throw new DuplicationOfUniqueValueException("User Name already exists");

        return userRepository.save(user);
    }

    @Override
    public User findUserByUserName(String userName) throws UsernameNotFoundException{
        Optional<User> userOptional = userRepository.findById(userName);

        if(!userOptional.isPresent()) throw new UsernameNotFoundException("User is not found : " + userName);
        return userOptional.get();
    }

}
