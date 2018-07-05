package com.mmvtc.college.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.mmvtc.college.R;


public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initData();
    }

    private void initData() {
        
    }

    @Override
    public void onClick(View view) {

    }

}