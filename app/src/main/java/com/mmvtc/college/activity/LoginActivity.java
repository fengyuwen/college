package com.mmvtc.college.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mmvtc.college.R;
import com.mmvtc.college.Utils.Local;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    String url = "";
    private ImageView ivVertify;
    private String user, passwrod, vertift;
    private EditText etUser, etPasswrod, etVertify;
    private String cookie = "";
    private Button btnLogin;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        picUpdate();
    }

    private void init() {
        ivVertify = (ImageView) findViewById(R.id.iv_vertify);
        etVertify = (EditText) findViewById(R.id.et_vertify);
        etUser = (EditText) findViewById(R.id.et_user);
        etPasswrod = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        ivVertify.setOnClickListener(this);
    }

    public void picUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://jwc.mmvtc.cn/CheckCode.aspx");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    if (!cookie.isEmpty())
                        conn.setRequestProperty("Cookie", cookie);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        if (cookie.isEmpty()) {
                            cookie = conn.getHeaderField("Set-Cookie");
                            Log.i(TAG, "run: " + cookie);
                            cookie = cookie.substring(0, cookie.indexOf(";"));
                        }
                        InputStream is = conn.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(is);
                        is.close();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivVertify.setImageBitmap(bitmap);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        user = etUser.getText().toString().trim();
        passwrod = etPasswrod.getText().toString().trim();
        vertift = etVertify.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_vertify:
                picUpdate();
                break;
            case R.id.btn_login:
                //需要改良
                if (user.isEmpty() || passwrod.isEmpty() || vertift.isEmpty()) {
                    Toast.makeText(this, "账号或秘密或验证码不能空", Toast.LENGTH_SHORT).show();
                } else
                    login();
                break;
        }
    }

    public void login() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(Local.url + "default2.aspx")
                            .cookie("ASP.NET_SessionId", cookie.substring(cookie.indexOf("=") + 1, cookie.length()))
                            .data("__VIEWSTATE", "dDw3OTkxMjIwNTU7Oz5qFv56B08dbR82AMSOW+P8WDKexA==")
                            .data("TextBox1", user)
                            .data("TextBox2", passwrod)
                            .data("TextBox3", vertift)
                            .data("RadioButtonList1", "学生")
                            .data("Button1", "")
                            .timeout(3000)
                            .post();
                    Element body = doc.body();
                    Element script = doc.getElementsByTag("script").last();
                    final String msg =  massage(script.html());
                    if (msg.isEmpty()){
                      Elements as = body.getElementsByTag("a");
                      for (Element a :as){
                          Local.urls.put(a.text(),a.attr("href"));
                      }
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        LoginActivity.this.finish();
                  }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    Log.i(TAG, "run: " + body.text() + ":" + cookie);
                    picUpdate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private String massage(String msg){
        String str="";
        if (msg.indexOf("密码错误")!=-1){
            str="如忘记密码，请与教务处联系！";
        } else if (msg.indexOf("验证码不正确")!=-1){
            str="验证码不正确";
        }
        return str;
   }

}
