package com.cefalo.assignment.utils;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Service
public class ExceptionHandlerUtilImpl implements ExceptionHandlerUtil {

    @Override
    public String getRootThrowableMessage(Throwable e) {
        while (e.getCause() != null){
            e = e.getCause();
        }
        return e.getMessage();
    }

    @Override
    public String getErrorString(Exception ex) {
        String ret = "";
        try( StringWriter errors = new StringWriter();
             PrintWriter printWriter = new PrintWriter(errors)) {

            ex.printStackTrace(printWriter);
            ret = errors.toString();

        }catch (IOException e) {
            ret = ret + e.getMessage() + e.fillInStackTrace();
        }
        return ret;
    }
}
