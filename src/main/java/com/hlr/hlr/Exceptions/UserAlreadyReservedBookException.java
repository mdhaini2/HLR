package com.hlr.hlr.Exceptions;

public class UserAlreadyReservedBookException extends Exception{
    public UserAlreadyReservedBookException(String error){
        super(error);
    }
}
