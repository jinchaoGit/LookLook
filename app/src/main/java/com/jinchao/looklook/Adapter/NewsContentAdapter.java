package com.jinchao.looklook.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinchao.looklook.Model.NewsContent;
import com.jinchao.looklook.R;

import java.util.List;

/**
 * Created by ljc on 18-6-30.
 */

public class NewsContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NewsContent> newsContentList;
    private Context context;

    public NewsContentAdapter(Context context, List<NewsContent> newsContentList) {
        this.context = context;
        this.newsContentList = newsContentList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType==0){
            view = LayoutInflater.from(context).inflate(R.layout.item_newscontent_text,parent, false);
            return new ContentTextHolder(view);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.item_newscontent_image,parent, false);
            return new ContentImageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (newsContentList.get(position).getType() == 1 && holder instanceof ContentImageHolder) {
            Glide.with(context).load(newsContentList.get(position).getValue()).into(((ContentImageHolder) holder).imageView);
        } else if (holder instanceof ContentTextHolder) {
            ((ContentTextHolder) holder).textView.setText(newsContentList.get(position).getValue());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return newsContentList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return newsContentList.size();
    }

    class ContentImageHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ContentImageHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_news_content_image);
        }
    }

    class ContentTextHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ContentTextHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_news_content_text);
        }
    }

}
