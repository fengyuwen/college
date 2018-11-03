package com.mmvtc.college.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmvtc.college.R;
import com.mmvtc.college.bean.NewsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/10/21.
 */

public class CollegeNewsAdapter extends BaseAdapter {

    private Context context;
    private List<NewsBean> collegeNewsBeens;

    public CollegeNewsAdapter(Context context, List<NewsBean> collegeNewsBeens) {
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
            view = View.inflate(context, R.layout.college_news_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

            holder.mLlStyle2.setVisibility(View.VISIBLE);
            holder.mTvTime.setText(collegeNewsBeens.get(i).getTime());
            holder.mTvEyeMeasure.setText(collegeNewsBeens.get(i).getEyeMeasure());
            holder.mTvText.setText(collegeNewsBeens.get(i).getText());

        return view;
    }


    static class ViewHolder {
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_text)
        TextView mTvText;
        @BindView(R.id.ll_style1)
        LinearLayout mLlStyle1;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_eye_measure)
        TextView mTvEyeMeasure;
        @BindView(R.id.ll_style2)
        LinearLayout mLlStyle2;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
