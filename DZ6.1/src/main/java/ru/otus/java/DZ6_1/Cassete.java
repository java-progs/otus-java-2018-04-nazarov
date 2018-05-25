package ru.otus.java.DZ6_1;

/**
 * Created by operator on 24.05.2018.
 */
public class Cassete {

    private String name = "NoName";
    private int capacity = 0;
    private int nominal = 0;
    private int count = 0;
    private CasseteStates state = CasseteStates.NO_INITIALIZE;

    public Cassete() {
        updateState();
    }

    public Cassete(String name, int nominal, int capacity) {
        this.name = name;
        this.nominal = nominal;
        this.capacity = capacity;
        updateState();
    }


    public CasseteStates setName(String name) {
        this.name = name;
        updateState();

        return state;
    }

    public CasseteStates setCapacity(int capacity) {
        this.capacity = capacity;
        updateState();

        return state;
    }

    public CasseteStates setNominal(int nominal) {
        this.nominal = nominal;
        updateState();

        return state;
    }


    public boolean addNotes(int addCount) {
        if (state == CasseteStates.WORK || state == CasseteStates.CASSETE_EMPTY) {
            if (capacity - count >= addCount) {
                count += addCount;

                updateState();
                return true;
            }
        }

        return false;
    }

    public boolean getNotes(int getCount) {
        if (count >= getCount) {
            count -= getCount;
            updateState();

            return true;
        }
        return false;
    }

    public int getBalance() {
        return count * nominal;
    }

    public int getNominal() {
        return nominal;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }

    public CasseteStates getState() {
        return state;
    }

    private void updateState() {
        if (capacity <= 0 || nominal <= 0 || name.equals("NoName") || name.length() == 0) {
            state = CasseteStates.NO_INITIALIZE;
        } else if (count == capacity) {
            state = CasseteStates.CASSETE_FULL;
        } else if (count > 0) {
            state = CasseteStates.WORK;
        } else {
            state = CasseteStates.CASSETE_EMPTY;
        }

    }
}