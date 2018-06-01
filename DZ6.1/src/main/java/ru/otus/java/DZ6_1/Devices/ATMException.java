package ru.otus.java.DZ6_1.Devices;

public class ATMException extends Exception {

    String message;

    public ATMException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
