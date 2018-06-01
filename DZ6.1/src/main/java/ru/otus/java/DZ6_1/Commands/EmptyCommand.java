package ru.otus.java.DZ6_1.Commands;

import ru.otus.java.DZ6_1.Devices.ATM;

public class EmptyCommand extends Command {

    public EmptyCommand(ATM atm) {
        super(atm);
    }

    @Override
    public boolean execute() {
       return false;
    }
}
