package com.cefalo.assignment.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExceptionHandlerUtilImpl implements ExceptionHandlerUtil {

    public String getRootThrowableMessage(Throwable e) {
        while (e.getCause() != null){
            e = e.getCause();
        }
        return e.getMessage();
    }
}
