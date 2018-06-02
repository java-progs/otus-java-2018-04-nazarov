package ru.otus.java.DZ7_1.Devices;

public class WithdrawalException extends ATMException {

    public WithdrawalException(String message) {
        super(message);
        this.message = message;
    }
}
