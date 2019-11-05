package com.cefalo.assignment.controller;

import com.cefalo.assignment.service.business.AuthService;
import com.cefalo.assignment.utils.AuthenticationRequest;
import com.cefalo.assignment.utils.ExceptionHandlerUtil;
import com.cefalo.assignment.utils.ResponseEntityCreation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authenticate")
public class JWTController {

    private static Logger logger = LogManager.getLogger("com");
    private final ExceptionHandlerUtil exceptionHandlerUtil;
    private final AuthService authService;
    private final ResponseEntityCreation responseEntityCreation;

    @Autowired
    public JWTController(AuthService authService,ResponseEntityCreation responseEntityCreation,
                         ExceptionHandlerUtil exceptionHandlerUtil){
        this.authService = authService;
        this.responseEntityCreation = responseEntityCreation;
        this.exceptionHandlerUtil = exceptionHandlerUtil;
    }

    @PostMapping
    public ResponseEntity<?> getAuthToken(@RequestBody AuthenticationRequest authenticationRequest) {
        System.out.println(authenticationRequest.toString());
        try {
            return responseEntityCreation.makeResponseEntity(
                    authService.createAuthToken(authenticationRequest), HttpStatus.OK);
        }catch (BadCredentialsException e) {
            logger.trace(exceptionHandlerUtil.getErrorString(e));

            return responseEntityCreation.makeResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
