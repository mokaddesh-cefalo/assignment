package com.cefalo.assignment.controller;

import com.cefalo.assignment.service.business.AuthService;
import com.cefalo.assignment.utils.AuthenticationRequest;
import com.cefalo.assignment.utils.ResponseEntityCreation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authenticate")
public class JWTController {
    private final AuthService authService;
    private final ResponseEntityCreation responseEntityCreation;

    @Autowired
    public JWTController(AuthService authService,ResponseEntityCreation responseEntityCreation){
        this.authService = authService;
        this.responseEntityCreation = responseEntityCreation;
    }

    @PostMapping
    public ResponseEntity<?> getAuthToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            return responseEntityCreation.makeResponseEntity(
                    authService.createAuthToken(authenticationRequest), HttpStatus.OK);
        }catch (BadCredentialsException e) {
            return responseEntityCreation.makeResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
