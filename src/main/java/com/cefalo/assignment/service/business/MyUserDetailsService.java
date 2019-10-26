package com.cefalo.assignment.service.business;


import com.cefalo.assignment.model.orm.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
       Optional<User> currentUser = userService.findUserByUserName(s);

       if(!currentUser.isPresent()) throw new  UsernameNotFoundException("No such user : " + s);
       User user = currentUser.get();

       return new UserDetails() {
           @Override
           public Collection<? extends GrantedAuthority> getAuthorities() {
               return Arrays.stream(user.getRoles().split(","))
                       .map(SimpleGrantedAuthority::new)
                       .collect(Collectors.toList());
           }

           @Override
           public String getPassword() {
               return user.getPassword();
           }

           @Override
           public String getUsername() {
               return user.getUserName();
           }

           @Override
           public boolean isAccountNonExpired() {
               return true;
           }

           @Override
           public boolean isAccountNonLocked() {
               return true;
           }

           @Override
           public boolean isCredentialsNonExpired() {
               return true;
           }

           @Override
           public boolean isEnabled() {
               return user.getActive();
           }
       };
    }
}
