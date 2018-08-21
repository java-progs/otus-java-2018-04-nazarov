package main;

import myORM.base.dataSets.AddressDataSet;
import myORM.base.dataSets.PhoneDataSet;
import myORM.base.dataSets.UserDataSet;
import hibernate.dbService.HibernateDBServiceImpl;

import java.util.List;

public class MainHibernate {

    public static void main(String... args) {

        HibernateDBServiceImpl dbService = new HibernateDBServiceImpl();

        String status = dbService.getLocalStatus();
        System.out.println("Status: " + status);

        UserDataSet user;

        user = new UserDataSet("tully", 23);
        user.setAddress(new AddressDataSet("Gagarina str, 12"));
        user.addPhone(new PhoneDataSet("12345"));
        user.addPhone(new PhoneDataSet("454354545"));

        dbService.save(user);

        user = new UserDataSet("sully", 21);
        user.setAddress(new AddressDataSet("Mira, 19"));
        user.addPhone(new PhoneDataSet("+791200000000"));
        user.addPhone(new PhoneDataSet("8-800-200-300"));

        dbService.save(user);

        UserDataSet dataSet = dbService.read(1);
        System.out.println(dataSet);

        dataSet = dbService.readByName("sully");
        System.out.println(dataSet);

        List<UserDataSet> dataSets = dbService.readAll();
        for (UserDataSet userDataSet : dataSets) {
            System.out.println(userDataSet);
        }

        AddressDataSet address = dbService.readAddress(1);
        System.out.println("Address : " + address);
        PhoneDataSet phone = dbService.readPhone(2);
        System.out.println("Phone : " + phone);

        List<PhoneDataSet> allPhones = dbService.readAllPhones();
        for (PhoneDataSet phoneObj : allPhones) {
            System.out.println(phoneObj);
        }

        dbService.shutdown();

    }
}
