package com.cefalo.assignment.service.business;

import com.cefalo.assignment.security.AuthenticationRequest;
import com.cefalo.assignment.security.AuthenticationResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    AuthenticationResponse createAuthToken(@RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException;
}
