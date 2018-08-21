package myORM.base.dataSets;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="user")
public class UserDataSet extends DataSet {

    @Column
    String name;

    @Column
    int age;

    @OneToOne(cascade = CascadeType.ALL)
    AddressDataSet address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhoneDataSet> phones = new HashSet<>();

    public UserDataSet() {}

    public UserDataSet(String name, int age) {
        this.setId(-1L);
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Set<PhoneDataSet> getPhonesList() {
        return phones;
    }

    public void addPhone(PhoneDataSet phone) {
        phone.setUser(this);
        phones.add(phone);
    }

    public void removePhone(PhoneDataSet phone) {
        phones.remove(phone);
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("Id: %s, name: %s, age: %s, phone: %s, address: %s", super.getId(), getName(), getAge(), getPhonesList(), getAddress());
    }
}
