package com.mmvtc.college.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.mmvtc.college.R;
import com.mmvtc.college.adapter.CollegeNewsAdapter;
import com.mmvtc.college.bean.CollegeNewsBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollegeMessage extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "CollegeMessage";

    @BindView(R.id.lv_content)
    ListView mLvContent;
    @BindView(R.id.activity_college_massage)
    LinearLayout mActivityCollegeMassage;
    @BindView(R.id.btn_news1)
    Button mBtnNews1;
    @BindView(R.id.btn_news2)
    Button mBtnNews2;
    @BindView(R.id.iv_logo)
    ImageView mIvLogo;
    Timer timer;

    private List<CollegeNewsBean> collegeNewsBeansItem2;
    private List<CollegeNewsBean> collegeNewsBeansItem1;
    private List<String> images;
    private CollegeNewsAdapter mCollegeNewsAdapter;
    private int logoIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_college_massage);
        ButterKnife.bind(this);
        init();
        initData();
    }

    private void init() {
        collegeNewsBeansItem2 = new ArrayList<>();
        collegeNewsBeansItem1 = new ArrayList<>();
        images = new ArrayList<>();
        mCollegeNewsAdapter = new CollegeNewsAdapter(this, collegeNewsBeansItem2);
        mLvContent.setAdapter(mCollegeNewsAdapter);
        mLvContent.setOnItemClickListener(this);
    }
    /*
    * 轮播时间
    * */
    Handler logo = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Glide.with(CollegeMessage.this).load(images.get(msg.what)).into(mIvLogo);
        }
    };
    public void logoStart() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                logoIndex%=images.size();
                logo.sendEmptyMessage(logoIndex);
                Log.i(TAG, "run: "+logoIndex);
                logoIndex++;
                //if (++logoIndex==images.size()) logoIndex=0;
            }
        };
        timer.schedule(timerTask,1000,3000);//延时1s，每隔500毫秒执行一次run方法
    }


    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //清除上次获取数据
                    collegeNewsBeansItem1.clear();
                    collegeNewsBeansItem2.clear();
                    //获取网页数据
                    Document doc = Jsoup.connect("http://www.mmvtc.cn/templet/default/index.jsp")
                            .timeout(3000)
                            .post();
                    Element body = doc.body();
                    Element logo = body.getElementById("owl-demo");
                    for (Element img : logo.getElementsByTag("img")) {
                        images.add("http://www.mmvtc.cn/templet/default/" + img.attr("src"));
                    }


                    Elements news = body.getElementsByClass("news");
                    CollegeNewsBean collegeNewsBean;
                    for (Element e : news) {
                        //获取新闻分类标题
                        String title = e.getElementsByClass("title").text();
                        //获取更多新闻的跳转链接
                        String titleValue = e.getElementsByClass("title").first().getElementsByTag("a").attr("href");
                        //添加分类标题信息到集合
                        //collegeNewsBean = new CollegeNewsBean();
                        //collegeNewsBean.setTitle(title);
                        //collegeNewsBean.setTitleValue(titleValue);
                        //collegeNewsBeansItem2.add(collegeNewsBean);
                        //获取新闻对象
                        Elements lis = e.getElementsByTag("li");
                        for (Element li : lis) {
                            String time = li.getElementsByTag("time").text();
                            String eyeMeasure = li.getElementsByClass("pv").text();
                            String text = li.getElementsByTag("a").first().text();
                            String textValue = li.getElementsByTag("a").first().attr("href");
                            //创建学院新闻类
                            collegeNewsBean = new CollegeNewsBean();
                            collegeNewsBean.setTitle(title);
                            //通过TitleValue是否存在，识别是否新闻信息
                            collegeNewsBean.setTitleValue(titleValue);
                            collegeNewsBean.setTime(time);
                            collegeNewsBean.setEyeMeasure(eyeMeasure);
                            collegeNewsBean.setText(text);
                            collegeNewsBean.setTextValue(textValue);
                            //添加新闻到新闻集合
                            //collegeNewsBeans.add(collegeNewsBean);
                            if (title.indexOf("学院新闻") != -1) {
                                collegeNewsBeansItem1.add(collegeNewsBean);
                            } else {
                                if (title.indexOf("通知公告") != -1)
                                    collegeNewsBeansItem2.add(collegeNewsBean);
                            }
                        }

                    }


                    // Message msg = new Message();
                    // msg.obj = news.html();
                    // handler.sendMessage(msg);
                    handler.sendEmptyMessage(1);
                    Log.i(TAG, "run: " + news.html());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mCollegeNewsAdapter.setData(collegeNewsBeansItem1);
                    break;
                case 2:
                    mCollegeNewsAdapter.setData(collegeNewsBeansItem2);
                    break;
            }
            logoStart();
        }
    };

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop: ");
        super.onStop();
    }

    @OnClick({R.id.btn_news1, R.id.btn_news2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_news1:
                handler.sendEmptyMessage(1);
                mBtnNews2.setBackgroundColor(Color.parseColor("#dddddd"));
                mBtnNews1.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case R.id.btn_news2:
                handler.sendEmptyMessage(2);
                mBtnNews1.setBackgroundColor(Color.parseColor("#dddddd"));
                mBtnNews2.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(this,NewsActivity.class);
        intent.putExtra("a",mCollegeNewsAdapter.getItem(i).getTextValue());
        startActivity(intent);
    }
}
