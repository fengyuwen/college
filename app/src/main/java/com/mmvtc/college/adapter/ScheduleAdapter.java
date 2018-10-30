package com.mmvtc.college.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mmvtc.college.bean.WeekBean;
import com.mmvtc.college.R;

import java.util.List;



public class ScheduleAdapter extends BaseAdapter {


    private List<WeekBean> weeks;
    private Context mContext;

    public ScheduleAdapter(Context mContext, List<WeekBean> weeks) {
        this.weeks = weeks;
        this.mContext = mContext;
    }

    public void setData(List<WeekBean> weeks) {
        this.weeks = weeks;
        notifyDataSetChanged();
    }

    //得到item的总数
    @Override
    public int getCount() {
        return weeks.size();
    }

    //得到item代表的对象
    @Override
    public Object getItem(int position) {
        return weeks.get(position);
    }

    //得到item的id
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mCurrentView = View.inflate(mContext, R.layout.schedule_item, null);
        TextView tv_monday = (TextView) mCurrentView.findViewById(R.id.tv_monday);
        TextView tv_thursday = (TextView) mCurrentView.findViewById(R.id.tv_thursday);
        TextView tv_wednesday = (TextView) mCurrentView.findViewById(R.id.tv_wednesday);
        TextView tv_tuesday = (TextView) mCurrentView.findViewById(R.id.tv_tuesday);
        TextView tv_friday = (TextView) mCurrentView.findViewById(R.id.tv_friday);

        tv_monday.setText(weeks.get(position).getMonday());
        tv_tuesday.setText(weeks.get(position).getTuesday());
        tv_wednesday.setText(weeks.get(position).getWednesday());
        tv_thursday.setText(weeks.get(position).getThursday());
        tv_friday.setText(weeks.get(position).getFriday());
        if (weeks.get(position).getMonday().isEmpty()) tv_monday.setAlpha(0);
        if (weeks.get(position).getTuesday().isEmpty()) tv_tuesday.setAlpha(0);
        if (weeks.get(position).getWednesday().isEmpty()) tv_wednesday.setAlpha(0);
        if (weeks.get(position).getThursday().isEmpty()) tv_thursday.setAlpha(0);
        if (weeks.get(position).getFriday().isEmpty()) tv_friday.setAlpha(0);
        return mCurrentView;
    }

}
