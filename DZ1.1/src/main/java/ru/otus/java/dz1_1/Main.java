package ru.otus.java.dz1_1;

import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.Scanner;

/*
 *  Читает строку с консоли и разделяет ее на слова используя Google Guava
 */
public class Main {
    public static void main(String args[]) {
        System.out.println("Enter the text: ");

        Scanner in = new Scanner(System.in);
        String str = in.nextLine();

        Iterable<String> split = Splitter.onPattern("[,. \\-();:]")
                .trimResults()
                .omitEmptyStrings()
                .split(str);

        System.out.println("List of words: ");
        for (String s : split) {
            System.out.println(s);
        }
    }
}
