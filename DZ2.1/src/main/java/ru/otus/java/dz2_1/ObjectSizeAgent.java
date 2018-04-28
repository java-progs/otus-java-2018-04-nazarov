package ru.otus.java.dz2_1;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Stack;

public class ObjectSizeAgent {

    private static Instrumentation instrumentation;
    private static Stack stack;
    private static Map visited;

    public static void premain(String args, Instrumentation instrumentation) {
        ObjectSizeAgent.instrumentation = instrumentation;
    }

    public static long getSize(Object object) {
        if (instrumentation == null) {
            throw new IllegalStateException("Agent not initialised");
        }

        visited = new IdentityHashMap();
        stack = new Stack();
        stack.push(object);

        long result = 0;
        while (!stack.empty()) {
            result += sizeOfObject(stack.pop());
        }
        return result;
    }

    private static long sizeOfObject(Object object) {
        if (object == null || visited.containsKey(object)) {
            return 0;
        }

        Class cls = object.getClass();
        if (cls.isArray()) {
            if (!cls.getComponentType().isPrimitive()) {
                int length = Array.getLength(object);
                for (int i = 0; i < length; i++) {
                    stack.add(Array.get(object, i));
                }
            }
        } else {
            while (cls != null) {
                Field[] fields = cls.getDeclaredFields();
                for (Field field : fields) {
                    if (!Modifier.isStatic(field.getModifiers())
                            && !field.getType().isPrimitive()) {
                        field.setAccessible(true);
                        try {
                            stack.add(field.get(object));
                        } catch (IllegalAccessException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                cls = cls.getSuperclass();
            }
        }
        visited.put(object, null);
        return instrumentation.getObjectSize(object);
    }

}
