package com.cefalo.assignment.utils;

import org.springframework.stereotype.Service;


public interface ExceptionHandlerUtil {
    String getRootThrowableMessage(Throwable e);
}
