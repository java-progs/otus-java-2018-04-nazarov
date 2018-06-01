package ru.otus.java.DZ6_1.Commands;

import ru.otus.java.DZ6_1.Devices.ATM;
import ru.otus.java.DZ6_1.Devices.WithdrawalException;

import java.util.Scanner;

public class WithdrawalCommand extends Command {

    public WithdrawalCommand(ATM atm) {
        super(atm);
    }

    @Override
    public boolean execute() {
        Scanner sc = new Scanner(System.in);

        System.out.println();
        System.out.println("===== Withdrawal =====");

        String withdrawalState = atm.getWithdrawalState();
        if (withdrawalState.equals(atm.WITHDRAWAL_NOT_AVAILABLE)) {
            System.out.println(atm.WITHDRAWAL_NOT_AVAILABLE);
            return false;
        }

        System.out.println("Enter withdrawal amount: ");

        int amount = sc.nextInt();

        try {
            atm.withdrawal(amount);
            System.out.println("Please take your cash");
        } catch (WithdrawalException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }
}
