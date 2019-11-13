package com.cefalo.assignment.service.business;

import com.cefalo.assignment.security.AuthenticationRequest;
import com.cefalo.assignment.security.AuthenticationResponse;
import com.cefalo.assignment.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final @Qualifier("myUserDetailsService") UserDetailsService userDetailsService;
    private final JwtUtil jwtTokenUtil;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtTokenUtil,
                           @Qualifier("myUserDetailsService") UserDetailsService userDetailsService){

        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public AuthenticationResponse createAuthToken(AuthenticationRequest authenticationRequest) throws BadCredentialsException{
        UserDetails userDetails = userDetailsService.loadUserByUsername(
                authenticationRequest.getUserName()
        );

        if(!userDetails.getPassword().equals(authenticationRequest.getPassword()))
            throw new BadCredentialsException("Password does not match!");

        Optional<String> jwt = Optional.ofNullable(jwtTokenUtil.generateToken(userDetails));
        return jwt.map(AuthenticationResponse::new).get();
    }
}
