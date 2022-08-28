package com.hlr.hlr.Utils;

import java.sql.Timestamp;
import java.util.Date;

public class ResponseError {
    private int status;
    private String message;
    private Date time;
    private StackTraceElement[]  stackTrace;

    public ResponseError() {
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

    public StackTraceElement[]  getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTraceElement[] stackTrace) {
        this.stackTrace = stackTrace;
    }
}
