package com.mmvtc.college.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;

import com.mmvtc.college.R;

public class Grade {
    private Activity mContext;
    private LayoutInflater mInflater;
    private View mCurrentView;

    public Grade(Activity context) {
        mContext = context;
        //为之后将Layout转化为view时用
        mInflater = LayoutInflater.from(mContext);
    }
    private  void createView() {
        initView();
    }
    /**
     * 获取界面控件
     */
    private void initView() {
        //设置布局文件
        mCurrentView = mInflater.inflate(R.layout.activity_grade, null);

    }

    /**
     * 获取当前在导航栏上方显示对应的View
     */
    public View getView() {
        if (mCurrentView == null) {
            createView();
        }
        return mCurrentView;
    }
    /**
     * 显示当前导航栏上方所对应的view界面
     */
    public void showView(){
        if(mCurrentView == null){
            createView();
        }
        mCurrentView.setVisibility(View.VISIBLE);
    }
    /**
     * 从SharedPreferences中读取登录状态
     */
    private boolean readLoginStatus(){
        SharedPreferences sp=mContext.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        boolean isLogin=sp.getBoolean("isLogin", false);
        return isLogin;
    }

}