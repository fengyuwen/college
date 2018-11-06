package com.mmvtc.college.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmvtc.college.R;
import com.mmvtc.college.activity.NewsActivity;
import com.mmvtc.college.bean.NewsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/10/21.
 */

public class BulidingNewsAdapter extends BaseAdapter {

    private Context context;
    private List<NewsBean> collegeNewsBeens;


    public BulidingNewsAdapter(Context context, List<NewsBean> collegeNewsBeens) {
        this.context = context;
        this.collegeNewsBeens = collegeNewsBeens;
    }

    public void setData(List<NewsBean> collegeNewsBeens) {
        this.collegeNewsBeens = collegeNewsBeens;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return collegeNewsBeens.size();
    }

    @Override
    public NewsBean getItem(int i) {
        return collegeNewsBeens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = View.inflate(context, R.layout.computers_news_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.tvTime.setText(collegeNewsBeens.get(i).getTime());
        holder.tvText.setText(collegeNewsBeens.get(i).getText());
        final int i1 = i;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(App.appContext, "" + collegeNewsBeens.get(i1).getText(), Toast.LENGTH_SHORT).show();
                String a="";
                if (collegeNewsBeens.get(i1).getTextValue().indexOf("http")==-1){
                    a="http://www.mmvtc.cn" +collegeNewsBeens.get(i1).getTextValue();
                }else a=collegeNewsBeens.get(i1).getTextValue();
                context.startActivity(new Intent(context,NewsActivity.class).putExtra("type","3").putExtra("a",a));
            }
        });

        return view;
    }




    static class ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_text)
        TextView tvText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
