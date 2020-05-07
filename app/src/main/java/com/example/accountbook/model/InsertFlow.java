package com.example.accountbook.model;

public class InsertFlow {
    private Boolean isCost;
    private Integer type;
    private Integer userid;
    private String note;
    private String money;
    private String date;
    private String location;

    public InsertFlow() {
    }

    public void setCost(Boolean cost) {
        isCost = cost;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "InsertFlow{" +
                "isCost=" + isCost +
                ", type=" + type +
                ", userid=" + userid +
                ", note='" + note + '\'' +
                ", money='" + money + '\'' +
                ", date='" + date + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
