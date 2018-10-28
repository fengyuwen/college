package com.mmvtc.college.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmvtc.college.R;
import com.mmvtc.college.bean.CollegeNewsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/10/21.
 */

public class XiBuNewsAdapter extends BaseAdapter {

    private Context context;
    private List<CollegeNewsBean> collegeNewsBeens;

    public XiBuNewsAdapter(Context context, List<CollegeNewsBean> collegeNewsBeens) {
        this.context = context;
        this.collegeNewsBeens = collegeNewsBeens;
    }

    public void setData(List<CollegeNewsBean> collegeNewsBeens) {
        this.collegeNewsBeens = collegeNewsBeens;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return collegeNewsBeens.size();
    }

    @Override
    public CollegeNewsBean getItem(int i) {
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

        /*if (collegeNewsBeens.get(i).getTitleValue() != null) {
            holder.mLlStyle1.setVisibility(View.VISIBLE);
            holder.mLlStyle2.setVisibility(View.GONE);
            holder.mTvTitle.setText(collegeNewsBeens.get(i).getTitle());
            holder.mTvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "1234", Toast.LENGTH_SHORT).show();
                }
            });
        } else {*/
        //holder.mLlStyle1.setVisibility(View.GONE);
        holder.mLlStyle2.setVisibility(View.VISIBLE);
        holder.mTvTime.setText(collegeNewsBeens.get(i).getTime());

        holder.mIvIcon.setVisibility(View.GONE);
        holder.mTvEyeMeasure.setVisibility(View.GONE);

        holder.mTvText.setText(collegeNewsBeens.get(i).getText());

        //}

        return view;
    }


    static class ViewHolder {
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.ll_style1)
        LinearLayout mLlStyle1;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.iv_icon)
        ImageView mIvIcon;
        @BindView(R.id.tv_eye_measure)
        TextView mTvEyeMeasure;
        @BindView(R.id.tv_text)
        TextView mTvText;
        @BindView(R.id.ll_style2)
        LinearLayout mLlStyle2;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
