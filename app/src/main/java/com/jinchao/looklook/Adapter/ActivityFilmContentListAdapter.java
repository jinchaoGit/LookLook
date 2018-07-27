package com.jinchao.looklook.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jinchao.looklook.Model.FilmContentResult;
import com.jinchao.looklook.R;

import java.util.List;

/**
 * Created by ljc on 18-5-31.
 */

public class ActivityFilmContentListAdapter extends RecyclerView.Adapter<ActivityFilmContentListAdapter.ResultHolder> {

    private Context context;
    private List<FilmContentResult> results;
    private OnButtonClickListener onButtonClickListener;

    public ActivityFilmContentListAdapter(Context context, List<FilmContentResult> results) {
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_content_result_info, parent, false);

        return new ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ResultHolder holder, final int position) {
        if (!results.get(position).getOnline().equals("")) {
            holder.item_view.setVisibility(View.VISIBLE);
        } else {
            holder.item_view.setVisibility(View.GONE);
        }
        if (onButtonClickListener!=null){
            holder.item_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClickListener.onClickListener(holder.item_copy,position);
                }
            });

            holder.item_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClickListener.onClickListener(holder.item_view, position);
                }
            });
        }
        holder.item_title.setText(results.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    public interface OnButtonClickListener {
        void onClickListener(View view, int position);
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    class ResultHolder extends RecyclerView.ViewHolder {

        private TextView item_title;
        private Button item_copy;
        private Button item_view;

        public ResultHolder(View itemView) {
            super(itemView);
            item_title = (TextView) itemView.findViewById(R.id.content_item_film_title);
            item_copy = itemView.findViewById(R.id.content_item_copy_button);
            item_view = itemView.findViewById(R.id.content_item_view_button);
        }
    }
}
