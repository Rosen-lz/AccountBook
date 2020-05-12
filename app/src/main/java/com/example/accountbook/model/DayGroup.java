package com.example.accountbook.model;

import java.util.ArrayList;
import java.util.List;

public class DayGroup {
    private List<Members> members = new ArrayList<>();
    private String day;

    public String getDay() {
        return day;
    }

    public DayGroup(String day, String type, String money){
        this.day = day;
        members.add(new Members(type, money));
    }

    public void addMember(String type, String money){
        members.add(new Members(type, money));
    }

    public String getType(int position){
        return members.get(position).type;
    }

    public String getMoney(int position){
        return members.get(position).money;
    }

    public int getMemberCount(){
        return members == null? 0 : members.size();
    }

    @Override
    public String toString() {
        return "DayGroup{" +
                "members=" + members +
                ", day='" + day + '\'' +
                '}';
    }

    private class Members{
        public String type, money;

        public Members(String type, String money) {
            this.type = type;
            this.money = money;
        }
    }
}
