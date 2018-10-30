package com.mmvtc.college.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mmvtc.college.App;
import com.mmvtc.college.GlideImageLoader;
import com.mmvtc.college.R;
import com.mmvtc.college.activity.NewsActivity;
import com.mmvtc.college.adapter.CollegeNewsAdapter;
import com.mmvtc.college.bean.NewsBean;
import com.youth.banner.Banner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;




public class CollegeMessageFragment extends Fragment implements AdapterView.OnItemClickListener {

   @BindView(R.id.banner)
    Banner mBanner;
    private List<NewsBean> collegeNewsBeansItem2;
    private List<NewsBean> collegeNewsBeansItem1;
    private List<String> images;
    private CollegeNewsAdapter mCollegeNewsAdapter;
    private static final String TAG = "BaseFragment";

    @BindView(R.id.btn_news1)
    Button btnNews1;
    @BindView(R.id.btn_news2)
    Button btnNews2;
    @BindView(R.id.lv_content)
    ListView lvContent;
    @BindView(R.id.activity_college_massage)
    LinearLayout activityCollegeMassage;

    Unbinder unbinder;


    public static CollegeMessageFragment newInstance() {
        CollegeMessageFragment fragment = new CollegeMessageFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_college_massage, null);
        unbinder = ButterKnife.bind(this, view);
        init();
        initData();
        return view;
    }

    private void init() {
        collegeNewsBeansItem2 = new ArrayList<>();
        collegeNewsBeansItem1 = new ArrayList<>();
        images = new ArrayList<>();
        mCollegeNewsAdapter = new CollegeNewsAdapter(App.appContext, collegeNewsBeansItem2);
        lvContent.setAdapter(mCollegeNewsAdapter);
        lvContent.setOnItemClickListener(this);

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

    public void logoStart() {
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
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
                    NewsBean collegeNewsBean;
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
                            collegeNewsBean = new NewsBean();
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
                    //  Log.i(TAG, "run: " + news.html());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.btn_news1, R.id.btn_news2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_news1:
                handler.sendEmptyMessage(1);
                btnNews2.setBackgroundColor(Color.parseColor("#dddddd"));
                btnNews1.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case R.id.btn_news2:
                handler.sendEmptyMessage(2);
                btnNews1.setBackgroundColor(Color.parseColor("#dddddd"));
                btnNews2.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(App.appContext, NewsActivity.class);
        intent.putExtra("a", mCollegeNewsAdapter.getItem(i).getTextValue());
        startActivity(intent);
    }
}
