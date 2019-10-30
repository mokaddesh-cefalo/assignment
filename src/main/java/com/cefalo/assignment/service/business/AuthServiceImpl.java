package com.cefalo.assignment.service.business;

import com.cefalo.assignment.utils.AuthenticationRequest;
import com.cefalo.assignment.utils.AuthenticationResponse;
import com.cefalo.assignment.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
    public AuthenticationResponse createAuthToken(@RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException{
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                        authenticationRequest.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(
                authenticationRequest.getUsername()
        );

        Optional<String> jwt = Optional.ofNullable(jwtTokenUtil.generateToken(userDetails));
        return jwt.map(AuthenticationResponse::new).get();
    }
}
