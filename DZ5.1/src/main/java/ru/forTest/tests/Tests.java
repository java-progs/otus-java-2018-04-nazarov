package ru.forTest.tests;

import ru.otus.java.DZ5_1.framework.annotations.After;
import ru.otus.java.DZ5_1.framework.annotations.Before;
import ru.otus.java.DZ5_1.framework.annotations.Test;

import static ru.otus.java.DZ5_1.framework.TestFramework.assertEquals;

public class Tests {
    static SampleClass clazz = new SampleClass();

    @Before
    public void before() {
        System.out.println("");
        System.out.println("Run before method on Tests");
    }

    @After
    public void after() {
        System.out.println("Run after method on Tests");
    }

    @Test
    public void test1_Tests() {
        int result = clazz.mult(10, 15);
        assertEquals(result, 150);
    }

    @Test
    public void test2_Tests() {
        int result = clazz.div(10, 0);
        assertEquals(result, 1);
    }
}
