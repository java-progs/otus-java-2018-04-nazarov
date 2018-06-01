package ru.otus.java.DZ6_1.Devices;

public class WithdrawalException extends ATMException {

    public WithdrawalException(String message) {
        super(message);
        this.message = message;
    }
}
