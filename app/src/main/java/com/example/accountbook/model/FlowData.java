package com.example.accountbook.model;

public class FlowData {

    private String flow_id;
    private String category;
    private String money;
    private String makeDate;
    private String note;
    private String location;
    private boolean isCost;

    public FlowData(String flow_id, String category, String money, String makeDate, String note, String location, boolean isCost) {
        this.flow_id = flow_id;
        this.category = category;
        this.money = money;
        this.makeDate = makeDate;
        this.note = note;
        this.location = location;
        this.isCost = isCost;
    }

    public FlowData() {

    }

    public String getFlow_id() {
        return flow_id;
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

    public String getNote() {
        return note;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "FlowData{" +
                "flow_id='" + flow_id + '\'' +
                ", category='" + category + '\'' +
                ", money='" + money + '\'' +
                ", makeDate='" + makeDate + '\'' +
                ", note='" + note + '\'' +
                ", location='" + location + '\'' +
                ", isCost=" + isCost +
                '}';
    }
}
