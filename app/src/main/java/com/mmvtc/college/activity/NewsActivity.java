package com.mmvtc.college.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ListView;

import com.mmvtc.college.R;
import com.mmvtc.college.adapter.NewsAdapter;
import com.mmvtc.college.bean.NewContontBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {



    ListView lvText;

    private String title = "";
    private String message = "";
    private String a = "";
    private NewContontBean newContontBean;
    private List<NewContontBean> newContontBeans;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_news);
        init();
        initData();
    }

    private void init() {
        lvText= (ListView) findViewById(R.id.lv_text);
        a = getIntent().getStringExtra("a");
        newContontBeans = new ArrayList<>();
        newsAdapter = new NewsAdapter(newContontBeans, this);
        lvText.setAdapter(newsAdapter);
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //获取网页数据
                    Document doc = Jsoup.connect(a)
                            .timeout(20*1000)
                            .post();
                    Element body = doc.body();
                    title = body.getElementsByClass("ali-ol-experiment-title mt-20").first().text();
                    message = body.getElementsByClass("ali-ol-experiment-data mb-15").first().text();
                    Element content = body.getElementsByClass("ali-ol-experiment-content").first();
                    //添加标题
                    newContontBean = new NewContontBean();
                    newContontBean.setType(NewsAdapter.P);
                    newContontBean.setText(title);
                    newContontBeans.add(newContontBean);
                    //添加时间作者等信息
                    newContontBean = new NewContontBean();
                    newContontBean.setType(NewsAdapter.P);
                    newContontBean.setText(message);
                    newContontBeans.add(newContontBean);

                    //content.childNodeSize();
                    int size =content.childNodeSize();
                    int size2 =content.children().size();

                    for (int i = 0; i < size2; i++) {
                        Element childNode = content.child(i);
                        switch (childNode.tagName()) {
                            case "p":
                                Elements imgs = childNode.getElementsByTag("img");
                                if (!imgs.isEmpty()) {
                                    for (Element img : imgs) {
                                        newContontBean = new NewContontBean();
                                        newContontBean.setType(NewsAdapter.IMG);
                                        newContontBean.setText(img.attr("src"));
                                        newContontBeans.add(newContontBean);
                                    }
                                } else {
                                    newContontBean = new NewContontBean();
                                    newContontBean.setType(NewsAdapter.P);
                                    newContontBean.setText(childNode.text());
                                    newContontBeans.add(newContontBean);
                                }
                                break;
                            case "iframe":
                                newContontBean = new NewContontBean();
                                newContontBean.setType(NewsAdapter.IFRAME);
                                newContontBean.setText(childNode.attr("src"));
                                newContontBeans.add(newContontBean);
                                break;

                        }

                    }


                    //newContontBean.setText(text);

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

            //tvTitle.setText("" + title);
            // tvMessage.setText(""+message);
             newsAdapter.setData(newContontBeans);
            // tvText.setText("\t\t"+newContontBean.getText());
        }
    };
}
