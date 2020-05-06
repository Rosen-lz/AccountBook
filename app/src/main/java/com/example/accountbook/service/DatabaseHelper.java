package com.example.accountbook.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    static String name="sit305.db";
    static int dbVersion=1;

    public DatabaseHelper(Context context) {
        super(context, name, null, dbVersion);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists user(id INTEGER primary key autoincrement, username STRING not null," +
                "password STRING not null, birthday STRING not null, sex STRING not null)");
        db.execSQL("create table if not exists flow_type(id INTEGER primary key autoincrement, type_name STRING not null)");
        db.execSQL("create table if not exists costDetail(id INTEGER primary key autoincrement," +
                "user_id INTEGER not null, type INTEGER not null, money STRING not null," +
                "note STRING, makeDate STRING not null, isCost BOOLEAN not null)");

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}

