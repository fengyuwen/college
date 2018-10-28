package com.mmvtc.college.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mmvtc.college.bean.WeekBean;
import com.mmvtc.college.R;
import com.mmvtc.college.utils.Local;
import com.mmvtc.college.adapter.ScheduleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class CourseView {
    private Activity mContext;
    private LayoutInflater mInflater;
    private View mCurrentView;
    private ScheduleAdapter scheduleAdapter;
    private List<WeekBean> courses = new ArrayList();

    private TextView tv_monday, tv_tuesday, tv_wednesday, tv_thursday, tv_friday;

    private ListView lv_schedule;

    public CourseView(Activity context) {
        mContext = context;
        //为之后将Layout转化为view时用
        mInflater = LayoutInflater.from(mContext);
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url2 = Local.urls.get("班级课表查询");
                    Document doc = Jsoup.connect(Local.url + url2)
                            .cookie("ASP.NET_SessionId", Local.cookie.substring(Local.cookie.indexOf("=") + 1, Local.cookie.length()))
                            .referrer("http://jwc.mmvtc.cn/xs_main.aspx?xh=" + Local.number)
                            .timeout(3000)
                            .post();
                    Element body = doc.body();
                    Element table = body.getElementById("Table6");
                    Elements trs = table.getElementsByTag("tr");
                    WeekBean weekBean;
                    for (int i = 0; i < trs.size(); i++) {
                        if (i == 0 || i == 1 || i == 3 || i == 5 || i == 7 || i == 9 || i == 11)
                            continue;
                        Element tr = trs.get(i);
                        Elements tds = tr.getElementsByTag("td");
                        weekBean = new WeekBean();
                        for (int j = 0; j < tds.size(); j++) {
                            String msg = tds.get(j).text();
                            if (i == 2 || i == 6 || i == 10)
                                switch (j) {
                                    case 2:
                                        weekBean.setMonday(msg);
                                        break;
                                    case 3:
                                        weekBean.setTuesday(msg);
                                        break;
                                    case 4:
                                        weekBean.setWednesday(msg);
                                        break;
                                    case 5:
                                        weekBean.setThursday(msg);
                                        break;
                                    case 6:
                                        weekBean.setFriday(msg);
                                        break;
                                }
                            else
                                switch (j) {
                                    case 1:
                                        weekBean.setMonday(msg);
                                        break;
                                    case 2:
                                        weekBean.setTuesday(msg);
                                        break;
                                    case 3:
                                        weekBean.setWednesday(msg);
                                        break;
                                    case 4:
                                        weekBean.setThursday(msg);
                                        break;
                                    case 5:
                                        weekBean.setFriday(msg);
                                        break;
                                }
                        }
                        courses.add(weekBean);
                    }
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scheduleAdapter.setData(courses);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void createView() {
        initView();

    }

    /**
     * 获取界面控件
     */
    private void initView() {
        //设置布局文件
        mCurrentView = mInflater.inflate(R.layout.activity_schedule, null);
        tv_friday = (TextView) mCurrentView.findViewById(R.id.tv_friday);
        tv_monday = (TextView) mCurrentView.findViewById(R.id.tv_monday);
        tv_thursday = (TextView) mCurrentView.findViewById(R.id.tv_thursday);
        tv_tuesday = (TextView) mCurrentView.findViewById(R.id.tv_tuesday);
        tv_wednesday = (TextView) mCurrentView.findViewById(R.id.tv_wednesday);
        lv_schedule = (ListView) mCurrentView.findViewById(R.id.lv_schedule);
        scheduleAdapter = new ScheduleAdapter(mContext, courses);
        lv_schedule.setAdapter(scheduleAdapter);
        initData();
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


}