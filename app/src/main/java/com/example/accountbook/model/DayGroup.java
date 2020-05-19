package com.example.accountbook.model;

import java.util.ArrayList;
import java.util.List;

public class DayGroup {
    private List<Members> members = new ArrayList<>();
    private String day;
    private Double total_income = 0.0;
    private Double total_cost = 0.0;

    public String getDay() {
        return day;
    }

    public DayGroup(String flow_id, Double cost, Double income, String day, String type, String category, String money, String note, String location){
        this.day = day;
        total_cost = cost;
        total_income = income;
        members.add(new Members(flow_id, type, category, money, note, location));
    }

    public void addMember(String flow_id, Double cost, Double income, String type, String category, String money, String note, String location){
        total_income += income;
        total_cost += cost;
        members.add(new Members(flow_id, type, category, money, note, location));
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

    public String getMoney(int position){
        return members.get(position).money;
    }

    public String getCategory(int position){
        return members.get(position).category;
    }

    public String getNote(int position){
        return members.get(position).note;
    }

    public String getLocation(int position){
        return members.get(position).location;
    }

    public String getType(int position){
        return members.get(position).type;
    }

    public String getFlow_id(int position) {
        return members.get(position).flow_id;
    }

    private class Members{
        public String category, money, type, location, note, flow_id;

        public Members(String flow_id, String type, String category, String money, String note, String location) {
            this.flow_id = flow_id;
            this.category = category;
            this.money = money;
            this.type = type;
            this.location = location;
            this.note = note;
        }
    }
}
