package com.erick;

public class CellOutOfBoundsException extends ArrayIndexOutOfBoundsException {
    private String message;

    public CellOutOfBoundsException() {
        message = "Cell is out of bounds for this grid";
    }

    public CellOutOfBoundsException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}