package com.mmvtc.college.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mmvtc.college.App;
import com.mmvtc.college.GlideImageLoader;
import com.mmvtc.college.R;
import com.mmvtc.college.adapter.BuildingPagerAdapter;
import com.mmvtc.college.bean.NewsBean;
import com.youth.banner.Banner;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class BuildingFragment extends Fragment {

    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private List<List<NewsBean>> NewsBeansList;
    private List<String> images;

    private static final String TAG = "BaseFragment";
    //标题栏
    private static final String[] CHANNELS = new String[]{"新闻动态", "教学科研", "招生就业","系部概况"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    private BuildingPagerAdapter buildingPagerAdapter;


    @BindView(R.id.activity_college_massage)
    LinearLayout activityCollegeMassage;

    Unbinder unbinder;


    public static BuildingFragment newInstance() {
        BuildingFragment fragment = new BuildingFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_computers, null);
        unbinder = ButterKnife.bind(this, view);
        init();
        initData();
        initMagicIndicator();
        return view;
    }


    private void init() {
        NewsBeansList = new ArrayList<>();
        images = new ArrayList<>();
        buildingPagerAdapter = new BuildingPagerAdapter(mDataList);
        viewPager.setAdapter(buildingPagerAdapter);
    }

    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(App.appContext);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);

                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);

                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setColors(Color.WHITE);
                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return UIUtil.dip2px(App.appContext, 15);
            }
        });
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    buildingPagerAdapter.setData(NewsBeansList);
                    logoStart();
                    break;
            }
        }
    };

    public void logoStart() {
        //轮播图片
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(images);
        mBanner.start();
    }


    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //清除上次获取数据
                    images.clear();
                    NewsBeansList.clear();
                    //获取轮播数据
                    Document doc = Jsoup.connect("http://www.mmvtc.cn/templet/tmgcx/")
                            .timeout(3000)
                            .post();
                    Element logo = doc.body().getElementsByClass("slides").first();

                    for (Element img : logo.getElementsByTag("img")) {
                        images.add("http://www.mmvtc.cn/templet/tmgcx/" + img.attr("src"));
                    }

                    //获得新闻动态
                    doc = Jsoup.connect("http://www.mmvtc.cn/templet/tmgcx/ShowClass.jsp?id=1273")
                            .timeout(3000)
                            .post();
                    addNewsList(doc,0);

                    //获得教学科研
                    doc = Jsoup.connect("http://www.mmvtc.cn/templet/tmgcx/ShowClass.jsp?id=2052")
                            .timeout(3000)
                            .post();
                    addNewsList(doc,1);

                    //获得教学科研
                    doc = Jsoup.connect("http://www.mmvtc.cn/templet/tmgcx/ShowClass.jsp?id=2053")
                            .timeout(3000)
                            .post();
                    addNewsList(doc,2);

                    //获得教学科研
                    doc = Jsoup.connect("http://www.mmvtc.cn/templet/tmgcx/ShowClass.jsp?id=2050")
                            .timeout(3000)
                            .post();
                    addNewsList(doc,3);

                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public void addNewsList(Document doc,int i){
        Elements lis = doc.body().getElementsByClass("info").first().getElementsByTag("li");
        List<NewsBean> list = new ArrayList<>();
        NewsBean newsBean;
        for (Element li : lis) {
            newsBean = new NewsBean();
            String title = CHANNELS[i];
            String text = li.getElementsByTag("a").first().text();
            String textValue = li.getElementsByTag("a").first().attr("href");
            String time = li.getElementsByTag("span").first().text();
            newsBean.setTitle(title);
            newsBean.setText(text);
            newsBean.setTextValue(textValue);
            newsBean.setTime(time);
            list.add(newsBean);
        }
        NewsBeansList.add(list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
