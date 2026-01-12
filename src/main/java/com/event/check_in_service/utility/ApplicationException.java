package com.event.check_in_service.utility;

public class ApplicationException extends RuntimeException{

    public ApplicationException(String message, Throwable err) {
        super(message, err);
    }
}
