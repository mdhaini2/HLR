package com.hlr.hlr.Utils;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Date;

public class Response {
    private int status;
    private String message;
    private Date time;
    private Object Data;


    public Response() {
    }

    public Response(int status, String message,long timeStamp, Object data) {
        this.status = status;
        this.message = message;
        setTime(timeStamp);
        Data = data;
    }
    public Response(String message, Object data) {
        this.status = HttpStatus.OK.value();
        this.message = message;
        setTime(System.currentTimeMillis());
        Data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(long timeStamp) {
        Timestamp ts =new Timestamp(timeStamp);
        Date date=new Date(ts.getTime());
        this.time = date;
    }

    public void setData(Object data) {
        Data = data;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Object getData() {
        return Data;
    }
}
