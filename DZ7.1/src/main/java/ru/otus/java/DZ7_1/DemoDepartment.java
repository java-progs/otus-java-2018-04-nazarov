package ru.otus.java.DZ7_1;

import ru.otus.java.DZ7_1.Department.ATMDepartment;

public class DemoDepartment {
    public static void main (String... args) {

        System.out.println(String.format("----- Initial status ATM Department -----"));
        ATMDepartment department = new ATMDepartment(5);
        department.printATMList();

        department.printAllATMBalance();

        System.out.println();

        System.out.println(String.format("----- Withdrawal from all ATMs -----"));

        department.randomWithdrawal();
        department.printAllATMBalance();
        department.randomWithdrawal();
        department.printAllATMBalance();

        System.out.println();
        System.out.println(String.format("----- Restore all ATM states -----"));
        System.out.println();

        department.restoreAllATMState();

        department.printAllATMBalance();

    }
}
