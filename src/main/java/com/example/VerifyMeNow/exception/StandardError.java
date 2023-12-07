package com.example.VerifyMeNow.exception;
import org.springframework.http.HttpStatus;
import java.io.Serializable;
import java.util.Date;
public class StandardError implements Serializable{
    private static final long serialVersionUID = 1L;
    private HttpStatus httpStatus;
    private Date timestamp;
    private String message;

    public StandardError(int httpStatus, Date timestamp, String message) {
        this.httpStatus = HttpStatus.valueOf(httpStatus);
        this.timestamp = timestamp;
        this.message = message;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }



}
