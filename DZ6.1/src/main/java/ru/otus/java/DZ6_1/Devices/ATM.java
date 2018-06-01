package ru.otus.java.DZ6_1.Devices;

import java.util.*;

/**
 * Created by operator on 24.05.2018.
 */
public class ATM {

    public final String CASH_IN_NOT_AVAILABLE = "Cash-in not available";
    public final String WITHDRAWAL_NOT_AVAILABLE = "Withdrawal not available";
    private final int MAX_CASSETES_COUNT = 5;

    private Cassete[] cassetes = new Cassete[MAX_CASSETES_COUNT];
    CasseteManager cassManager = new CasseteManager(cassetes);

    public ATM() {
        initAtm();
        balancingAtm();
    }

    public void withdrawal(int amount) throws WithdrawalException {
         cassManager.getMinCountNotes(amount);
    }


    public void initAtm() {
        cassetes[0] = new Cassete("A", CasseteNominals.FIFTY, 10);
        cassetes[1] = new Cassete("B", CasseteNominals.ONE_THOUSAND, 10);
        cassetes[2] = new Cassete("C", CasseteNominals.TWO_THOUSAND, 10);
        cassetes[3] = new Cassete("D", CasseteNominals.HUNDRED, 10);
        cassetes[4] = new Cassete("E", CasseteNominals.ONE_THOUSAND, 10);

        cassManager.init();
    }

    public void balancingAtm() {
        cassetes[0].addNotes(5);
        cassetes[1].addNotes(7);
        cassetes[3].addNotes(10);
        cassetes[4].addNotes(10);
    }

    public void printState() {
        System.out.println("ATM state:");
        for (Cassete c : cassetes) {
            System.out.println(String.format("Cassete %s : state %s, nominal %s, capacity %s, count notes %s, balance %s",
                    c.getName(), c.getState(), c.getNominal(), c.getCapacity(), c.getCount(), c.getBalance()));
        }
        System.out.println("ATM balance: " + getBalance());
    }

    private int getBalance() {
        return cassManager.getBalance();
    }

    public String getWithdrawalState() {
        if (getBalance() > 0) {
            return "";
        } else {
            return WITHDRAWAL_NOT_AVAILABLE;
        }
    }

    public String getCashInState() {
        Set<Integer> nominals = cassManager.getCashInNominals();
        if (nominals.size() > 0) {
            StringBuilder result = new StringBuilder();
            for (Integer nominal : nominals) {
                result.append(" " + nominal);
            }

            return result.toString();
        } else {
            return CASH_IN_NOT_AVAILABLE;
        }
    }

    public Set<Integer> getCashInNominals() {
        return cassManager.getCashInNominals();
    }

    public void deposit(HashMap<Integer, Integer> notes) throws CashInException{
        cassManager.deposit(notes);
    }

    private class CasseteManager {

        Cassete[] initCassetes;

        public CasseteManager(Cassete[] initCassetes) {
            this.initCassetes = initCassetes;
        }

        // TreeMap. K - номинал кассеты, V - ArrayList кассет данного номинала
        private Map<Integer, List<Cassete>> cassetes = new TreeMap<Integer, List<Cassete>>(new Comparator<Integer>()
        {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });


        public void init() {
            for (Cassete c : initCassetes) {
                put(c);
            }
        }

        private void put(Cassete newCassete) {
            List<Cassete> alCas = cassetes.get(newCassete.getNominal());
            if (alCas == null) {
                alCas = new ArrayList<>();
            }
            alCas.add(newCassete);
            cassetes.put(newCassete.getNominal(), alCas);
        }

        public void printCassetesList() {
            for (Map.Entry<Integer, List<Cassete>> pair : cassetes.entrySet()) {
                System.out.print("Nominal : " + pair.getKey());
                for (Cassete c : pair.getValue()) {
                    System.out.print(". Cassete : " + c.getName() + ", state : " + c.getState() + ", notes : " + c.getCount());
                }
                System.out.println();
            }
        }

        // Возвращает сумму по всем касетам
        public int getBalance() {
            int balance = 0;
            for (Map.Entry<Integer, List<Cassete>> pair : cassetes.entrySet()) {
                for (Cassete c : pair.getValue()) {
                    balance += c.getBalance();
                }
            }
            return balance;
        }


        // Возвращает сумму доступных номиналов для вложения
        public Set<Integer> getCashInNominals() {
            TreeSet<Integer> nominals = new TreeSet<>();
            for (Map.Entry<Integer, List<Cassete>> pair : cassetes.entrySet()) {
                boolean addNominal = false;
                for (Cassete c : pair.getValue()) {
                    if (c.getState() == CasseteStates.CASSETE_EMPTY || c.getState() == CasseteStates.WORK) addNominal = true;
                }

                if (addNominal) nominals.add(pair.getKey());
            }
            return nominals;
        }

        // Набор суммы минимальным числом купюр
        public void getMinCountNotes(int amount) throws WithdrawalException {

            int originalAmount = amount;

            if (amount <= 0) throw new WithdrawalException(String.format("Invalid amount %s", amount));

            if (amount > getBalance()) throw new WithdrawalException(String.format("ATM can't withdraw cash. Amount %s", amount));


            // HashMap для хранения ссылки на кассету и количества банкнот, которые необходимо из нее взять
            Map<Cassete, Integer> countNotesFromCassete = new HashMap<>();

            // Проверяем возможность набрать запрошенную клиентом сумму
            // Т.к. список номиналов отсортирован в cassetes по убыванию, то начинаем набирать сумму
            // последовательно с первого элемента
            for (Map.Entry<Integer, List<Cassete>> pair : cassetes.entrySet()) {
                int countNotes = amount / pair.getKey();
                if (countNotes > 0)
                    for (Cassete c : pair.getValue()) {
                        int countFromCassete = countNotes;
                        if (c.getState() != CasseteStates.WORK && c.getState() != CasseteStates.CASSETE_FULL) continue;

                        if (c.getCount() <= countNotes) countFromCassete = c.getCount();

                        if (countFromCassete > 0) countNotesFromCassete.put(c, countFromCassete);

                        countNotes -= countFromCassete;
                        amount = amount - countFromCassete * c.getNominal();
                    }
            }

            // Если сумму набрать удастся - набираем
            if (amount == 0) {
                System.out.println("Get from cassetes : ");
                for (Map.Entry<Cassete, Integer> pair : countNotesFromCassete.entrySet()) {
                    pair.getKey().getNotes(pair.getValue());
                    System.out.println("Cassete : " + pair.getKey().getName() + ", Nominal : " + pair.getKey().getNominal() + ", Notes : " + pair.getValue());
                }
                return;
            } else {
                throw new WithdrawalException(String.format("ATM can't withdraw cash. Amount %s", originalAmount));
            }
        }

        public void deposit(Map<Integer, Integer> notes) throws CashInException {

            // HashMap для хранения ссылки на кассету и количества банкнот, которые необходимо в нее положить
            Map<Cassete, Integer> countNotesToCassete = new HashMap<>();

            for (Map.Entry<Integer, Integer> pair : notes.entrySet()) {
                int nominal = pair.getKey();
                int count = pair.getValue();

                if (count < 0) throw new CashInException(String.format("Invalid notes count. Nominal %s, count %s\nATM can't deposit cash", nominal, count));

                // Проверяем возможность разложить купюры по кассетам
                for (Cassete c : cassetes.get(nominal)) {
                    int countToCassete = 0;
                    if (c.getState() != CasseteStates.WORK && c.getState() != CasseteStates.CASSETE_EMPTY) continue;
                    if ((c.getCapacity() - c.getCount()) <= count)  countToCassete = c.getCapacity() - c.getCount();
                    else countToCassete = count;
                    countNotesToCassete.put(c, countToCassete);
                    count -= countToCassete;
                    if (count == 0) break;
                }
                if (count != 0) throw new CashInException("ATM can't deposit cash\nPlease take your cash");
            }

            // Раскладываем купюры по кассетам
            for (Map.Entry<Cassete, Integer> pair : countNotesToCassete.entrySet()) {
                pair.getKey().addNotes(pair.getValue());
            }
        }

    }
}