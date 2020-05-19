package com.example.accountbook.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.accountbook.model.Category;
import com.example.accountbook.model.Percentage;
import com.example.accountbook.model.User;
import com.example.accountbook.model.FlowData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class UserService {
    public static void setUsername(String username) {
        UserService.username = username;
    }

    private static String username = null;
    private DatabaseHelper dbHelper;
    public UserService(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    public boolean login(String username,String password){
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        String sql="select * from user where username=? and password=?";
        Cursor cursor = sdb.rawQuery(sql, new String[]{username,password});
        if(cursor.moveToFirst()==true){
            cursor.close();
            return true;
        }
        return false;
    }

    public boolean register(User user){
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        Cursor cursor = sdb.query("user", null, "username=?", new String[]{user.getUsername()}, null, null, null);
        if (cursor.getCount() == 0){
            String sql="insert into user(username,password,birthday,sex) values(?,?,?,?)";
            Object obj[]={user.getUsername(),user.getPassword(),user.getBirthday(),user.getSex()};
            sdb.execSQL(sql, obj);
            cursor.close();
            return true;
        }else {
            cursor.close();
            return false;
        }
    }

    public List<FlowData> getData(){
        List<FlowData> mitemList = new ArrayList<>();
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        String sql = "select x.id, x.isCost, x. money, x.makeDate, x.note, x.location, y.type_name " +
                "from costDetail x, flow_type y, user z where x.type=y.id and z.id=x.user_id and " +
                "z.username=? order by datetime(x.makeDate) desc";
        Cursor cursor = sdb.rawQuery(sql, new String[]{username});
        if (cursor.moveToFirst()) {
            Boolean isCost;
            String money, date, type, note, location, flow_id;
            do {
                Integer temp_id = cursor.getInt(cursor.getColumnIndex("id"));
                flow_id = temp_id.toString();
                isCost = cursor.getString(cursor.getColumnIndex("isCost")).equals("1");
                money = cursor.getString(cursor.getColumnIndex("money"));
                date = cursor.getString(cursor.getColumnIndex("makeDate"));
                type = cursor.getString(cursor.getColumnIndex("type_name"));
                note = cursor.getString(cursor.getColumnIndex("note"));
                location = cursor.getString(cursor.getColumnIndex("location"));
                // adding to todo list
                mitemList.add(new FlowData(flow_id, type, money, date, note, location, isCost));
            } while (cursor.moveToNext());
            cursor.close();
            return mitemList;
        }
        return null;
    }

    public List<FlowData> getMonthDate(String Year_Month){
        List<FlowData> mitemList = new ArrayList<>();
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        Integer userId = this.getUserID();
        String sql = "select x.id, x.isCost, x. money, x.makeDate, x.note, x.location, y.type_name " +
                "from costDetail x, flow_type y where x.type=y.id and x.user_id=? " +
                "and x.makeDate like ? order by datetime(x.makeDate) desc";
        Cursor cursor = sdb.rawQuery(sql, new String[]{userId.toString(), "%"+Year_Month+"%"});
        if (cursor.moveToFirst()) {
            Boolean isCost;
            String money, date, type, note, location, flow_id;
            do {
                Integer temp_id = cursor.getInt(cursor.getColumnIndex("id"));
                flow_id = temp_id.toString();
                isCost = cursor.getString(cursor.getColumnIndex("isCost")).equals("1");
                money = cursor.getString(cursor.getColumnIndex("money"));
                date = cursor.getString(cursor.getColumnIndex("makeDate"));
                type = cursor.getString(cursor.getColumnIndex("type_name"));
                note = cursor.getString(cursor.getColumnIndex("note"));
                location = cursor.getString(cursor.getColumnIndex("location"));
                mitemList.add(new FlowData(flow_id, type, money, date, note, location, isCost));
            } while (cursor.moveToNext());
            cursor.close();
            return mitemList;
        }
        return null;
    }

    public List<Category> getCategory(){
        List<Category> mitemList = new ArrayList<>();
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        String sql = "select * from flow_type";
        Cursor cursor = sdb.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            String name;
            Integer id;
            do {
                name = cursor.getString(cursor.getColumnIndex("type_name"));
                id = cursor.getInt(cursor.getColumnIndex("id"));
                // adding to todo list
                mitemList.add(new Category(name, false, id));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return mitemList;
    }

    public Integer getUserID(){
        Integer temp = null;
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        String sql = "select id from user where username=?";
        Cursor cursor = sdb.rawQuery(sql, new String[]{username});
        if (cursor.moveToFirst()){
            temp = cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();
        return temp;
    }

    public void insertFlow(Integer user_id, Integer type, String money, String note, String makeDate, Boolean isCost, String location){
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        String sql = "insert into costDetail(user_id, type, money, note, makeDate, isCost, location) values(?,?,?,?,?,?,?)";
        Object obj[] = {user_id, type, money, note, makeDate, isCost, location};
        sdb.execSQL(sql, obj);
    }

    public List<Percentage> getPieChartData(String isCost, String Year_Month){
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        List<Percentage> data = new ArrayList<>();
        Integer userId = this.getUserID();
        String sql = "select x.money, y.type_name from costDetail x, flow_type y where x.user_id=? and x.isCost=? " +
                "and x.makeDate like ? and y.id=x.type";
        Cursor cursor = sdb.rawQuery(sql, new String[]{userId.toString(), isCost, "%"+Year_Month+"%"});
        double total = 0;
        if (cursor.moveToFirst()) {
            String label, money;
            Double number;
            do {
                money = cursor.getString(cursor.getColumnIndex("money"));
                number = Double.parseDouble(money);
                total += number;
                label = cursor.getString(cursor.getColumnIndex("type_name"));
                data.add(new Percentage(label, number));
            } while (cursor.moveToNext());
            for (Percentage temp: data){
                Double percentage = (temp.getValue()/total) * 100;
                BigDecimal b = new BigDecimal(percentage);
                float d = (float)b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                temp.setPercentage(d);
            }
            cursor.close();
            return data;
        }
        cursor.close();
        return null;
    }

    public void deleteData(String id) {
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        sdb.delete("costDetail","id = ?", new String[]{ id });
        sdb.close();
    }
}

