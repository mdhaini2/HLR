package com.hlr.hlr.Exceptions;

public class ServiceAlreadyExistsException extends Exception{
    public ServiceAlreadyExistsException(String error){
        super(error);
    }
}
