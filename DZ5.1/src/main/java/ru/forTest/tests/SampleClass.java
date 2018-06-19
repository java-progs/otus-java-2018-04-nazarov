package ru.forTest.tests;

public class SampleClass {

    public int summ(int a, int b) {
        return a + b;
    }

    public int sub(int a, int b) {
        return a - b;
    }

    public int mult(int a, int b) {
        return a * b;
    }

    public int div(int a, int b) {
        if (b == 0) throw new ArithmeticException("Division by 0");
        return a / b;
    }

}

