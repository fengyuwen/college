package com.mmvtc.college.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mmvtc.college.R;
import com.mmvtc.college.bean.GradeBean;
import com.mmvtc.college.utils.Local;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GradeActivity extends AppCompatActivity {
    private ListView mListView;
    private MyyBaseAdapter myAdapter;

    private List<GradeBean> scores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        scores = new ArrayList();
        //mInflater.inflate();
        mListView = (ListView) findViewById(R.id.lv_grade2);
        myAdapter = new MyyBaseAdapter();
        mListView.setAdapter(myAdapter);
        initData();
    }
    private void initData() {
        //创建一个子线程
        new  Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //从Local中获取教务网网址并和成绩查询网址拼接起来
                    String url2 = Local.urls.get("学习成绩查询");
                    Document doc = Jsoup.connect(Local.url+url2)
                            //获取登录状态
                            .cookie("ASP.NET_SessionId",Local.cookie.substring(Local.cookie.indexOf("=") + 1, Local.cookie.length()))
                            .referrer("http://jwc.mmvtc.cn/xs_main.aspx?xh="+Local.number)
                            .timeout(3000)//请求时间
                            .get();
                    Elements inputs = doc.getElementsByTag("input");
                    String viewState = "";
                    for (Element e:inputs){
                        String view = e.attr("name");
                        if (view.equals("__VIEWSTATE")){
                            viewState =e.attr("value");
                        }
                    }

                    Document doc2 = Jsoup.connect(Local.url+url2)
                            //发送请求码获取数据
                            .cookie("ASP.NET_SessionId",Local.cookie.substring(Local.cookie.indexOf("=") + 1, Local.cookie.length()))

                            .data("__EVENTTARGET","")
                            .data("__EVENTARGUMENT","")
                            .data("__VIEWSTATE",viewState)

                            .data("ddlXN","")
                            .data("ddlXQ","")
                            .data("ddl_kcxz","")

                            .data("btn_zcj","历年成绩")

                            .referrer("http://jwc.mmvtc.cn/xs_main.aspx?xh="+Local.number)
                            .timeout(3000)
                            .post();
                    //抓取网页上的信息
                    Element body =doc2.body();
                    Element tbody = body.getElementsByTag("table").get(1);
                    Elements trs =tbody.getElementsByTag("tr");
                    //创建JavaBean存储数据
                    GradeBean scoreBean;
                    //用for循环遍历出每个行和列中所需要的数据
                    for (Element tr:trs){
                        if (tr==trs.get(0))continue;
                        Elements tds =tr.getElementsByTag("td");
                        scoreBean=new GradeBean();
                        //用if判断语句过滤掉不需要的信息，并把所需要的学年、课程名称等存到scoreBean中。
                        for (int i=0;i<tds.size();i++){
                            if (i==2||i==4||i==5){
                                continue;
                            }
                            if (i==9)break;
                            Element td = tds.get(i);
                            String msg = td.text();
                            switch (i) {
                                case 0:
                                    scoreBean.setAcademicYear(msg);
                                    break;
                                case 1:
                                    scoreBean.setSemester(msg);
                                    break;
                                case 3:
                                    scoreBean.setCourseName(msg);
                                    break;
                                case 6:
                                    scoreBean.setCredits(msg);
                                    break;
                                case 7:
                                    scoreBean.setGradePoint(msg);
                                    break;
                                case 8:
                                    scoreBean.setResults(msg);
                                    break;
                            }
                        }
                        scores.add(scoreBean);
                    }

                    Collections.reverse(scores);
                    //返回主线程，更新列表
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myAdapter.notifyDataSetChanged();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    private class MyyBaseAdapter extends BaseAdapter {
        //得到item的总数
        @Override
        public int getCount() {
            return scores.size();
        }
        //得到item代表的对象
        @Override
        public Object getItem(int position) {
            return scores.get(position);
        }
        //得到item的id
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        //与set_grade布局文件连接，获取listview，并把数据填入到相应的位置上
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(GradeActivity.this,R.layout.grade,null);
            TextView mTextView1 = (TextView) view.findViewById(R.id.tv_item1);
            TextView mTextView2 = (TextView) view.findViewById(R.id.tv_item2);
            TextView mTextView3 = (TextView) view.findViewById(R.id.tv_item3);
            //TextView mTextView4 = (TextView) view.findViewById(R.id.tv_item4);
            TextView mTextView5 = (TextView) view.findViewById(R.id.tv_item5);
            TextView mTextView6 = (TextView) view.findViewById(R.id.tv_item6);
            mTextView1.setText(scores.get(position).getAcademicYear());
            mTextView2.setText(scores.get(position).getSemester());
            mTextView3.setText(scores.get(position).getCourseName());
            //mTextView4.setText(scores.get(position).getCredits());
            mTextView5.setText(scores.get(position).getGradePoint());
            mTextView6.setText(scores.get(position).getResults());
            return view;
        }
    }
}
