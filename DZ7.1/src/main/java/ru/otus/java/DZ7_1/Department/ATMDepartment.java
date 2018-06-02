package ru.otus.java.DZ7_1.Department;

import ru.otus.java.DZ7_1.Devices.ATM;
import ru.otus.java.DZ7_1.Devices.WithdrawalException;
import ru.otus.java.DZ7_1.Notification.EventListener;
import ru.otus.java.DZ7_1.Notification.EventsEnum;

import java.util.*;

public class ATMDepartment implements EventListener {

    private static Map<ATM, Map<Integer, Integer>> atmList = new LinkedHashMap<>();
    private int countAtm;

    public ATMDepartment(Integer countAtm) {
        if (countAtm <= 0) countAtm = 5;
        this.countAtm = countAtm;

        //Инициализация ATM Department countAtm банкоматами, в каждом - случайное число купюр
        Random random = new Random();
        for (int i=1; i<=this.countAtm; i++) {
            List<Integer> atmInit = new ArrayList<>();

            for (int j=1; j<=5; j++) {
                atmInit.add(random.nextInt(10));
            }

            ATM atm = new ATM("ATM_" + i, atmInit);
            atm.events.subscribe(EventsEnum.CHANGE_ATM_BALANCE, this);

            // Сохранение начальных остатков по банкоматам
            atmList.put(atm, atm.getCassetesStates());
        }
    }

    public void printATMList() {
        System.out.println("ATMs list:");
        for (Map.Entry<ATM, Map<Integer, Integer>> pair : atmList.entrySet()) {
            System.out.println(pair.getKey().getName());
        }

        System.out.println();
    }

    // Получение остатков от банкомата после удачного выполнения снятия/вложения
    @Override
    public void notify(ATM atm, EventsEnum eventType, Map<Integer, Integer> value) {
        System.out.println(String.format("Update ATM state %s", atm.getName()));
        atmList.put(atm, value);
    }

    public int printAllATMBalance() {
        int allATMBalance = 0;

        for(Map.Entry<ATM, Map<Integer, Integer>> pairs : atmList.entrySet()) {

            Map<Integer, Integer> cassetes = pairs.getValue();
            int atmBalance = 0;
            for (Map.Entry<Integer, Integer> pairsCass : cassetes.entrySet()) {
                atmBalance += pairsCass.getKey() * pairsCass.getValue();
            }
            allATMBalance += atmBalance;
            System.out.println(String.format("%s, balance %s", pairs.getKey().getName(), atmBalance));
        }
        System.out.println("==========");
        System.out.println(String.format("ALL ATMs balance %s", allATMBalance));

        return allATMBalance;
    }

    public void randomWithdrawal() {
        Random random = new Random();
        for(Map.Entry<ATM, Map<Integer, Integer>> pairs : atmList.entrySet()) {
            int amount = random.nextInt(10) * 50 + random.nextInt(10) * 100 + random.nextInt(10) * 1000;
            try {
                System.out.println();
                System.out.println(String.format("Withdrawal %s from %s", amount, pairs.getKey().getName()));
                pairs.getKey().withdrawal(amount);
            } catch (WithdrawalException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void restoreAllATMState() {
        for(Map.Entry<ATM, Map<Integer, Integer>> pairs : atmList.entrySet()) {
            pairs.getKey().restoreCassetesState();
        }
    }
}
