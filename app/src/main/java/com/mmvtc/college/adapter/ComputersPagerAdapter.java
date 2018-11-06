package com.mmvtc.college.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.mmvtc.college.App;
import com.mmvtc.college.bean.NewsBean;
import com.mmvtc.college.view.ShowHeadView;

import java.util.ArrayList;
import java.util.List;



public class ComputersPagerAdapter extends PagerAdapter{
    private List<String> mDataList;
    private List<ListView> listViews;
    private List<ComputersNewsAdapter> adapters;

    private ComputersNewsAdapter adapter;
    private ListView listView;
    private Context mContext;

    public ComputersPagerAdapter(List<String> dataList,Context context, final ShowHeadView showHeadView) {
        mDataList = dataList;
        mContext=context;
        listViews = new ArrayList<>();
        adapters = new ArrayList<>();
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        for (int i = 0; i < dataList.size(); i++) {
            listView=new ListView(App.appContext);
            listView.setLayoutParams(lp);
            listViews.add(listView);
            adapters.add(new ComputersNewsAdapter(mContext, new ArrayList<NewsBean>()));
            listViews.get(i).setAdapter(adapters.get(i));
            final int finalI = i;
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    switch (scrollState) {
                        // 当不滚动时
                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                            if (listViews.get(finalI).getFirstVisiblePosition() == 0)
                                showHeadView.ShowHeadView(true);
                            else
                                showHeadView.ShowHeadView(false);

                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                            // 判断滚动到顶部
                            if (listViews.get(finalI).getFirstVisiblePosition() == 0)
                                showHeadView.ShowHeadView(true);
                            else
                                showHeadView.ShowHeadView(false);

                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                            if (listViews.get(finalI).getFirstVisiblePosition() == 0)
                                showHeadView.ShowHeadView(true);
                            else
                                showHeadView.ShowHeadView(false);

                            break;
                    }

                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                }

            });
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
