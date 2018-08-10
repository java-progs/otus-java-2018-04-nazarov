package myORM.base.dataSets;

import myORM.base.dataSets.reflections.annotations.ColumnMyORM;
import myORM.base.dataSets.reflections.annotations.EntityMyORM;
import myORM.base.dataSets.reflections.annotations.TableMyORM;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EntityMyORM
@TableMyORM(name="address")
@Entity
@Table(name="address")
public class AddressDataSet extends DataSet {

    @ColumnMyORM(type = "VARCHAR(255)")
    @Column
    private String street;

    public AddressDataSet() {
    }

    public AddressDataSet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return String.format("Id: %s, Street: %s", super.getId(), street);
    }
}
