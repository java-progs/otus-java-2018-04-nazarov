package ru.otus.java.DZ7_1.Devices;

/**
 * Created by operator on 24.05.2018.
 */
enum CasseteNominals {
    NO(0),
    FIFTY(50),
    HUNDRED(100),
    TWO_HUNDRED(200),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000),
    TWO_THOUSAND(2000),
    FIVE_THOUSAND(5000);

    private int nominal;

    CasseteNominals(int nominal) {
        this.nominal = nominal;
    }

    int getNominal() {
        return nominal;
    }

}