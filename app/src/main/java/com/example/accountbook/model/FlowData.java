package com.example.accountbook.model;

public class FlowData {
    private String category;
    private String money;
    private String makeDate;
    private String note;
    private String location;
    private boolean isCost;

    public FlowData(String category, String money, String makeDate, String note, String location, boolean isCost) {
        this.category = category;
        this.money = money;
        this.makeDate = makeDate;
        this.note = note;
        this.location = location;
        this.isCost = isCost;
    }

    public FlowData() {

    }

    public FlowData(String category, String money, String makeDate, boolean isCost) {
        this.category = category;
        this.money = money;
        this.makeDate = makeDate;
        this.isCost = isCost;
    }

    public boolean isCost() {
        return isCost;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMakeDate() {
        return makeDate;
    }

    @Override
    public String toString() {
        return "DetailsItem{" +
                "type='" + category + '\'' +
                ", money='" + money + '\'' +
                ", makeDate='" + makeDate + '\'' +
                '}';
    }

    public String getNote() {
        return note;
    }

    public String getLocation() {
        return location;
    }
}
