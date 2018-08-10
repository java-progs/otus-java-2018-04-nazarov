package main;

import myORM.base.dataSets.AddressDataSet;
import myORM.base.dataSets.PhoneDataSet;
import myORM.dbService.DBServiceImpl;
import myORM.base.dataSets.UserDataSet;

import java.util.List;
import java.util.Random;

public class MainMyORM {

    public static void main(String... args) {


        try (DBServiceImpl dbService = new DBServiceImpl()) {
            dbService.prepareDB(UserDataSet.class);

            for (int i = 0; i < 2; i++) {
                UserDataSet user = createUserDataSet();
                dbService.save(user);
                System.out.println("Add new user : " + user);
            }

            System.out.println();
            System.out.println();
            System.out.println("Select user 1 from db : " + dbService.read(1));
            System.out.println();
            System.out.println("All users");

            List<UserDataSet> allUsers = dbService.readAll();

            for (UserDataSet userList : allUsers) {
                System.out.println(userList);
            }

            //myORM.dbService.deleteTable(UserDataSet.class);

        } catch (Exception e) {
            e.printStackTrace();
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
