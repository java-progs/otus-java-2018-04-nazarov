package hibernate.dbService;

import myORM.base.DBService;
import myORM.base.dataSets.AddressDataSet;
import myORM.base.dataSets.PhoneDataSet;
import myORM.base.dataSets.UserDataSet;
import hibernate.dbService.dao.AddressDAO;
import hibernate.dbService.dao.PhoneDAO;
import hibernate.dbService.dao.UsersDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;
import java.util.function.Function;

public class HibernateDBServiceImpl implements DBService {
    private final SessionFactory sessionFactory;

    public HibernateDBServiceImpl() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(UserDataSet.class);
        configuration.addAnnotatedClass(PhoneDataSet.class);
        configuration.addAnnotatedClass(AddressDataSet.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://10.0.0.132:3306/otusexample");
        configuration.setProperty("hibernate.connection.username", "root");
        configuration.setProperty("hibernate.connection.password", "root");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.connection.useSSL", "false");
        configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");

        sessionFactory = createSessionFactory(configuration);
    }

    public HibernateDBServiceImpl(Configuration configuration) {
        sessionFactory = createSessionFactory(configuration);
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public String getLocalStatus() {
        return runInSession(session -> {
            return session.getTransaction().getStatus().name();
        });
    }

    public void save(UserDataSet dataSet) {
        try (Session session = sessionFactory.openSession()) {
            UsersDAO dao = new UsersDAO(session);
            dao.save(dataSet);
        }
    }

    public UserDataSet read(long id) {
        return runInSession(session -> {
            UsersDAO dao = new UsersDAO(session);
            return dao.read(id);
        });
    }

    public UserDataSet readByName(String name) {
        return runInSession(session -> {
            UsersDAO dao = new UsersDAO(session);
            return dao.readByName(name);
        });
    }

    public List<UserDataSet> readAll() {
        return runInSession(session -> {
            UsersDAO dao = new UsersDAO(session);
            return dao.readAll();
        });
    }


    public void save(AddressDataSet dataSet) {
        try (Session session = sessionFactory.openSession()) {
            AddressDAO dao = new AddressDAO(session);
            dao.save(dataSet);
        }
    }

    public AddressDataSet readAddress(long id) {
        return runInSession(session -> {
            AddressDAO dao = new AddressDAO(session);
            return dao.read(id);
        });
    }

    public AddressDataSet readByAddress(String address) {
        return runInSession(session -> {
            AddressDAO dao = new AddressDAO(session);
            return dao.readByAddress(address);
        });
    }

    public List<AddressDataSet> readAllAddress() {
        return runInSession(session -> {
            AddressDAO dao = new AddressDAO(session);
            return dao.readAll();
        });
    }

    public void save(PhoneDataSet dataSet) {
        try (Session session = sessionFactory.openSession()) {
            PhoneDAO dao = new PhoneDAO(session);
            dao.save(dataSet);
        }
    }

    public PhoneDataSet readPhone(long id) {
        return runInSession(session -> {
            PhoneDAO dao = new PhoneDAO(session);
            return dao.read(id);
        });
    }

    public PhoneDataSet readByNumber(String number) {
        return runInSession(session -> {
            PhoneDAO dao = new PhoneDAO(session);
            return dao.readByNumber(number);
        });
    }

    public List<PhoneDataSet> readAllPhones() {
        return runInSession(session -> {
            PhoneDAO dao = new PhoneDAO(session);
            return dao.readAll();
        });
    }

    public void shutdown() {
        sessionFactory.close();
    }

    private <R> R runInSession(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }
}
