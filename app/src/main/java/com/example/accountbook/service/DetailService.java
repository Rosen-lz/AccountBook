package com.example.accountbook.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.accountbook.User;
import com.example.accountbook.ui.home.DetailsItem;

import java.util.ArrayList;
import java.util.List;

public class DetailService {
    private DatabaseHelper dbHelper;
    public DetailService(Context context){
        dbHelper=new DatabaseHelper(context);
    }

    public List<DetailsItem> getData(Integer userId){
        List<DetailsItem> mitemList = new ArrayList<>();
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        String sql = "select flow_type.type_name, costDetails.cost, costDetails.isCost, costDetails.makeDate " +
                "from costDetails, flow_type where user_id=? and costDetails.type=flow_type.id";
        Cursor cursor = sdb.rawQuery(sql, new String[]{String.valueOf(userId)});
        if(cursor.getCount()==0){
            return null;
        }
        while(cursor.moveToNext()){
            String type = cursor.getString(cursor.getColumnIndex("type_name"));
            Boolean isCost = cursor.getString(cursor.getColumnIndex("isCost")).equals("1");
            String money = cursor.getString(cursor.getColumnIndex("cost"));
            String date = cursor.getString(cursor.getColumnIndex("makeDate"));
            if(isCost){
                money = "-" + money;
            }else{
                money = "+" + money;
            }
            mitemList.add(new DetailsItem(type, money, date));
        }
        cursor.close();
        return mitemList;
    }
}
