package com.order.service.exception;

public class DuplicateOrderException extends RuntimeException{

    public DuplicateOrderException(String message) {
        super(message);
    }
}
