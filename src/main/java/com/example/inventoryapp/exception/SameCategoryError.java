package com.example.inventoryapp.exception;

/**
 * Used for catching an exception.
 */

public class SameCategoryError extends RuntimeException {

    public SameCategoryError() {
    }

    public SameCategoryError(String message) {
        super(message);
    }

    public SameCategoryError(String message, Throwable cause) {
        super(message, cause);
    }

    public SameCategoryError(Throwable cause) {
        super(cause);
    }

    public SameCategoryError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }





}
