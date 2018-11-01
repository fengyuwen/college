package com.mmvtc.college.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mmvtc.college.App;
import com.mmvtc.college.bean.NewsBean;

import java.util.ArrayList;
import java.util.List;


public class CollegePagerAdapter extends PagerAdapter {
    private List<String> mDataList;
    private List<ListView> listViews;
    private List<CollegeNewsAdapter> adapters;

    private CollegeNewsAdapter adapter;
    private ListView listView;

    public CollegePagerAdapter(List<String> dataList) {
        mDataList = dataList;
        listViews = new ArrayList<>();
        adapters = new ArrayList<>();
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        for (int i = 0; i < dataList.size(); i++) {
            listView = new ListView(App.appContext);
            listView.setLayoutParams(lp);
            listViews.add(listView);
            adapters.add(new CollegeNewsAdapter(App.appContext, new ArrayList<NewsBean>()));
            listViews.get(i).setAdapter(adapters.get(i));
        }
    }

    public void setData(List<List<NewsBean>> newsBeansList) {
        for (int i = 0; i < newsBeansList.size(); i++) {
            adapters.get(i).setData(newsBeansList.get(i));
        }
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private static final String TAG = "ExamplePagerAdapter";

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(listViews.get(position));
        return listViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        // TextView textView = (TextView) object;
        // String text = textView.getText().toString();
        // int index = mDataList.indexOf(text);
        // if (index >= 0) {
        //     return index;
        // }
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDataList.get(position);
    }

}
