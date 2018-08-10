package hibernate.dbService.dao;

import myORM.base.dataSets.PhoneDataSet;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class PhoneDAO {
    private Session session;

    public PhoneDAO(Session session) {
        this.session = session;
    }

    public void save(PhoneDataSet dataSet) {
        session.save(dataSet);
    }

    public PhoneDataSet read(long id) {
        return session.load(PhoneDataSet.class, id);
    }

    public PhoneDataSet readByNumber(String number) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PhoneDataSet> criteria = builder.createQuery(PhoneDataSet.class);
        Root<PhoneDataSet> from = criteria.from(PhoneDataSet.class);
        criteria.where(builder.equal(from.get("number"), number));
        Query<PhoneDataSet> query = session.createQuery(criteria);
        return query.uniqueResult();
    }

    public List<PhoneDataSet> readAll() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PhoneDataSet> criteria = builder.createQuery(PhoneDataSet.class);
        criteria.from(PhoneDataSet.class);
        return session.createQuery(criteria).list();
    }
}
