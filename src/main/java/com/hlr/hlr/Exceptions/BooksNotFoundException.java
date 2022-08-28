package com.hlr.hlr.Exceptions;

public class BooksNotFoundException extends Exception{
    public BooksNotFoundException(String error){
        super(error);
    }
}
