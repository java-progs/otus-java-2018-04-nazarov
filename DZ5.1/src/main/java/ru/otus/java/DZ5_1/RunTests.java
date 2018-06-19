package ru.otus.java.DZ5_1;

import ru.otus.java.DZ5_1.framework.TestFramework;

public class RunTests {
    public static void main(String... args) {
        TestFramework test = new TestFramework();

        test.runTest(ExampleTest.class);

        test.runAllPackageTests("ru.forTest.tests");
    }
}
