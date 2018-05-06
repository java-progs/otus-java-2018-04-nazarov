package ru.otus.java.DZ3_1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestArrayList {

    // Запуск java -classpath java -classpath ./target/classes ru.otus.java.DZ3_1.TestArrayList

    public static void main(String args[]) {

        MyArrayList<Integer> arrayList1 = new MyArrayList();
        ArrayList<Integer> arrayList2 = new ArrayList();

        for (int i=0; i<20; i++) {
            arrayList1.add(i);
        }

        System.out.println(String.format("arrayList1 size %s", arrayList1.size()));
        System.out.println("arrayList1 : ");
        printArray(arrayList1);

        for (int i=0; i<10; i++) {
            arrayList2.add(i*10);
        }

        System.out.println();
        System.out.println(String.format("arrayList2 size %s", arrayList2.size()));
        System.out.println("arrayList2 : ");
        printArray(arrayList2);

        Collections.copy(arrayList1, arrayList2);
        System.out.println();
        System.out.println("copy arrayList2 to arrayList1");
        System.out.println(String.format("arrayList1 size %s", arrayList1.size()));
        System.out.println("arrayList1 : ");
        printArray(arrayList1);

        Collections.addAll(arrayList1, 100, 200, 300, 400, 500);
        System.out.println();
        System.out.println("add new element to arrayList1");
        System.out.println(String.format("arrayList1 size %s", arrayList1.size()));
        System.out.println("arrayList1 : ");
        printArray(arrayList1);

        Collections.sort(arrayList1, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
        System.out.println();
        System.out.println("arrayList1 after sort : ");
        printArray(arrayList1);
    }

    static void printArray(List list) {
        for (int i=0; i<list.size(); i++) {
            System.out.println(i + " " + list.get(i));
        }
    }
}
