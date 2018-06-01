package ru.otus.java.DZ6_1.Commands;

import ru.otus.java.DZ6_1.Devices.ATM;

public abstract class Command {
    ATM atm;

    public Command(ATM atm) {
        this.atm = atm;
    }

    public abstract boolean execute();

}
