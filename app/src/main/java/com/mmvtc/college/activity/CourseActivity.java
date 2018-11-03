package com.mmvtc.college.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.mmvtc.college.R;
import com.mmvtc.college.adapter.ScheduleAdapter;
import com.mmvtc.college.bean.WeekBean;
import com.mmvtc.college.utils.Local;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {


    private ScheduleAdapter scheduleAdapter;
    private List<WeekBean> courses = new ArrayList();

    private TextView tv_monday, tv_tuesday, tv_wednesday, tv_thursday, tv_friday;

    private ListView lv_schedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        init();
    }

    private void init() {

        tv_friday = (TextView) findViewById(R.id.tv_friday);
        tv_monday = (TextView) findViewById(R.id.tv_monday);
        tv_thursday = (TextView) findViewById(R.id.tv_thursday);
        tv_tuesday = (TextView) findViewById(R.id.tv_tuesday);
        tv_wednesday = (TextView) findViewById(R.id.tv_wednesday);
        lv_schedule = (ListView) findViewById(R.id.lv_schedule);
        scheduleAdapter = new ScheduleAdapter(this, courses);
        lv_schedule.setAdapter(scheduleAdapter);
        initData();
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
                    runOnUiThread(new Runnable() {
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
}
