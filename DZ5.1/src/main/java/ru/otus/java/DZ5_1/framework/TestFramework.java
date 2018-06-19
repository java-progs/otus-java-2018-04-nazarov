package ru.otus.java.DZ5_1.framework;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import ru.otus.java.DZ5_1.framework.annotations.After;
import ru.otus.java.DZ5_1.framework.annotations.Before;
import ru.otus.java.DZ5_1.framework.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class TestFramework {

    private static boolean testStatus = false;

    private static int passedCount;
    private static int failCount;

    private static Object instance;

    private static Set<Method> setBefore;
    private static Set<Method> setAfter;
    private static Set<Method> setTest;

    public static void runAllPackageTests(String pckg) {
        MethodAnnotationsScanner scanner = new MethodAnnotationsScanner();
        Reflections reflections = new Reflections(pckg, scanner);

        Set<Method> annotationMethods = reflections.getMethodsAnnotatedWith(Test.class);
        Set<Class> classList = new HashSet<>();
        for (Method m : annotationMethods) {
            classList.add(m.getDeclaringClass());
        }

        for (Class cls : classList) {
            runTest(cls);
        }
    }

    public static void runTest(Class<?> cls) {
        System.out.println("--------------------");
        System.out.println(String.format("Run tests on %s", cls));
        System.out.println("--------------------");
        setBefore = new HashSet();
        setAfter = new HashSet();
        setTest= new HashSet();

        passedCount = 0;
        failCount = 0;

        for (Method m : cls.getMethods()) {
            if (m.isAnnotationPresent(Before.class)) setBefore.add(m);
        }
        for (Method m : cls.getMethods()) {
            if (m.isAnnotationPresent(After.class)) setAfter.add(m);
        }
        for (Method m : cls.getMethods()) {
            if (m.isAnnotationPresent(Test.class)) setTest.add(m);
        }


        try {
            instance = cls.newInstance();

            for (Method m : setTest) {
                try {
                    testStatus = false;
                    runBefore();

                    testStatus = true;
                    m.invoke(instance);

                } catch (Exception e) {
                    e.printStackTrace();
                    testStatus = false;
                } finally {

                    try {
                        runAfter();
                    } catch (Exception e) {
                        e.printStackTrace();
                        testStatus = false;
                    }

                    if (testStatus) passedCount++;
                    else failCount++;

                    printTestResult(m.getName(), testStatus);
                }
            }

        } catch (InstantiationException ie) {
            ie.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        } finally {
            System.out.println("=============");
            System.out.println("Tests results:");
            System.out.println("Passed: " + passedCount);
            System.out.println("Fail: " + failCount);
        }

    }

    private static void runBefore() throws InvocationTargetException, IllegalAccessException {
        for (Method m : setBefore) {
            m.invoke(instance);
        }
    }

    private static void runAfter() throws InvocationTargetException, IllegalAccessException {
        for (Method m : setAfter) {
            m.invoke(instance);
        }
    }

    private static void printTestResult(String method, boolean status) {
        System.out.println();
        System.out.println(method + ". Status: " + ((status == false) ? "FAIL" : "PASSED"));
    }

    public static void assertEquals(int a, int b) {
        if (a != b) {
            testStatus = false;
        }
    }
}
