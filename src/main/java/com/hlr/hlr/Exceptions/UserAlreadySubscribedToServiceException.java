package com.hlr.hlr.Exceptions;

public class UserAlreadySubscribedToServiceException extends Exception{
    public UserAlreadySubscribedToServiceException(String error){
        super(error);
    }
}
