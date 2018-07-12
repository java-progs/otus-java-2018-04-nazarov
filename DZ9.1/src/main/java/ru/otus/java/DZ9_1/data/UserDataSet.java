package ru.otus.java.DZ9_1.data;

import ru.otus.java.DZ9_1.data.reflections.annotations.Column;
import ru.otus.java.DZ9_1.data.reflections.annotations.Entity;
import ru.otus.java.DZ9_1.data.reflections.annotations.Table;

@Entity
@Table(name="user")
public class UserDataSet extends DataSet {

    @Column(type = "VARCHAR(255)")
    private String name;

    @Column(type = "INT(3)")
    int age;

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

    public String toString() {
        return String.format("Id: %s, name: %s, age: %s", super.getId(), getName(), getAge());
    }
}