package com.example.accountbook.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //todo auto-generated method stub
        return null;
    }

    //Callback function called when the service object is created
    @Override
    public void onCreate() {
        super.onCreate();
        //Override the method
    }

    //Callback function called when the service is started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Override the method
        return super.onStartCommand(intent, flags, startId);
    }

    //Callback function called when the service is stopped
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
