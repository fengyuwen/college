package com.mmvtc.college.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.mmvtc.college.R;
import com.mmvtc.college.bean.NewContontBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity {

    @BindView(R.id.tv_text)
    TextView tvText;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    String a = "";
    @BindView(R.id.tv_message)
    TextView tvMessage;
    NewContontBean newContontBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        init();
        initData();
    }

    private void init() {
        newContontBean = new NewContontBean();
        Intent intent = getIntent();
        a = intent.getStringExtra("a");
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //获取网页数据
                    Document doc = Jsoup.connect("http://www.mmvtc.cn" + a)
                            .timeout(3000)
                            .post();
                    Element body = doc.body();
                    String title = body.getElementsByClass("ali-ol-experiment-title mt-20").first().text();
                    String message = body.getElementsByClass("ali-ol-experiment-data mb-15").first().text();
                    String text = body.getElementsByClass("ali-ol-experiment-content").first().text();


                    newContontBean.setTitle(title);
                    newContontBean.setMessage(message);
                    newContontBean.setText(text);

                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            tvText.setText("\t\t"+newContontBean.getText());
            tvTitle.setText(""+newContontBean.getTitle());
            tvMessage.setText(""+newContontBean.getMessage());
        }
    };
}
