package ru.otus.java.DZ6_1;

import java.util.*;

/**
 * Created by operator on 24.05.2018.
 */
public class ATM {

    public final String CASH_IN_NOT_AVAILABLE = "Cash-in not available";
    public final String WITHDRAWAL_NOT_AVAILABLE = "Withdrawal not available";
    private final int MAX_CASSETES_COUNT = 5;

    public Cassete[] cassetes = new Cassete[MAX_CASSETES_COUNT];
    CasseteManager cassManager = new CasseteManager(cassetes);

    public ATM() {
        initAtm();
        balancingAtm();
    }

    public boolean withdrawal(int amount) {
        if (amount <= 0) return false;
        if (getBalance() < amount) return false;

        return getBills(amount);
    }

    private boolean getBills(int amount) {
        return cassManager.getMinCountNotes(amount);
    }

    public void initAtm() {
        cassetes[0] = new Cassete("A", 500, 10);
        cassetes[1] = new Cassete("B", 1000, 10);
        cassetes[2] = new Cassete("C", 2000, 10);
        cassetes[3] = new Cassete("D", 100, 10);
        cassetes[4] = new Cassete("E", 1000, 10);

        cassManager.init();
    }

    public void balancingAtm() {
        cassetes[0].addNotes(5);
        //cassetes[1].addNotes(1);
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
        TreeSet<Integer> nominals = cassManager.getCashInNominals();
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

    public TreeSet<Integer> getCashInNominals() {
        return cassManager.getCashInNominals();
    }

    public boolean deposit(HashMap<Integer, Integer> notes) {
        return cassManager.deposit(notes);
    }

    private class CasseteManager {

        Cassete[] initCassetes;

        public CasseteManager(Cassete[] initCassetes) {
            this.initCassetes = initCassetes;
        }

        // TreeMap. K - номинал кассеты, V - ArrayList кассет данного номинала
        private Map<Integer, ArrayList<Cassete>> cassetes = new TreeMap<Integer, ArrayList<Cassete>>(new Comparator<Integer>()
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
            ArrayList<Cassete> alCas = cassetes.get(newCassete.getNominal());
            if (alCas == null) {
                alCas = new ArrayList<>();
            }
            alCas.add(newCassete);
            cassetes.put(newCassete.getNominal(), alCas);
        }

        public void printCassetesList() {
            for (Map.Entry<Integer, ArrayList<Cassete>> pair : cassetes.entrySet()) {
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
            for (Map.Entry<Integer, ArrayList<Cassete>> pair : cassetes.entrySet()) {
                for (Cassete c : pair.getValue()) {
                    balance += c.getBalance();
                }
            }
            return balance;
        }


        // Возвращает сумму доступных номиналов для вложения
        public TreeSet<Integer> getCashInNominals() {
            TreeSet<Integer> nominals = new TreeSet<>();
            for (Map.Entry<Integer, ArrayList<Cassete>> pair : cassetes.entrySet()) {
                boolean addNominal = false;
                for (Cassete c : pair.getValue()) {
                    if (c.getState() == CasseteStates.CASSETE_EMPTY || c.getState() == CasseteStates.WORK) addNominal = true;
                }

                if (addNominal) nominals.add(pair.getKey());
            }
            return nominals;
        }

        // Набор суммы минимальным числом купюр
        public boolean getMinCountNotes(int amount) {
            if (amount > getBalance()) {
                return false;
            }


            // HashMap для хранения ссылки на кассету и количества банкнот, которые необходимо из нее взять
            Map<Cassete, Integer> countNotesFromCassete = new HashMap<>();

            // Проверяем возможность набрать запрошенную клиентом сумму
            // Т.к. список номиналов отсортирован в cassetes по убыванию, то начинаем набирать сумму
            // последовательно с первого элемента
            for (Map.Entry<Integer, ArrayList<Cassete>> pair : cassetes.entrySet()) {
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
                return true;
            } else {
                return false;
            }
        }

        public boolean deposit(HashMap<Integer, Integer> notes) {

            // HashMap для хранения ссылки на кассету и количества банкнот, которые необходимо в нее положить
            Map<Cassete, Integer> countNotesToCassete = new HashMap<>();

            for (Map.Entry<Integer, Integer> pair : notes.entrySet()) {
                int nominal = pair.getKey();
                int count = pair.getValue();

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
                if (count != 0) return false;
            }

            // Раскладываем купюры по кассетам
            for (Map.Entry<Cassete, Integer> pair : countNotesToCassete.entrySet()) {
                pair.getKey().addNotes(pair.getValue());
            }

            return true;
        }

    }
}