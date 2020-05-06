package com.example.accountbook.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.accountbook.User;
import com.example.accountbook.ui.home.DetailsItem;

import java.util.ArrayList;
import java.util.List;


public class UserService {
    public static void setUsername(String username) {
        UserService.username = username;
    }

    private static String username = null;
    private DatabaseHelper dbHelper;
    public UserService(Context context){
        dbHelper=new DatabaseHelper(context);
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
    public List<DetailsItem> getData(){
        List<DetailsItem> mitemList = new ArrayList<>();
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        String sql = "select * from costDetail x, flow_type y, user z where x.type=y.id and z.id=x.user_id and z.username=?";
        Cursor cursor = sdb.rawQuery(sql, new String[]{username});
        if (cursor.moveToFirst()) {
            Boolean isCost;
            String money, date, type;
            do {
                isCost = cursor.getString(cursor.getColumnIndex("isCost")).equals("1");
                money = cursor.getString(cursor.getColumnIndex("money"));
                date = cursor.getString(cursor.getColumnIndex("makeDate"));
                type = cursor.getString(cursor.getColumnIndex("type_name"));
                if (isCost){
                    money = "-" + money;
                }else {
                    money = "+" + money;
                }
                // adding to todo list
                mitemList.add(new DetailsItem(type, money, date));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return mitemList;
    }
}

