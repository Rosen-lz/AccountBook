package com.example.accountbook.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.accountbook.model.Category;
import com.example.accountbook.model.InsertFlow;
import com.example.accountbook.model.Percentage;
import com.example.accountbook.model.User;
import com.example.accountbook.model.FlowData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class UserService {
    private static SQLiteDatabase sdb_w = null;
    private static SQLiteDatabase sdb_r = null;


    private static String username = null;
    private DatabaseHelper dbHelper;
    public UserService(Context context){
        dbHelper = new DatabaseHelper(context);
        sdb_w = dbHelper.getWritableDatabase();
        sdb_r = dbHelper.getReadableDatabase();
    }

    public static void setUsername(String username) {
        UserService.username = username;
    }

    public String getEmail() {
        String sql="select email from user where username=?";
        Cursor cursor = sdb_r.rawQuery(sql, new String[]{username});
        if (cursor.moveToNext()) {
            String email = cursor.getString(cursor.getColumnIndex("email"));
            return email;
        }
        return "no data";
    }

    public boolean login(String username,String password){
        String sql="select * from user where username=? or email=? or phone=?";
        Cursor cursor = sdb_r.rawQuery(sql, new String[]{username,username,username});
        if(cursor.moveToFirst()==true){
            String temp = cursor.getString(cursor.getColumnIndex("password"));
            if (temp.equals(password)) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    public boolean register(User user){
        Cursor cursor = sdb_w.query("user", null, "username=?", new String[]{user.getUsername()}, null, null, null);
        if (cursor.getCount() == 0){
            String sql="insert into user(username,password,birthday,sex) values(?,?,?,?)";
            Object obj[]={user.getUsername(),user.getPassword(),user.getBirthday(),user.getSex()};
            sdb_w.execSQL(sql, obj);
            cursor.close();
            return true;
        }else {
            cursor.close();
            return false;
        }
    }

    public User getUserInfo() {
        String sql = "select * from user where username=?";
        Cursor cursor = sdb_r.rawQuery(sql, new String[] {username});
        User user = null;
        if (cursor.moveToNext()) {
            Integer id = cursor.getInt(cursor.getColumnIndex("id"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
            String sex = cursor.getString(cursor.getColumnIndex("sex"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            user = new User(id.toString(), username, password, birthday, sex, email, phone);
        }
        cursor.close();
        return user;
    }

    public boolean updateInfo(String id, String type, String value) {
        try{
            String sql = "update user set " + type + "=? where id=?";
            Object obj[] = {value, id};
            sdb_w.execSQL(sql, obj);
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(type, value);
//            sdb_w.update("user", contentValues, "id=?", new String[]{id});
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isInfoExist(String type, String value){
        String sql = "Select * from user where " + type + "=?";
        Cursor cursor = sdb_r.rawQuery(sql,new String[]{value});
        if(cursor.getCount() == 0){
            cursor.close();
            return false;
        }else{
            cursor.close();
            return true;
        }
    }

    public List<FlowData> getData(){
        List<FlowData> mitemList = new ArrayList<>();
        String sql = "select x.id, x.isCost, x. money, x.makeDate, x.note, x.location, y.type_name " +
                "from costDetail x, flow_type y, user z where x.type=y.id and z.id=x.user_id and " +
                "z.username=? order by datetime(x.makeDate) desc";
        Cursor cursor = sdb_r.rawQuery(sql, new String[]{username});
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
        Integer userId = this.getUserID();
        String sql = "select x.id, x.isCost, x. money, x.makeDate, x.note, x.location, y.type_name " +
                "from costDetail x, flow_type y where x.type=y.id and x.user_id=? " +
                "and x.makeDate like ? order by datetime(x.makeDate) desc";
        Cursor cursor = sdb_r.rawQuery(sql, new String[]{userId.toString(), "%"+Year_Month+"%"});
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
        String sql = "select * from flow_type";
        Cursor cursor = sdb_r.rawQuery(sql, null);
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
        String sql = "select id from user where username=?";
        Cursor cursor = sdb_r.rawQuery(sql, new String[]{username});
        if (cursor.moveToFirst()){
            temp = cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();
        return temp;
    }

    public void insertFlow(InsertFlow temp){
        String sql = "insert into costDetail(user_id, type, money, note, makeDate, isCost, location) values(?,?,?,?,?,?,?)";
        Object obj[] = {temp.getUserid(), temp.getType(), temp.getMoney(), temp.getNote(), temp.getDate(), temp.getCost(), temp.getLocation()};
        sdb_w.execSQL(sql, obj);
    }

    public List<Percentage> getPieChartData(String isCost, String Year_Month){
        List<Percentage> data = new ArrayList<>();
        Integer userId = this.getUserID();
        String sql = "select x.money, y.type_name from costDetail x, flow_type y where x.user_id=? and x.isCost=? " +
                "and x.makeDate like ? and y.id=x.type";
        Cursor cursor = sdb_r.rawQuery(sql, new String[]{userId.toString(), isCost, "%"+Year_Month+"%"});
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
        sdb_w.delete("costDetail","id = ?", new String[]{ id });
        sdb_w.close();
    }
}

