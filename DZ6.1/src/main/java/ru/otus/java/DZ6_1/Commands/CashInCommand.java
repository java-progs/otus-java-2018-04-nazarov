package ru.otus.java.DZ6_1.Commands;

import ru.otus.java.DZ6_1.Devices.ATM;
import ru.otus.java.DZ6_1.Devices.CashInException;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CashInCommand extends Command {

    public CashInCommand(ATM atm) {
        super(atm);
    }

    @Override
    public boolean execute() {
        Scanner sc = new Scanner(System.in);

        System.out.println();
        System.out.println("===== Cash-in =====");
        System.out.println("Available denominations for cash deposits: ");
        String cashInState = atm.getCashInState();
        System.out.println(cashInState);
        if (cashInState.equals(atm.CASH_IN_NOT_AVAILABLE)) return false;

        Set<Integer> nominals = atm.getCashInNominals();
        HashMap<Integer, Integer> notes = new HashMap<>();

        System.out.println("Please, enter the number of notes: ");
        for (Integer nominal : nominals) {
            System.out.print(nominal + " : ");
            int count = sc.nextInt();
            notes.put(nominal, count);
        }

        System.out.println("Your deposit :");
        for (Map.Entry<Integer, Integer> pair : notes.entrySet()) {
            System.out.print(pair.getKey() + " : " + pair.getValue() + " notes. ");
        }
        System.out.println();


        try {
            atm.deposit(notes);
            System.out.println("Success deposit");
        } catch (CashInException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;

    }
}
