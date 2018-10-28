package com.mmvtc.college.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mmvtc.college.R;
import com.mmvtc.college.utils.Local;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ModifyPsw extends AppCompatActivity implements View.OnClickListener {
    private TextView tvUserName,tvBack;
    private EditText etOriginalPsw;
    private EditText etNewPsw;
    private EditText etNewPswAgain;
    private Button btnCommit;
    private String userName, originalPsw, newPsw, newPswAgain;
    private static final String TAG = "ModifyPsw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_psw);
        init();
    }

    private void init() {
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        etOriginalPsw = (EditText) findViewById(R.id.et_original_psw);
        etNewPsw = (EditText) findViewById(R.id.et_new_psw);
        etNewPswAgain = (EditText) findViewById(R.id.et_new_psw_again);
        btnCommit = (Button) findViewById(R.id.btn_commit);
        tvBack= (TextView) findViewById(R.id.tv_back);
        btnCommit.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        tvUserName.setText(Local.number);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.tv_back){
            finish();
        }else {
            originalPsw = etOriginalPsw.getText().toString().trim();
            newPsw = etNewPsw.getText().toString().trim();
            newPswAgain = etNewPswAgain.getText().toString().trim();
            if (!newPsw.equals(newPswAgain)) {
                Toast.makeText(this, "两次新密码不一样", Toast.LENGTH_SHORT).show();
                return;
            }
            if (newPsw.equals("000000") || newPsw.equals("123456")) {
                Toast.makeText(this, "您的密码安全性较低，请重新输入！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (newPsw.length() < 6 || newPsw.length() < 6) {
                Toast.makeText(this, "密码长度不能小于 6 位！", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String url2 = Local.urls.get("密码修改");
                        Document doc = Jsoup.connect(Local.url + url2)
                                .cookie("ASP.NET_SessionId", Local.cookie.substring(Local.cookie.indexOf("=") + 1, Local.cookie.length()))
                                .referrer("http://jwc.mmvtc.cn/xs_main.aspx?xh=" + Local.number)
                                .timeout(3000)
                                .get();
                        Elements inputs = doc.getElementsByTag("input");
                        String viewState = "";
                        for (Element e : inputs) {
                            String view = e.attr("name");
                            if (view.equals("__VIEWSTATE")) {
                                viewState = e.attr("value");
                            }
                        }

                        final Document doc2 = Jsoup.connect(Local.url + url2)
                                .cookie("ASP.NET_SessionId", Local.cookie.substring(Local.cookie.indexOf("=") + 1, Local.cookie.length()))
                                .referrer("http://jwc.mmvtc.cn/xs_main.aspx?xh=" + Local.number)
                                .data("__VIEWSTATE", viewState)
                                .data("Button1", "修  改")
                                .data("TextBox2", originalPsw)
                                .data("TextBox3", newPsw)
                                .data("TextBox4", newPswAgain)
                                .timeout(3000)
                                .post();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (doc2.html().indexOf("修改成功") != -1) {
                                    Toast.makeText(ModifyPsw.this, "修改成功!", Toast.LENGTH_SHORT).show();
                                    ModifyPsw.this.finish();
                                } else if (doc2.html().indexOf("旧密码不正确") != -1) {
                                    Toast.makeText(ModifyPsw.this, "旧密码不正确!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

}
