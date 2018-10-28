package com.mmvtc.college.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mmvtc.college.bean.UserInfoBean;
import com.mmvtc.college.R;
import com.mmvtc.college.utils.Local;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private List<UserInfoBean> lists=null;
    private ListView mListView;
    private MyBaseAdapter myAdapter;
    private Button btn_ret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_body);
        init();
    }

    private void init() {
        lists=new ArrayList();
        mListView = (ListView) findViewById(R.id.lv_info);
        btn_ret = (Button) findViewById(R.id.btn_ret);
        myAdapter = new MyBaseAdapter();
        mListView.setAdapter(myAdapter);
        btn_ret.setOnClickListener(this);
        initData();
    }

    private void initData() {
        new  Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url2 = Local.urls.get("个人信息");
                    Document  doc = Jsoup.connect(Local.url+url2)
                            .cookie("ASP.NET_SessionId",Local.cookie.substring(Local.cookie.indexOf("=") + 1, Local.cookie.length()))
                            .referrer("http://jwc.mmvtc.cn/xs_main.aspx?xh="+Local.number)
                            .timeout(5000)
                            .get();
                    Element body =doc.body();
                    Elements tds = body.getElementsByTag("td");
                    String key="";
                    String value="";
                    Elements elements=new Elements();
                    for (int i=0;i<tds.size();i++){
                        Element td =tds.get(i);
                        if (i==6||i==43||i==50||i==57||i==64||i==132||i==91){
                            continue;
                        }
                        elements.add(td);
                    }
                    for (int i=0;i<elements.size();i++){
                        Element td =elements.get(i);
                        if (i%2==0) {
                            key = td.text();

                        }
                        if (i%2==1){
                            value=td.text();
                            if (value.isEmpty()){
                                Element input =  td.getElementsByTag("input").first();
                               if (input!=null)value = input.attr("value");
                            }

                            if (!key.isEmpty()) {
                                UserInfoBean userInfo = new UserInfoBean();
                                userInfo.setKey(key);
                                userInfo.setValue(value);
                                lists.add(userInfo);
                            }
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       myAdapter.notifyDataSetChanged();
                   }
               });
            }
        }).start();

    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    private class MyBaseAdapter extends BaseAdapter {
        //得到item的总数
        @Override
        public int getCount() {
            return lists.size();
        }
        //得到item代表的对象
        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }
        //得到item的id
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(UserInfoActivity.this,R.layout.list_item,null);
            TextView mTextView = (TextView) view.findViewById(R.id.tv_item1);
            mTextView.setText(lists.get(position).getKey());
            TextView mtextView = (TextView) view.findViewById(R.id.tv_item2);
            mtextView.setText(lists.get(position).getValue());
            return view;
        }
    }

}