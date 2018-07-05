package com.mmvtc.college.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mmvtc.college.R;
import com.mmvtc.college.Utils.Local;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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
        setContentView(R.layout.activity_logint);
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
                    if (!cookie.isEmpty()) conn.setRequestProperty("Cookie", cookie);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        if (cookie.isEmpty()) {
                            cookie = conn.getHeaderField("Set-Cookie");
                            cookie = cookie.substring(cookie.indexOf("=")+1, cookie.indexOf(";"));
                            Local.saveCookie(MainActivity.this, cookie);
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
                login();
                picUpdate();
                break;
        }
    }

    public void login() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*try {
                    URL url = new URL(Local.url+"default2.aspx");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(5000);
                    String data = "__VIEWSTATE=" + URLEncoder.encode("dDw3OTkxMjIwNTU7Oz5qFv56B08dbR82AMSOW+P8WDKexA==")
                            + "&TextBox1=" + URLEncoder.encode(user)
                            + "&TextBox2=" + URLEncoder.encode(passwrod)
                            + "&TextBox3=" + URLEncoder.encode(vertift)
                            + "&RadioButtonList1=" + URLEncoder.encode("学生")
                            + "&Button1=" + "";
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Cookie", cookie);
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    os.write(data.getBytes());
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        final String result = new String(Local.read(is), "gb2312");
                        Log.i("tags", "run: " + result);
                        String s = result;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
/*Document doc = Jsoup.connect("http://jwc.mmvtc.cn/default2.aspx")
                            .data("TextBox1", "Java")
                            .data("TextBox2", "Java")
                            .userAgent("I’mjsoup")
                            .cookie("auth", "token")
                            .timeout(3000)
                            .post();
                    Element body = doc.body();
                    Log.i("tags", ":"+body.text());*/
                //http://jwc.mmvtc.cn/CheckCode.aspx
                try {
                    Document doc = Jsoup.connect(Local.url+"default2.aspx")
                            .cookie("ASP.NET_SessionId",cookie)
                            .data("__VIEWSTATE","dDw3OTkxMjIwNTU7Oz5qFv56B08dbR82AMSOW+P8WDKexA==")
                            .data("TextBox1",user)
                            .data("TextBox2",passwrod)
                            .data("TextBox3",vertift)
                            .data("RadioButtonList1","学生")
                            .data("Button1","")
                            .timeout(3000)
                            .post();
                    Element body = doc.body();
                    Elements a = body.getElementsByTag("a");
                    Log.i(TAG, "run: "+body.text());
              
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }).start();

    }


}
