package myORM.base.dataSets;

import myORM.base.dataSets.reflections.annotations.*;

import javax.persistence.*;

@EntityMyORM
@TableMyORM(name="phone")
@Entity
@Table(name="phone")
public class PhoneDataSet extends DataSet {

    @ColumnMyORM(type = "VARCHAR(255)")
    @Column
    private String number;


    @ManyToOneMyORM
    @JoinColumnMyORM(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserDataSet user;

    public PhoneDataSet() {}

    public PhoneDataSet(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public UserDataSet getUser() {
        return user;
    }

    public void setUser(UserDataSet user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return String.format("Id: %s, Number: %s", super.getId(), number);
    }
}
