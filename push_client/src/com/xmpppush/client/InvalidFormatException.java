package com.xmpppush.client;

public class InvalidFormatException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidFormatException() {
        super();
    }

    public InvalidFormatException(String message) {
        super(message);
    }

    public InvalidFormatException(Throwable cause) {
        super(cause);
    }

    public InvalidFormatException(String message, Throwable cause) {
        super(message, cause);
    }

}
