package com.mmvtc.college.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
<<<<<<< HEAD
import android.widget.TextView;
=======
>>>>>>> 879ef399f8ccd7ed6978647c9a89b46f9187ec63


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



<<<<<<< HEAD
    private ListView lvText;
    private TextView tvTitle, tvMessage;
=======
    ListView lvText;

>>>>>>> 879ef399f8ccd7ed6978647c9a89b46f9187ec63
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
    }

    private void init() {
<<<<<<< HEAD
        lvText = (ListView) findViewById(R.id.lv_text);
        View view = View.inflate(this, R.layout.news_head, null);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvMessage = (TextView) view.findViewById(R.id.tv_message);
        lvText.addHeaderView(view);
                newContontBeans = new ArrayList<>();
        Intent intent = getIntent();
        a = intent.getStringExtra("a");
        String type = intent.getStringExtra("type");
        switch (type) {
            case "1":
                initData1();
                break;
            case "2":
                initData2();
                break;
            case "3":
                initData3();
                break;
        }

=======
        lvText= (ListView) findViewById(R.id.lv_text);
        a = getIntent().getStringExtra("a");
        newContontBeans = new ArrayList<>();
        newsAdapter = new NewsAdapter(newContontBeans, this);
        lvText.setAdapter(newsAdapter);
>>>>>>> 879ef399f8ccd7ed6978647c9a89b46f9187ec63
    }

    //处理学院新闻
    private void initData1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    newContontBeans.clear();
                    //获取网页数据
                    Document doc = Jsoup.connect(a)
                            .timeout(20 * 1000)
                            .get();
                    Element body = doc.body();
                    Element content = body.getElementsByClass("ali-ol-experiment-content").first();
                    if (content != null) {
                        title = body.getElementsByClass("ali-ol-experiment-title mt-20").first().text();
                        message = body.getElementsByClass("ali-ol-experiment-data mb-15").first().text();
                    } else {
                        content = body.getElementById("zoomcon");
                        title = body.getElementById("zoomtitl").text();
                        message = body.getElementById("zoomtime").text();
                    }
                    int size2 = content.children().size();
                    for (int i = 0; i < size2; i++) {
                        Element childNode = content.child(i);
                        addData(childNode.tagName(), childNode);

                    }
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //处理计算机新闻
    private void initData2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    newContontBeans.clear();
                    //获取网页数据
                    Document doc = Jsoup.connect(a)
<<<<<<< HEAD
                            .timeout(20 * 1000)
                            .get();
                    Element body = doc.body();
                    Element content = body.getElementsByClass("newsContent").first();
                    title = body.getElementsByClass("newsTitle").first().text();
                    message = "";


                    int size2 = content.children().size();
                    for (int i = 0; i < size2; i++) {
                        Element childNode = content.child(i);
                        addData(childNode.tagName(), childNode);
=======
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
>>>>>>> 879ef399f8ccd7ed6978647c9a89b46f9187ec63

                    }
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


<<<<<<< HEAD
    //处理土木新闻
    private void initData3() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //newContontBeans.clear();
                    //获取网页数据
                    Document doc = Jsoup.connect(a)
                            .timeout(20 * 1000)
                            .get();
                    Element body = doc.body();
                    Element content = body.getElementById("xiangxi");
                    title = body.getElementById("title").text();
                    message = body.getElementById("laiyuan").text();
                    int size2 = content.children().size();
                    for (int i = 0; i < size2; i++) {
                        Element childNode = content.child(i);
                        addData(childNode.tagName(), childNode);

                    }
                    handler.sendEmptyMessage(1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public void addData(String tagName, Element childNode) {
        switch (tagName) {
            case "a":
            case "p":
                Elements imgs = childNode.getElementsByTag("img");
                if (!imgs.isEmpty()) {
                    for (Element img : imgs) {
                        newContontBean = new NewContontBean();
                        newContontBean.setType(NewsAdapter.IMG);
                        String imgA = "";
                        if (img.attr("src").indexOf("http") == -1) {
                            imgA = a.substring(0, a.indexOf("cn/") + 2) + img.attr("src");
                        } else {
                            imgA = img.attr("src");
                        }
                        newContontBean.setText(imgA);
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
            default:
                for (int i = 0; i < childNode.children().size(); i++) {
                    Element childNode2 = childNode.child(i);
                    addData(childNode2.tagName(), childNode2);
                }
                break;
        }
    }


=======
>>>>>>> 879ef399f8ccd7ed6978647c9a89b46f9187ec63
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tvTitle.setText("" + title);
            tvMessage.setText("" + message);
            newsAdapter = new NewsAdapter(newContontBeans, NewsActivity.this);
            lvText.setAdapter(newsAdapter);

<<<<<<< HEAD
=======
            //tvTitle.setText("" + title);
            // tvMessage.setText(""+message);
             newsAdapter.setData(newContontBeans);
            // tvText.setText("\t\t"+newContontBean.getText());
>>>>>>> 879ef399f8ccd7ed6978647c9a89b46f9187ec63
        }
    };

}
