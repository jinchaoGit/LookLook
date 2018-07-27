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
import com.jinchao.looklook.Model.FragmentFilmContentList;
import com.jinchao.looklook.R;

import java.util.List;

/**
 * Created by ljc on 18-6-4.
 */

public class FragmentFilmListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    //    private static final int TYPE_TITLE = 0;
//    private static final int TYPE_CONTENT = 1;
    private List<FragmentFilmContentList> film_list;
    //    private List<String> film_title;
    private Context context;

    private OnItemClickListener onItemClickListener;

    public FragmentFilmListAdapter(Context context, List<FragmentFilmContentList> film_list) {
        this.context = context;
        this.film_list = film_list;
//        this.film_title = film_title;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
//        if (viewType == 0) {
//            view = LayoutInflater.from(context).inflate(R.layout.item_fragment_film_content_title, parent, false);
//            return new FilmTitleHolder(view);
//        } else {
        view = LayoutInflater.from(context).inflate(R.layout.item_fragment_film_content_image, parent, false);
        view.setOnClickListener(this);
        return new FilmImageHolder(view);
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        if (film_list.get(position).getType() == 0 && holder instanceof FilmTitleHolder) {
//            ((FilmTitleHolder) holder).title.setText(film_list.get(position).);
//        } else {
//
//        }

        if (holder instanceof FilmImageHolder) {
            Glide.with(context).load(film_list.get(position).getImageUrl()).into(((FilmImageHolder) holder).filmPic);
            ((FilmImageHolder) holder).filmChName.setText(film_list.get(position).getChName());
            ((FilmImageHolder) holder).film_serial.setText((position + 1) + " :");
            ((FilmImageHolder) holder).filmEnName.setText(film_list.get(position).getEnName());
            ((FilmImageHolder) holder).itemView.setTag(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = film_list.get(position).getType();
        return type;
    }

    @Override
    public int getItemCount() {
        return film_list.size();
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

    public void setOnItemClickLintener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class FilmImageHolder extends RecyclerView.ViewHolder {
        private TextView filmChName;
        private TextView filmEnName;
        private ImageView filmPic;
        private TextView film_serial;

        public FilmImageHolder(View itemView) {
            super(itemView);
            filmChName = itemView.findViewById(R.id.fragment_film_content_chName);
            filmEnName = itemView.findViewById(R.id.fragment_film_content_enName);
            filmPic = itemView.findViewById(R.id.fragment_film_content_image);
            film_serial = itemView.findViewById(R.id.fragment_film_serial);
        }
    }

//    class FilmTitleHolder extends RecyclerView.ViewHolder {
//        private TextView title;
//
//        public FilmTitleHolder(View itemView) {
//            super(itemView);
//            title = itemView.findViewById(R.id.fragment_film_content_title);
//        }
//    }

}
