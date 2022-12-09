package com.example.multichar.advices;

public class CharSheetGenerateException extends RuntimeException{
    public CharSheetGenerateException() {
    }

    public CharSheetGenerateException(String message) {
        super(message);
    }

    public CharSheetGenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CharSheetGenerateException(Throwable cause) {
        super(cause);
    }

    public CharSheetGenerateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
