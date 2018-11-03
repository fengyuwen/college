package com.mmvtc.college.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mmvtc.college.R;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {//两秒跳转activity
            @Override
            public void run() {
                Log.i(TAG, "run: "+123);
                finish();
            }
        }, 3000);
    }
}
