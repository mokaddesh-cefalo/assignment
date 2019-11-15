package com.cefalo.assignment.exception;

import com.cefalo.assignment.utils.ExceptionHandlerUtil;
import com.cefalo.assignment.utils.ResponseEntityCreation;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(ResponseEntityCreation.class);

    @Autowired
    ExceptionHandlerUtil exceptionHandlerUtil;

    @Autowired
    ResponseEntityCreation responseEntityCreation;

    private BindingResult bindingResult;

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex) {
        log.trace(ex.getMessage());

        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getMessage());

        return responseEntityCreation.buildResponseEntity(apiError);
    }

    @ExceptionHandler(UnAuthorizedRequestException.class)
    protected ResponseEntity<ApiError> handleUnAuthorizedRequest(UnAuthorizedRequestException ex) {

        log.error(exceptionHandlerUtil.getErrorString(ex));

        ApiError apiError = new ApiError(UNAUTHORIZED);
        apiError.setMessage(ex.getMessage());

        return responseEntityCreation.buildResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidFormatException.class)
    protected ResponseEntity<ApiError> handleInvalidFormatException(InvalidFormatException ex) {

        log.error(exceptionHandlerUtil.getErrorString(ex));

        ApiError apiError = new ApiError(BAD_REQUEST);
        int firstOccurance = ex.getLocalizedMessage().indexOf("at [Source:");

        apiError.setMessage(ex.getLocalizedMessage().substring(0, firstOccurance-1));
        return responseEntityCreation.buildResponseEntity(apiError);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException ex) {

        log.error(exceptionHandlerUtil.getErrorString(ex));

        ApiError apiError = new ApiError(UNAUTHORIZED);
        apiError.setMessage(ex.getMessage());
        return responseEntityCreation.buildResponseEntity(apiError);
    }

    @ExceptionHandler(DuplicationOfUniqueValueException.class)
    protected ResponseEntity<ApiError> handleDuplicationOfUniqueValueException(DuplicationOfUniqueValueException ex) {

        log.error(exceptionHandlerUtil.getErrorString(ex));

        ApiError apiError = new ApiError(UNPROCESSABLE_ENTITY);
        apiError.setMessage(ex.getMessage());
        return responseEntityCreation.buildResponseEntity(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException( MethodArgumentNotValidException ex ) {

        log.error(exceptionHandlerUtil.getErrorString(ex));

        ApiError apiError = new ApiError(UNPROCESSABLE_ENTITY);
        apiError.setMessage("Validation error!");
        BindingResult bindingResult = ex.getBindingResult();

        bindingResult.getFieldErrors()
                .forEach(e -> apiError.addSubError(e.getField(), e.getDefaultMessage()));
        return responseEntityCreation.buildResponseEntity(apiError);
    }

}
