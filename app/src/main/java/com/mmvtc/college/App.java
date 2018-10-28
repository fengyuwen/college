package com.mmvtc.college;

import android.app.Application;
import android.content.Context;


public class App  extends Application {
    public static Context appContext=null;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext=getApplicationContext();
    }
}
