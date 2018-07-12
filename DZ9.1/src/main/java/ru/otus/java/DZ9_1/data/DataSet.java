package ru.otus.java.DZ9_1.data;

import ru.otus.java.DZ9_1.data.reflections.annotations.Id;

abstract public class DataSet {

    @Id(type="BIGINT(20)")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}