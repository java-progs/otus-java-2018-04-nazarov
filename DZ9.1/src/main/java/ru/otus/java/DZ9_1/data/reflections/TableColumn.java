package ru.otus.java.DZ9_1.data.reflections;

public class TableColumn {
    private String name;
    private String dataType;
    private String columnType;

    public TableColumn(String name, String dataType, String columnType) {
        this.name = name;
        this.dataType = dataType;
        this.columnType = columnType;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public String getColumnType() {
        return columnType;
    }
}