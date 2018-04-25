package ru.otus.java.dz2_1;

import java.lang.instrument.Instrumentation;

public class ObjectSizeAgent {

    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation instrumentation) {
        ObjectSizeAgent.instrumentation = instrumentation;
    }

    public static long getSize(Object object) {
        if (instrumentation == null) {
            throw new IllegalStateException("Agent not initialised");
        }

        return instrumentation.getObjectSize(object);
    }

}
