package com.hlr.hlr.Exceptions;

public class TokenExpiredException extends Exception{
    public TokenExpiredException(String error){
        super(error);
    }
}
