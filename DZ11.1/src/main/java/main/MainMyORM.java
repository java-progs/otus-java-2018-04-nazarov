package main;

import myORM.base.dataSets.AddressDataSet;
import myORM.base.dataSets.PhoneDataSet;
import myORM.dbService.MyDBServiceImpl;
import myORM.base.dataSets.UserDataSet;

import java.util.List;
import java.util.Random;

public class MainMyORM {

    public static void main(String... args) {

        MyDBServiceImpl dbService = new MyDBServiceImpl();

        try {
            dbService.prepareDB(UserDataSet.class);

            for (int i = 0; i < 70; i++) {
                UserDataSet user = createUserDataSet();
                dbService.save(user);
                System.out.println("Add new user : " + user);
            }

            System.out.println();
            System.out.println("Select user 1 : " + dbService.read(1));
            System.out.println();
            System.out.println("Select user 2 : " + dbService.read(2));
            System.out.println();
            System.out.println("Select user 1 : " + dbService.read(1));
            System.out.println();
            System.out.println("Select user 54 : " + dbService.read(54));
            System.out.println();

            Thread.sleep(3000);
            System.out.println();
            System.out.println("Read all users");

            List<UserDataSet> allUsers = dbService.readAll();

            /*for (UserDataSet userList : allUsers) {
                System.out.println(userList);
            }*/

            System.out.println();
            System.out.println("Select user 54 : " + dbService.read(54));
            System.out.println();

            dbService.deleteTable(UserDataSet.class);
            dbService.deleteTable(PhoneDataSet.class);
            dbService.deleteTable(AddressDataSet.class);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dbService.close();
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            dbService.getCache().dispose();
        }
    }

    private static UserDataSet createUserDataSet() {
        Random random = new Random();
        UserDataSet userDataSet = new UserDataSet();
        userDataSet.setName("User_" + (random.nextInt(99) + 1));
        userDataSet.setAge((random.nextInt(99) + 1));
        userDataSet.setAddress(new AddressDataSet("Mira str, " + (random.nextInt(200) + 1)));
        userDataSet.addPhone(new PhoneDataSet("8-800-200-30-" + (random.nextInt(200) + 1)));
        userDataSet.addPhone(new PhoneDataSet("+7-912-123-45-" + (random.nextInt(200) + 1)));

        return userDataSet;
    }
}
