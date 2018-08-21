package hibernate.dbService.dao;

import myORM.base.dataSets.AddressDataSet;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class AddressDAO {
    private Session session;

    public AddressDAO(Session session) {
        this.session = session;
    }

    public void save(AddressDataSet dataSet) {
        session.save(dataSet);
    }

    public AddressDataSet read(long id) {
        return session.load(AddressDataSet.class, id);
    }

    public AddressDataSet readByAddress(String address) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AddressDataSet> criteria = builder.createQuery(AddressDataSet.class);
        Root<AddressDataSet> from = criteria.from(AddressDataSet.class);
        criteria.where(builder.equal(from.get("street"), address));
        Query<AddressDataSet> query = session.createQuery(criteria);
        return query.uniqueResult();
    }

    public List<AddressDataSet> readAll() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AddressDataSet> criteria = builder.createQuery(AddressDataSet.class);
        criteria.from(AddressDataSet.class);
        return session.createQuery(criteria).list();
    }
}
