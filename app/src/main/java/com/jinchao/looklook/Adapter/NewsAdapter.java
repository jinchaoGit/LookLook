package com.jinchao.looklook.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinchao.looklook.Model.News;
import com.jinchao.looklook.R;

import java.util.List;

/**
 * Created by ljc on 18-5-22.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> implements View.OnClickListener {

    private static final int TYPE_HEAD = 0;
    private static final int TYPE_NORMAL = 1;
    private Context mContext;
    private List<News> mNews;

    private OnItemClickListener mOnItemClickListener;

//    private View mBanner;

    public NewsAdapter(Context context, List<News> news) {
        this.mContext = context;
        this.mNews = news;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (mBanner != null && viewType == 0)
//            return new NewsHolder(mBanner);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_info, parent,false);
        view.setOnClickListener(this);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
//        if (position ==0 )
//            return;
        Glide.with(mContext).load(mNews.get(position).getNews_imageUrl()).into(holder.imageView);
        holder.news_title.setText(mNews.get(position).getNews_title());
        holder.news_summary.setText(mNews.get(position).getNews_summary());
        holder.itemView.setTag(position);
//        holder.news_time.setText(mNews.get(position).getNews_time());
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

//    @Override
//    public int getItemViewType(int position) {
////        if (mBanner ==null)
////            return TYPE_NORMAL;
////        if (position == 0)
////            return TYPE_HEAD;
//        return TYPE_NORMAL;
//    }

    @Override
    public void onClick(View v){
        if (mOnItemClickListener!=null){
            mOnItemClickListener.onItemClick((Integer)v.getTag());
        }
    }

//    public void setmBanner(View view) {
//        mBanner = view;
//        notifyItemInserted(0);
//        notifyDataSetChanged();
//    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        mOnItemClickListener = itemClickListener;
    }
    class NewsHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView news_title;
        private TextView news_summary;
        private TextView news_time;

        public NewsHolder(View itemView) {
            super(itemView);
//            if (itemView ==mBanner)
//                return;
            imageView = (ImageView) itemView.findViewById(R.id.item_news_image);
            news_title = (TextView) itemView.findViewById(R.id.item_news_title);
            news_summary = (TextView) itemView.findViewById(R.id.item_news_summary);
//            news_time = (TextView) itemView.findViewById(R.id.item_news_time);
        }
    }
}