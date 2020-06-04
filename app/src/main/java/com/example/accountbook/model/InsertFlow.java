package com.example.accountbook.model;

public class InsertFlow {
    private Boolean isCost;

    private Integer type;
    private Integer userid;
    private String note;
    private String money;
    private String date;
    private String location;


    public InsertFlow(Boolean isCost, Integer type, Integer userid, String note, String money, String date, String location) {
        this.isCost = isCost;
        this.type = type;
        this.userid = userid;
        this.note = note;
        this.money = money;
        this.date = date;
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

    public Boolean getCost() {
        return isCost;
    }

    public Integer getType() {
        return type;
    }

    public Integer getUserid() {
        return userid;
    }

    public String getNote() {
        return note;
    }

    public String getMoney() {
        return money;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}
