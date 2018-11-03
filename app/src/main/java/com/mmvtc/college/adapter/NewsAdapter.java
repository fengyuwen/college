package com.mmvtc.college.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mmvtc.college.bean.NewContontBean;

import java.util.List;

public class NewsAdapter extends BaseAdapter {
    public final static int P = 0;
    public final static int IFRAME = 1;
    public final static int IMG = 2;
    private List<NewContontBean> contontBeans;
    private Context context;

    public NewsAdapter(List<NewContontBean> contontBeans, Context context) {
        this.contontBeans = contontBeans;
        this.context = context;
    }

    public void setData(List<NewContontBean> contontBeans){
        this.contontBeans = contontBeans;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return contontBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return contontBeans.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public Object getItem(int i) {
        return contontBeans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        switch (getItemViewType(i)) {
            case P:
                view=new TextView(context);
                ((TextView) view).setText("  "+contontBeans.get(i).getText());
                break;
            case IFRAME:
                view=new TextView(context);
                ((TextView) view).setText("  "+contontBeans.get(i).getText());
                break;
            case IMG:
                view=new ImageView(context);
                ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(context).load(contontBeans.get(i).getText()).into((ImageView) view);
                break;
        }

        return view;
    }
}
