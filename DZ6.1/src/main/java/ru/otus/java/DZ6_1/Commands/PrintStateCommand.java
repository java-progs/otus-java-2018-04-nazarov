package ru.otus.java.DZ6_1.Commands;

import ru.otus.java.DZ6_1.Devices.ATM;

public class PrintStateCommand extends Command {

    public PrintStateCommand(ATM atm) {
        super(atm);
    }

    @Override
    public boolean execute() {
        atm.printState();
        return true;
    }
}
