package com.jinchao.looklook.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.jinchao.looklook.Model.FilmSearchResult;
import com.jinchao.looklook.R;

import java.util.List;

/**
 * Created by ljc on 18-5-24.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ResultHolder> implements View.OnClickListener {

    private Context context;
    private List<FilmSearchResult> results;
    private OnItemClickListener onItemClickListener;

    public SearchResultAdapter(Context context, List<FilmSearchResult> results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result_info, parent,false);
        view.setOnClickListener(this);
        return new ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultHolder holder, int position) {
        holder.resutl_title.setText(results.get(position).getName());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ResultHolder extends RecyclerView.ViewHolder {
        private TextView resutl_title;

        public ResultHolder(View itemView) {
            super(itemView);
            resutl_title = (TextView) itemView.findViewById(R.id.search_result_title);
        }
    }
}
