package ru.otus.java.DZ9_1.data;

import ru.otus.java.DZ9_1.data.reflections.annotations.Column;
import ru.otus.java.DZ9_1.data.reflections.annotations.Entity;
import ru.otus.java.DZ9_1.data.reflections.annotations.Table;

@Entity
@Table(name="employee")
public class EmployeeDataSet extends DataSet {

    @Column(type = "VARCHAR(255)")
    private String name;

    @Column(type = "INT(3)")
    int age;

    @Column(type = "VARCHAR(255)")
    private String department;

    @Column(type = "INT(3)")
    int salary;

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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String toString() {
        return String.format("Id: %s, name: %s, age: %s, department: %s, salary: %s",
                super.getId(), getName(), getAge(), getDepartment(), getSalary());
    }
}