package com.mmvtc.college.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mmvtc.college.R;

=======
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
>>>>>>> 879ef399f8ccd7ed6978647c9a89b46f9187ec63
import com.mmvtc.college.bean.NewContontBean;

import java.util.List;

<<<<<<< HEAD

import butterknife.BindView;
import butterknife.ButterKnife;

=======
>>>>>>> 879ef399f8ccd7ed6978647c9a89b46f9187ec63
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

<<<<<<< HEAD

    public void setData(List<NewContontBean> contontBeans) {

=======
    public void setData(List<NewContontBean> contontBeans){
>>>>>>> 879ef399f8ccd7ed6978647c9a89b46f9187ec63
        this.contontBeans = contontBeans;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
<<<<<<< HEAD

        return contontBeans == null ? 0 : contontBeans.size();

=======
        return contontBeans.size();
>>>>>>> 879ef399f8ccd7ed6978647c9a89b46f9187ec63
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
<<<<<<< HEAD

        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = View.inflate(context, R.layout.news_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder.mIvImg.setVisibility(View.GONE);
        holder.mTvText.setVisibility(View.GONE);
        holder.mWvVideo.setVisibility(View.GONE);
        switch (getItemViewType(i)) {
            case P:
                holder.mTvText.setVisibility(View.VISIBLE);
                holder.mTvText.setText("\t\t" + contontBeans.get(i).getText() + "\n");
                break;
            case IFRAME:
                holder.mWvVideo.setVisibility(View.VISIBLE);
                String html1 = "<iframe frameborder=\"0\" width=\"1080\" height=\"1000\" src=\"" + contontBeans.get(i).getText() + "\" allowfullscreen></iframe>";
                holder.mWvVideo.getSettings().setJavaScriptEnabled(true);
                holder.mWvVideo.getSettings().setPluginState(WebSettings.PluginState.ON);
                holder.mWvVideo.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                holder.mWvVideo.getSettings().setAllowFileAccess(true);
                holder.mWvVideo.getSettings().setDefaultTextEncodingName("UTF-8");
                holder.mWvVideo.getSettings().setLoadWithOverviewMode(true);
                holder.mWvVideo.getSettings().setUseWideViewPort(true);
                holder.mWvVideo.getSettings().setDomStorageEnabled(true);
                holder.mWvVideo.loadData(html1, "text/html", "UTF-8");

                break;
            case IMG:
                holder.mIvImg.setVisibility(View.VISIBLE);
                Glide.with(context).load(contontBeans.get(i).getText()).into(holder.mIvImg);

=======
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
>>>>>>> 879ef399f8ccd7ed6978647c9a89b46f9187ec63
                break;
        }

        return view;
    }
<<<<<<< HEAD



    static class ViewHolder {
        @BindView(R.id.tv_text)
        TextView mTvText;
        @BindView(R.id.iv_img)
        ImageView mIvImg;
        @BindView(R.id.wv_video)
        WebView mWvVideo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

=======
>>>>>>> 879ef399f8ccd7ed6978647c9a89b46f9187ec63
}
