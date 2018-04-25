package ru.otus.java.dz2_1;

import java.util.ArrayList;
import java.util.HashSet;

public class ObjectSizeStand {

    //Запуск java -javaagent:./target/ObjectSizeAgent.jar -classpath ./target/classes/ ru.otus.java.dz2_1.ObjectSizeStand

    public static void main(String[] args) {

        final int countElements = 1000000;

        Object emptyObject = new Object();
        String emptyStringObj = new String();
        String stringObj = new String("This is a string");
        Integer integerObj = new Integer(1234567890);
        Double doubleObj = new Double(123456789.06789);
        Long longObj = new Long(Long.MAX_VALUE);
        Float floatObj = new Float(987654321.987654321);
        Character characterObj = new Character('w');
        Boolean booleanObj = new Boolean(false);

        printObjectSize("Empty " + getObjectName(emptyObject), getObjectSize(emptyObject));
        printObjectSize("Empty " + getObjectName(emptyStringObj), getObjectSize(emptyStringObj));
        printObjectSize(getObjectName(stringObj), getObjectSize(stringObj));
        printObjectSize(getObjectName(integerObj), getObjectSize(integerObj));
        printObjectSize(getObjectName(doubleObj), getObjectSize(doubleObj));
        printObjectSize(getObjectName(longObj), getObjectSize(longObj));
        printObjectSize(getObjectName(floatObj), getObjectSize(floatObj));
        printObjectSize(getObjectName(characterObj), getObjectSize(characterObj));
        printObjectSize(getObjectName(booleanObj), getObjectSize(booleanObj));

        //Массив из 1 000 000 элементов
        String[] arrString = new String[countElements];
        printObjectSize("Empty " + getObjectName(arrString), getObjectSize(arrString));

        for (int i = 0; i < countElements; i++) {
            arrString[i] = new String(i + "");
        }

        long fullSize = getObjectSize(arrString);

        for (int i = 0; i < arrString.length; i++) {
            fullSize += getObjectSize(arrString[i]);
        }

        printObjectSize(String.format("Filled of %s elements %s", arrString.length, getObjectName(arrString)), fullSize);

        // ArrayList из 500 000 элементов
        ArrayList<String> alStr = new ArrayList<>();
        printObjectSize("Empty " + getObjectName(alStr), getObjectSize(alStr));
        for (int i = 0; i < countElements / 2; i++) {
            alStr.add("String " + i);
        }

        fullSize = getObjectSize(alStr);
        for (int i = 0; i < alStr.size(); i++) {
            fullSize += getObjectSize(alStr.get(i));
        }
        printObjectSize(String.format("Filled of %s elements %s", alStr.size(), getObjectName(alStr)), fullSize);

        // ArrayList из 1 000 000 элементов
        alStr = new ArrayList<>();
        printObjectSize("Empty " + getObjectName(alStr), getObjectSize(alStr));
        for (int i = 0; i < countElements; i++) {
            alStr.add("String " + i);
        }

        fullSize = getObjectSize(alStr);
        for (int i = 0; i < alStr.size(); i++) {
            fullSize += getObjectSize(alStr.get(i));
        }
        printObjectSize(String.format("Filled of %s elements %s", alStr.size(), getObjectName(alStr)), fullSize);

        // HashSet из 500 000 элементов
        HashSet<Integer> hsInteger = new HashSet<Integer>();
        printObjectSize("Empty " + getObjectName(hsInteger), getObjectSize(hsInteger));
        for (int i = 0; i < (countElements / 2); i++) {
            hsInteger.add(i);
        }

        fullSize = getObjectSize(hsInteger);
        for (Integer intVal : hsInteger) {
            fullSize += getObjectSize(intVal);
        }
        printObjectSize(String.format("Filled of %s elements %s", hsInteger.size(), getObjectName(hsInteger)), fullSize);

        // HashSet из 1 000 000 элементов
        hsInteger = new HashSet<>();
        printObjectSize("Empty " + getObjectName(hsInteger), getObjectSize(hsInteger));
        for (int i = 0; i < countElements; i++) {
            hsInteger.add(i);
        }

        fullSize = getObjectSize(hsInteger);
        for (Integer intVal : hsInteger) {
            fullSize += getObjectSize(intVal);
        }
        printObjectSize(String.format("Filled of %s elements %s", hsInteger.size(), getObjectName(hsInteger)), fullSize);

    }

    public static long getObjectSize(Object object) {
        return ObjectSizeAgent.getSize(object);
    }

    public static String getObjectName(Object object) {
        return object.getClass().getSimpleName();
    }

    public static void printObjectSize(String objectName, long objectSize) {
        System.out.println(String.format("%s - %s bytes", objectName, objectSize));
    }

}
