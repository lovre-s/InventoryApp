package com.example.inventoryapp.exception;

public class SameItemError extends Exception{


    public SameItemError() {
    }

    public SameItemError(String message) {
        super(message);
    }

    public SameItemError(String message, Throwable cause) {
        super(message, cause);
    }

    public SameItemError(Throwable cause) {
        super(cause);
    }

    public SameItemError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
