package com.cefalo.assignment.service.business;

import com.cefalo.assignment.utils.AuthenticationRequest;
import com.cefalo.assignment.utils.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    AuthenticationResponse createAuthToken(@RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException;
}
