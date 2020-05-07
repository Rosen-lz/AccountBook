package com.example.accountbook.model;

public class Category {

    private String name;
    private Boolean isChecked;
    private Integer id;

    public Category(String name, Boolean isChecked, Integer id) {
        this.name = name;
        this.isChecked = isChecked;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", isChecked=" + isChecked +
                ", id=" + id +
                '}';
    }
}
