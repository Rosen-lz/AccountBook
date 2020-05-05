package com.example.accountbook.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.accountbook.User;


public class UserService {
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
    public Integer getUserId(String name){
        Integer id = null;
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        String sql="select id from user where username=?";
        Cursor cursor = sdb.rawQuery(sql, new String[]{name});
        if(cursor.moveToNext()){
            id = cursor.getInt(cursor.getColumnIndex("id"));
        }
        return id;
    }
}

