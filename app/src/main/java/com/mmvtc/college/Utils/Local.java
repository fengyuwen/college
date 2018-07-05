package com.mmvtc.college.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Local {
    /*
    * 学校官网网址
    * */
    public static String url = "http://jwc.mmvtc.cn/";
    /*
     * 从输入流获取内容为字符组
     * */
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
    /*
    * 保存用户帐号密码
    * */
    public static void saveUserInfo(Context context,String user,String password){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user",user);
        editor.putString("password",password);
        editor.commit();
    }
    /*
     *读取用户帐号密码
     * */
    public static Bundle getUserInfo(Context context){
        Bundle bundle=new Bundle();
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserInfo",Context.MODE_PRIVATE);
        bundle.putString("user",sharedPreferences.getString("user",""));
        bundle.putString("user",sharedPreferences.getString("password",""));
        return bundle;
    }
    /*
     * 保存cookie
     * */
    public static void saveCookie(Context context,String cookie){
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cookie",cookie);
        editor.commit();
    }
    /*
     *读取Cookie
     * */
    public static String getCookie(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserInfo",Context.MODE_PRIVATE);
        return sharedPreferences.getString("Cookie","");
    }
}
