package ru.otus.java.DZ6_1.Devices;

/**
 * Created by operator on 24.05.2018.
 */
public class Cassete {

    private String name;
    private int capacity;
    private CasseteNominals nominal;
    private int count;

    public Cassete() {
        name = "NoName";
        capacity = 0;
        nominal = CasseteNominals.NO;
        count = 0;
    }

    public Cassete(String name, CasseteNominals nominal, int capacity) {
        this.name = name;
        this.nominal = nominal;
        this.capacity = capacity;
    }


    public CasseteStates setName(String name) {
        this.name = name;

        return getState();
    }

    public CasseteStates setCapacity(int capacity) {
        this.capacity = capacity;

        return getState();
    }

    public CasseteStates setNominal(CasseteNominals nominal) {
        this.nominal = nominal;

        return getState();
    }


    public boolean addNotes(int addCount) {
        CasseteStates state;

        state = getState();
        if (state == CasseteStates.WORK || state == CasseteStates.CASSETE_EMPTY) {
            if (capacity - count >= addCount) {
                count += addCount;

                return true;
            }
        }

        return false;
    }

    public boolean getNotes(int getCount) {
        if (count >= getCount) {
            count -= getCount;

            return true;
        }
        return false;
    }

    public int getBalance() {
        return count * nominal.getNominal();
    }

    public int getNominal() {
        return nominal.getNominal();
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
        CasseteStates state;

        if (capacity <= 0 || nominal == CasseteNominals.NO || name.equals("NoName") || name.length() == 0) {
            state = CasseteStates.NO_INITIALIZE;
        } else if (count == capacity) {
            state = CasseteStates.CASSETE_FULL;
        } else if (count > 0) {
            state = CasseteStates.WORK;
        } else {
            state = CasseteStates.CASSETE_EMPTY;
        }

        return state;
    }

}