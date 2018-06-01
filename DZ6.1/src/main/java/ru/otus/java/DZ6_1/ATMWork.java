package ru.otus.java.DZ6_1;

import ru.otus.java.DZ6_1.Commands.*;
import ru.otus.java.DZ6_1.Devices.ATM;

import java.util.*;

/**
 * Created by operator on 24.05.2018.
 */
public class ATMWork{

    // Запуск java -classpath java -classpath ./target/classes ru.otus.java.DZ6_1.ATMWork

    public static void main(String args[]) {

        ATM atm = new ATM();
        Command command = new PrintStateCommand(atm);

        executeCommand(command);

        Scanner sc = new Scanner(System.in);
        boolean work = true;

        while (work) {
            System.out.println();
            System.out.println("===== Main menu =====");
            System.out.println("Pleas, select action:");
            System.out.println("1: Withdrawal");
            System.out.println("2: Deposit");
            System.out.println("3: ATM Balance");
            System.out.println("4: EXIT");

            System.out.println();
            System.out.println("Enter position: ");

            int position = sc.nextInt();
            command = new EmptyCommand(atm);

            switch (position) {
                case 1:
                    command = new WithdrawalCommand(atm);
                    break;
                case 2:
                    command = new CashInCommand(atm);
                    break;
                case 3:
                    command = new PrintStateCommand(atm);
                    break;
                case 4:
                    work = false;
                    break;
                default:
                    break;
            }

            executeCommand(command);
        }

    }

    private static void executeCommand(Command command) {
        command.execute();
    }
}
