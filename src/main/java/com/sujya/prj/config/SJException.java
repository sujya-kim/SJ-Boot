package com.sujya.prj.config;

import net.bytebuddy.implementation.bytecode.Throw;

public class SJException extends RuntimeException{

    private int statusCode;

    public SJException(String message){
        super(message);
    }
    public SJException(Throwable cause, int statusCode){
        super(cause);
        this.statusCode = statusCode;
    }
    public SJException(String message, int statusCode){
        super(message);
        this.statusCode = statusCode;
    }
    public SJException(String message, Throwable cause, int statusCode){
        super(message, cause);
        this.statusCode = statusCode;
    }
    public int getStatusCode() {
        return statusCode;
    }

}
