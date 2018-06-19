package ru.otus.java.DZ5_1;

import ru.otus.java.DZ5_1.framework.annotations.After;
import ru.otus.java.DZ5_1.framework.annotations.Before;
import ru.otus.java.DZ5_1.framework.annotations.Test;

import static ru.otus.java.DZ5_1.framework.TestFramework.assertEquals;

public class ExampleTest {
    static SampleClass clazz = new SampleClass();

    @Before
    public void before() {
        System.out.println("");
        System.out.println("Run before method");
    }

    @After
    public void after() {
        System.out.println("Run after method");
    }

    //@Test
    public void testSum() {
        int result = clazz.summ(10, 2);
        assertEquals(result, 12);
    }

    @Test
    public void testDiv() {
        int result = clazz.div(100, 15);
        assertEquals(result, 5);
    }

    @Test
    public void testSub() {
        int result = clazz.sub(10, 2);
        assertEquals(result, 10);
    }
}
