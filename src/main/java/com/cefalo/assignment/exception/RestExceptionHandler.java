package com.cefalo.assignment.exception;

import com.cefalo.assignment.utils.ResponseEntityCreation;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(basePackages = "com.cefalo.assignment")
@Slf4j
public class RestExceptionHandler {

    @Autowired
    ResponseEntityCreation responseEntityCreation;

    private BindingResult bindingResult;

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex) {

        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getMessage());

        return responseEntityCreation.buildResponseEntity(apiError);
    }

    @ExceptionHandler(UnAuthorizedRequestException.class)
    protected ResponseEntity<ApiError> handleUnAuthorizedRequest(UnAuthorizedRequestException ex) {

        ApiError apiError = new ApiError(UNAUTHORIZED);
        apiError.setMessage(ex.getMessage());

        return responseEntityCreation.buildResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidFormatException.class)
    protected ResponseEntity<ApiError> handleInvalidFormatException(InvalidFormatException ex) {

        ApiError apiError = new ApiError(BAD_REQUEST);
        int firstOccurance = ex.getLocalizedMessage().indexOf("at [Source:");

        apiError.setMessage(ex.getLocalizedMessage().substring(0, firstOccurance-1));
        return responseEntityCreation.buildResponseEntity(apiError);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException ex) {

        ApiError apiError = new ApiError(UNAUTHORIZED);
        apiError.setMessage(ex.getMessage());
        return responseEntityCreation.buildResponseEntity(apiError);
    }

    @ExceptionHandler(DuplicationOfUniqueValueException.class)
    protected ResponseEntity<ApiError> handleDuplicationOfUniqueValueException(DuplicationOfUniqueValueException ex) {

        ApiError apiError = new ApiError(UNPROCESSABLE_ENTITY);
        apiError.setMessage(ex.getMessage());
        return responseEntityCreation.buildResponseEntity(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException( MethodArgumentNotValidException error ) {

        ApiError apiError = new ApiError(UNPROCESSABLE_ENTITY);
        apiError.setMessage("Validation error!");
        BindingResult bindingResult = error.getBindingResult();

        bindingResult.getFieldErrors()
                .forEach(e -> apiError.addSubError(e.getField(), e.getDefaultMessage()));
        return responseEntityCreation.buildResponseEntity(apiError);
    }

}
