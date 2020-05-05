package com.example.accountbook.ui.home;

public class DetailsItem {
    private String type;
    private String money;
    private String makeDate;

    public DetailsItem(String type, String money, String makeDate) {
        this.type = type;
        this.money = money;
        this.makeDate = makeDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public void setMakeDate(String makeDate) {
        this.makeDate = makeDate;
    }

    @Override
    public String toString() {
        return "DetailsItem{" +
                "type='" + type + '\'' +
                ", money='" + money + '\'' +
                ", makeDate='" + makeDate + '\'' +
                '}';
    }
}
