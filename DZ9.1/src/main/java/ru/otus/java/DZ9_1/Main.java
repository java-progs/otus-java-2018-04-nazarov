package ru.otus.java.DZ9_1;

import ru.otus.java.DZ9_1.data.EmployeeDataSet;
import ru.otus.java.DZ9_1.data.UserDataSet;

import java.util.Random;

import static ru.otus.java.DZ9_1.orm.DataSetOrm.getDataSet;
import static ru.otus.java.DZ9_1.orm.DataSetOrm.prepareDB;
import static ru.otus.java.DZ9_1.orm.DataSetOrm.saveDataSet;

public class Main {

    public static void main(String... args) {

        prepareDB(UserDataSet.class);
        for (int i = 0; i < 5; i++) {
            saveDataSet(createUserDataSet());
        }
        System.out.println(getDataSet(3, UserDataSet.class));


        prepareDB(EmployeeDataSet.class);
        for (int i = 0; i < 5; i++) {
            saveDataSet(createEmployeeDataSet());
        }
        System.out.println(getDataSet(5, EmployeeDataSet.class));

        //clearDB(UserDataSet.class);
        //clearDB(EmployeeDataSet.class);
    }

    private static UserDataSet createUserDataSet() {
        Random random = new Random();
        UserDataSet userDataSet = new UserDataSet();
        userDataSet.setName("User_" + (random.nextInt(99) + 1));
        userDataSet.setAge((random.nextInt(99) + 1));

        return userDataSet;
    }

    private static EmployeeDataSet createEmployeeDataSet() {
        Random random = new Random();
        EmployeeDataSet employeeDataSet = new EmployeeDataSet();
        employeeDataSet.setName("Employee_" + (random.nextInt(99) + 1));
        employeeDataSet.setAge((random.nextInt(99) + 1));
        employeeDataSet.setDepartment("Department_" + (random.nextInt(99) + 1));
        employeeDataSet.setSalary((random.nextInt(99) + 1));

        return employeeDataSet;
    }
}