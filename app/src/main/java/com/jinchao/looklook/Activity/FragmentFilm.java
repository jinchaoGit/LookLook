package com.jinchao.looklook.Activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.jinchao.looklook.Adapter.FragmentFilmListAdapter;
import com.jinchao.looklook.Model.FragmentFilmContentList;
import com.jinchao.looklook.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljc on 18-5-18.
 */

public class FragmentFilm extends Fragment {

    private static final String TAG = "FragmentFilm";

    private static FragmentFilm sInstance;
    private List<FragmentFilmContentList> film_list = new ArrayList<>();
    private FragmentFilmListAdapter fragmentFilmListAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    public synchronized static FragmentFilm getInstance() {
        if (sInstance == null) {
            sInstance = new FragmentFilm();
        }
        return sInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FilmLoaderTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film, container, false);
        recyclerView = view.findViewById(R.id.fragment_film_recyclerView);
        initView();
        return view;
    }

    private void initView() {
        fragmentFilmListAdapter = new FragmentFilmListAdapter(getContext(), film_list);
        fragmentFilmListAdapter.setOnItemClickLintener(new FragmentFilmListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String url = film_list.get(position).getFilmUrl();
                String name = film_list.get(position).getChName();
                Intent intent = new Intent();
                intent.setClass(getContext(), ActivityFilmContent.class);
                intent.putExtra("url", url);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(fragmentFilmListAdapter);
    }

    private class FilmLoaderTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            if (!isNetworkConnected(getContext())) {
                return null;
            }

            Log.d(TAG, "start " + System.currentTimeMillis());
            Document document = null;
            try {
                document = Jsoup.connect("http://www.ttmeiju.vip/").get();
                Elements imageUrl = document.select("div[class=secldiv]").get(0).select("img");
                Elements filmUrl = document.select("div[class=secldiv]").get(0).select("a");
                Elements filmName = document.select("div[class=secldiv]").get(0).select("span");
                Log.d(TAG, "list size is " + imageUrl.size());
                if (imageUrl.size() == 0) {

                } else {
                    for (Element element : filmUrl) {
                        if (element.attr("target").equals("_blank")) {
                            String imgage_url = element.select("img").get(0).attr("src");
                            String film_url = element.attr("href");
                            String cnName = element.select("span").get(0).text();
                            String enName = element.select("span").get(1).text();
                            FragmentFilmContentList fragmentFilmContentList = new FragmentFilmContentList();
                            fragmentFilmContentList.setEnName(enName);
                            fragmentFilmContentList.setChName(cnName);
                            fragmentFilmContentList.setFilmUrl(" http://www.ttmeiju.vip/" + film_url);
                            fragmentFilmContentList.setImageUrl(imgage_url);
                            film_list.add(fragmentFilmContentList);
                        }
                    }

//                    List<String> film_image_url = new ArrayList<>();
//                    List<String> film_url = new ArrayList<>();
//                    for (int i = 0; i < imageUrl.size(); i++) {
//                        String imgUrl = imageUrl.get(i).attr("src");
//                        if (!imageUrl.attr("alt").equals("换一批")) {
//                            film_image_url.add(imgUrl);
//                            Log.d("asdc", imgUrl);
//                        }
//                    }
//                    for (int i = 0; i < filmUrl.size(); i++) {
//                        String url = filmUrl.get(i).attr("href");
//                        if (!url.equals("javascript:void(0);")) {
//                            film_url.add(url);
//                            Log.d(TAG, url);
//                        }
//                    }

//                    for (int i = 0; i < film_image_url.size(); i++) {
//                        FragmentFilmContentList fragmentFilmContentList = new FragmentFilmContentList();
//                        fragmentFilmContentList.setImageUrl(film_image_url.get(i));
//                        fragmentFilmContentList.setFilmUrl(film_url.get(i));
//                        fragmentFilmContentList.setChName(filmName.get(i * 2).text());
//                        fragmentFilmContentList.setEnName(filmName.get(i * 2 + 1).text());
//                        film_list.add(fragmentFilmContentList);
//                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "end " + System.currentTimeMillis());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(TAG, " execute");
            fragmentFilmListAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), " size: " + film_list.size(), Toast.LENGTH_LONG).show();
        }

        public boolean isNetworkConnected(Context context) {
            if (context != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null) {
                    return true;
                }
            }
            return false;
        }
    }
}
