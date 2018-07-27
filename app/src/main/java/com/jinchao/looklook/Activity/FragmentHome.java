package com.jinchao.looklook.Activity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.jinchao.looklook.Adapter.NewsAdapter;
import com.jinchao.looklook.Model.News;
import com.jinchao.looklook.R;
import com.jinchao.looklook.Utils.GlideImageLoader;
import com.jinchao.looklook.Utils.SharedPreferenceHelper;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ljc on 18-5-18.
 */

public class FragmentHome extends Fragment {

    private static final String TAG = "FragmentHome";
    private Banner banner;
    private List<String> images = new ArrayList<>();
    private List<String> imagestmp = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<String> titlestmp = new ArrayList<>();
    private static FragmentHome sInstance;

    private List<News> newsList = new ArrayList<>();
    private RecyclerView newsRecycleView;
    private LinearLayoutManager linearLayoutManager;
    private NewsAdapter newsAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AppBarLayout mAppBarLayout;
    private Button mHomeSearchButton;

    private SharedPreferenceHelper sharedPreferenceHelper;

    public synchronized static FragmentHome getInstance() {
        if (sInstance == null) {
            sInstance = new FragmentHome();
        }
        return sInstance;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    titles.clear();
//                    images.clear();
//                    newsList.clear();
                    new ImagesLoaded().execute();
                    Toast.makeText(getContext(), "刷新了", Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceHelper = new SharedPreferenceHelper(getContext(), "fragmentHome");
        new ImagesLoaded().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView is run");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        newsRecycleView = (RecyclerView) view.findViewById(R.id.news_recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.home_swipeRefreshLayout);
        banner = (Banner) view.findViewById(R.id.home_banner);
        mAppBarLayout = view.findViewById(R.id.home_appbarlayout);
        mHomeSearchButton = (Button) view.findViewById(R.id.home_search_button);
        initView();
        return view;
    }

    private void initView() {

        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(1, 500);
            }
        });

        newsAdapter = new NewsAdapter(getContext(), newsList);
        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getContext(), "列表中的第" + position + "个", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(getContext(), ActivityNewsContent.class);
                intent.putExtra("url", newsList.get(position).getNews_urls());
                startActivity(intent);
            }
        });
        newsRecycleView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(false);
        newsRecycleView.setLayoutManager(linearLayoutManager);
        newsRecycleView.setAdapter(newsAdapter);

        banner.setImageLoader(new GlideImageLoader());
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Toast.makeText(getContext(), "点击了Banner的第" + position + "张图", Toast.LENGTH_SHORT).show();
            }
        });
        for (int i = 0; i < 3; i++) {
            String banner_image = sharedPreferenceHelper.get("banner_image" + i, "");
            imagestmp.add(banner_image);
            String banner_title = sharedPreferenceHelper.get("banner_title" + i, "");
            titlestmp.add(banner_title);
            Log.d(TAG, "initBanner image:" + banner_image + "\n title:" + banner_title);
        }
        banner.setImages(imagestmp);
        banner.setBannerTitles(titlestmp);
        banner.start();

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mSwipeRefreshLayout.setEnabled(verticalOffset == 0);
            }
        });

        mHomeSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ActivitySearch.class);
                startActivity(intent);
            }
        });
    }

    private class ImagesLoaded extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            if (!isNetworkConnected(getContext())) {
                for (int i = 0; i < 3; i++) {
                    String banner_image = sharedPreferenceHelper.get("banner_image" + i, "");
                    if (!banner_image.equals("")) {
                        images.add(banner_image);
                        String banner_title = sharedPreferenceHelper.get("banner_title" + i, "");
                        titles.add(banner_title);
                    }
                }
                return null;
            }

//            if (newsList.size() == 10 && images.size() == 3 && titles.size() == 3)
//                return null;
            Log.d(TAG, "start " + System.currentTimeMillis());
            Document document = null;
            try {
                document = Jsoup.connect("http://www.ttmeiju.vip/").get();
                Elements imgs = document.select("div[class=trailer]").get(0).select("img");
                Elements tits = document.select("div[class=trailer]").get(0).select("div");
                Elements list_titles = document.select("div[class=third]").get(0).select("div[class=yplitit]");
                Elements list_summary = document.select("div[class=third]").get(0).select("div[class=ypinfo]");
                Elements list_images = document.select("div[class=third]").get(0).select("img");
                Elements list_urls = document.select("div[class=third]").get(0).select("a");
                Log.d(TAG, "tits size is " + tits.size());
                Log.d(TAG, "rows size is " + imgs.size());
                Log.d(TAG, "list size is " + list_images.size());
                if (imgs.size() == 1 || tits.size() == 1) {

                } else {
                    for (int i = 0; i < imgs.size(); i++) {
                        String src = imgs.get(i).attr("src");
                        if (images.size() < imgs.size()) {
                            images.add("");
                            titles.add("");
                        }
                        if (!images.get(i).equals(src)) {
                            String text = tits.get(i + 1).text();
                            images.remove(i);
                            titles.remove(i);
                            images.add(i, src);
                            titles.add(i, text);
                            String banner_image = sharedPreferenceHelper.get("banner_image" + i, "");
                            if (banner_image.equals("") || banner_image.equals(src)) {
                                sharedPreferenceHelper.put("banner_image" + i, src);
                                sharedPreferenceHelper.put("banner_title" + i, text);
                            }
                            Log.d(TAG, "this " + src);
                            Log.d(TAG, "this " + text);
                        }
                    }
                    for (int i = 0; i < list_images.size(); i++) {
                        News news = new News();
                        if (newsList.size() < list_images.size()) {
                            newsList.add(news);
                            Log.d("asd", "list size:" + newsList.size());
                        }
                        String imageUrl = list_images.get(i).attr("src");
                        if (!imageUrl.equals(newsList.get(i).getNews_imageUrl())) {
                            String title = list_titles.get(i).text();
                            String summary = list_summary.get(i).text();
                            String url;
                            if (i > 4) {
                                url = "http://www.ttmeiju.vip" + list_urls.get(i + 2).attr("href");
                            } else {
                                url = "http://www.ttmeiju.vip" + list_urls.get(i + 1).attr("href");
                            }
                            Log.d("adds", i + " " + url);
                            news.setNews_imageUrl(imageUrl);
                            news.setNews_title(title);
                            news.setNews_summary(summary);
                            news.setNews_urls(url);
                            newsList.remove(i);
                            newsList.add(i, news);
                        }
                    }
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
            banner.update(images, titles);
            newsAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        }
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

    @Override
    public void onResume() {
        super.onResume();
        banner.startAutoPlay();
        Log.d(TAG, "onResume is run");
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopAutoPlay();
        Log.d(TAG, "onPause is run");
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
        Log.d(TAG, "onStop is run");
    }
}
