package ru.forTest.tests;


import ru.otus.java.DZ5_1.framework.annotations.After;
import ru.otus.java.DZ5_1.framework.annotations.Before;
import ru.otus.java.DZ5_1.framework.annotations.Test;

import static ru.otus.java.DZ5_1.framework.TestFramework.assertEquals;

public class ExampleTest {
    static SampleClass clazz = new SampleClass();

    @Before
    public void before() {
        System.out.println("");
        System.out.println("Run before method on ExampleTest");
    }

    @After
    public void after() {
        System.out.println("Run after method on ExampleTest");
    }

    @Test
    public void test1_ExampleTest() {
        int result = clazz.mult(10, 15);
        assertEquals(result, 120);
    }

    @Test
    public void test2_ExampleTest() {
        int result = clazz.mult(0, 7);
        assertEquals(result, 0);
    }

    @Test
    public void test3_ExampleTest() {
        int result = clazz.div(10, 12);
        assertEquals(result, 10/12);
    }
}
