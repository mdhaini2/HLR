package com.hlr.hlr.Exceptions;


import com.google.i18n.phonenumbers.NumberParseException;
import com.hlr.hlr.Utils.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler
    public ResponseEntity<ResponseError> handleException(Exception exception) {
        ResponseError errorObject = new ResponseError();
        errorObject.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setTime(System.currentTimeMillis());
        return new ResponseEntity<ResponseError>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseError> handlePhoneNumberInvalidException(PhoneNumberInvalidException exception) {
        ResponseError errorObject = new ResponseError();
        errorObject.setStatus(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setTime(System.currentTimeMillis());
        return new ResponseEntity<ResponseError>(errorObject, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public ResponseEntity<ResponseError> handleNumberParseException(NumberParseException exception) {
        ResponseError errorObject = new ResponseError();
        errorObject.setStatus(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setTime(System.currentTimeMillis());
        return new ResponseEntity<ResponseError>(errorObject, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public ResponseEntity<ResponseError> handleUsersNotFoundException(UsersNotFoundException exception) {
        ResponseError errorObject = new ResponseError();
        errorObject.setStatus(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setTime(System.currentTimeMillis());
        return new ResponseEntity<ResponseError>(errorObject, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler
    public ResponseEntity<ResponseError> handleSQLException(SQLException exception) {
        ResponseError errorObject = new ResponseError();
        errorObject.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setTime(System.currentTimeMillis());
        return new ResponseEntity<ResponseError>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @ExceptionHandler
    public ResponseEntity<ResponseError> handelCredentialsNotValidException(CredentialsNotValidException exception) {
        ResponseError errorObject = new ResponseError();
        errorObject.setStatus(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setTime(System.currentTimeMillis());
        return new ResponseEntity<ResponseError>(errorObject, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public ResponseEntity<ResponseError> handleServicesNotFoundException(ServicesNotFoundException exception) {
        ResponseError errorObject = new ResponseError();
        errorObject.setStatus(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setTime(System.currentTimeMillis());
        return new ResponseEntity<ResponseError>(errorObject, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler
    public ResponseEntity<ResponseError> handleUserAlreadySubscribedToServiceException(UserAlreadySubscribedToServiceException exception) {
        ResponseError errorObject = new ResponseError();
        errorObject.setStatus(HttpStatus.CONFLICT.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setTime(System.currentTimeMillis());
        return new ResponseEntity<ResponseError>(errorObject, HttpStatus.CONFLICT);

    }

    @ExceptionHandler
    public ResponseEntity<ResponseError> handleServiceAlreadyExists(ServiceAlreadyExistsException exception) {
        ResponseError errorObject = new ResponseError();
        errorObject.setStatus(HttpStatus.CONFLICT.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setTime(System.currentTimeMillis());
        return new ResponseEntity<ResponseError>(errorObject, HttpStatus.CONFLICT);

    }

    @ExceptionHandler
    public ResponseEntity<ResponseError> handleInsufficientAmountException(InsufficientAmountException exception) {
        ResponseError errorObject = new ResponseError();
        errorObject.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setTime(System.currentTimeMillis());
        return new ResponseEntity<ResponseError>(errorObject, HttpStatus.NOT_ACCEPTABLE);

    }

}
