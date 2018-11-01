package com.mmvtc.college.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mmvtc.college.R;
import com.mmvtc.college.utils.Local;
import com.mmvtc.college.activity.LoginActivity;
import com.mmvtc.college.activity.ModifyPsw;
import com.mmvtc.college.activity.UserInfoActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;
import static com.mmvtc.college.utils.Local.cookie;

public class MyInfoView implements View.OnClickListener {
    public ImageView iv_head_icon;
    private LinearLayout ll_head;
    private RelativeLayout rl_course_history, rl_setting;
    private TextView tv_user_name;
    private Activity mContext;
    private LayoutInflater mInflater;
    private View mCurrentView;
    private String headIconUrl;
    public MyInfoView(Activity context) {
        mContext = context;
        //为之后将Layout转化为view时用
        mInflater = LayoutInflater.from(mContext);
    }

    private void createView() {
        initView();
    }

    /**
     * 获取界面控件
     */
    private void initView() {
        //设置布局文件
        mCurrentView = mInflater.inflate(R.layout.main_view_myinfo, null);
        ll_head = (LinearLayout) mCurrentView.findViewById(R.id.ll_head);
        iv_head_icon = (ImageView) mCurrentView.findViewById(R.id.iv_head_icon);
        rl_course_history = (RelativeLayout) mCurrentView.findViewById(R.id.rl_course_history);
        rl_setting = (RelativeLayout) mCurrentView.findViewById(R.id.rl_setting);
        tv_user_name = (TextView) mCurrentView.findViewById(R.id.tv_user_name);
        mCurrentView.setVisibility(View.VISIBLE);
        tv_user_name.setText(readUser());
        ll_head.setOnClickListener(this);
        rl_course_history.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        initHeadIcon();
    }

    private void initHeadIcon() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url2 = Local.urls.get("个人信息");
                    Document doc = Jsoup.connect(Local.url + url2)
                            .cookie("ASP.NET_SessionId", cookie.substring(cookie.indexOf("=") + 1, cookie.length()))
                            .referrer("http://jwc.mmvtc.cn/xs_main.aspx?xh=" + Local.number)
                            .timeout(5000)
                            .get();
                    Element body = doc.body();
                    Element headIcon = body.getElementById("xszp");
                    headIconUrl = headIcon.attr("src");
                    updateHeadIcon();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void updateHeadIcon(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(Local.url+headIconUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    if (!cookie.isEmpty())
                        conn.setRequestProperty("Cookie", cookie);
                    final int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(is);
                        is.close();
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv_head_icon.setImageBitmap(bitmap);
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
        switch (view.getId()) {
            case R.id.ll_head:
                mContext.startActivity(new Intent(mContext, UserInfoActivity.class));
                break;
            case R.id.rl_course_history:
                mContext.startActivity(new Intent(mContext, ModifyPsw.class));
                break;
            case R.id.rl_setting:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Document doc = Jsoup.connect("http://jwc.mmvtc.cn/xs_main.aspx?xh=" + Local.number)
                                    .cookie("ASP.NET_SessionId", cookie.substring(cookie.indexOf("=") + 1, cookie.length()))
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
                            Document doc2 = Jsoup.connect("http://jwc.mmvtc.cn/xs_main.aspx?xh=" + Local.number)
                                    .cookie("ASP.NET_SessionId", cookie.substring(cookie.indexOf("=") + 1, cookie.length()))
                                    .data("__EVENTTARGET", "likTc")
                                    .data("__EVENTARGUMENT", "")
                                    .data("__VIEWSTATE", viewState)
                                    .referrer("http://jwc.mmvtc.cn/xs_main.aspx?xh=" + Local.number)
                                    .timeout(3000)
                                    .post();
                            Log.i(TAG, "run: " + doc2.html());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        mContext.finish();
                    }
                }).start();
                break;
        }
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
    public void showView() {
        if (mCurrentView == null) {
            createView();
        }
        mCurrentView.setVisibility(View.VISIBLE);
    }

    /**
     * 从SharedPreferences中读取用户名
     */
    private String readUser() {
        SharedPreferences sp = mContext.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String user = sp.getString("user", "");
        return user;
    }


}