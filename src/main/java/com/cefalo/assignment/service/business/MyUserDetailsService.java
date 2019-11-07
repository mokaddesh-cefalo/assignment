package com.cefalo.assignment.service.business;


import com.cefalo.assignment.model.orm.User;
import com.cefalo.assignment.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public MyUserDetailsService(UserService userService){
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
       User currentUser = userService.findUserByUserName(s);
       return Optional.ofNullable(currentUser).map(MyUserDetails::new).get();
    }
}
