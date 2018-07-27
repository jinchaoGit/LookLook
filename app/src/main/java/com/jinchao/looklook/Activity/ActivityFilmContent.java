package com.jinchao.looklook.Activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jinchao.looklook.Adapter.ActivityFilmContentListAdapter;
import com.jinchao.looklook.Model.FilmContentResult;
import com.jinchao.looklook.Model.FilmSeasonList;
import com.jinchao.looklook.R;
import com.jinchao.looklook.Utils.NetworkUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljc on 18-5-24.
 */

public class ActivityFilmContent extends Activity {
    private static final String TAG = "ActivityFilmContent";
    private String url;
    private String name;

    private List<FilmContentResult> results = new ArrayList<>();

    private NetworkUtils utils;

    private TextView mTitleContent;
    private ImageView mTitleImage;
    private TextView mTitleUpdateTime;
    private TextView mTitleStatus;
    private TextView mTitleClass;
    private TextView mTitleLastUpdateTime;
    private TextView mTitleCountDown;
    private TextView mTitleBackTime;
    private TextView mFilmSummary;

    private Spinner mFilmSpinner;
    private List<FilmSeasonList> filmSeasonLists = new ArrayList<>();
    private List<String> seasonList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private AppBarLayout mAppbarLayout;
    private RecyclerView mFilmRecycleView;
    private LinearLayoutManager linearLayoutManager;
    private ActivityFilmContentListAdapter activityFilmContentListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filmcontent);

        Intent intent = getIntent();
        url = (String) intent.getExtras().get("url");
        name = (String) intent.getExtras().get("name");

        utils = new NetworkUtils();
        initView();
        new FilmContentTask().execute();
    }

    private void initView() {
        mTitleContent = findViewById(R.id.title_content);
        mTitleImage = (ImageView) findViewById(R.id.title_image);
        mTitleUpdateTime = (TextView) findViewById(R.id.film_update_time);
        mTitleStatus = (TextView) findViewById(R.id.film_status);
        mTitleClass = (TextView) findViewById(R.id.film_class);
        mTitleLastUpdateTime = (TextView) findViewById(R.id.film_last_update_time);
        mTitleCountDown = (TextView) findViewById(R.id.film_countdown);
        mTitleBackTime = (TextView) findViewById(R.id.film_back_time);
        mFilmSummary = (TextView) findViewById(R.id.film_summary);
        mProgressBar = findViewById(R.id.film_content_progressbar);
        mAppbarLayout = findViewById(R.id.film_content_appbarlayout);
        mFilmRecycleView = findViewById(R.id.content_film_recyclerview);
        mFilmSpinner = findViewById(R.id.film_spinner);
        mProgressBar.setVisibility(View.VISIBLE);
        mAppbarLayout.setVisibility(View.GONE);
        mFilmRecycleView.setVisibility(View.GONE);

        mFilmSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFilmSummary.getEllipsize() == TextUtils.TruncateAt.END) {
                    mFilmSummary.setMaxLines(100);
                    mFilmSummary.setEllipsize(null);
                } else {
                    mFilmSummary.setMaxLines(5);
                    mFilmSummary.setEllipsize(TextUtils.TruncateAt.END);
                }
            }
        });
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(false);
        activityFilmContentListAdapter = new ActivityFilmContentListAdapter(this, results);
        activityFilmContentListAdapter.setOnButtonClickListener(new ActivityFilmContentListAdapter.OnButtonClickListener() {
            @Override
            public void onClickListener(View view, int position) {
                switch (view.getId()) {
                    case R.id.content_item_copy_button:
                        try {
                            String link = results.get(position).getMagnet();
                            Intent magnet = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                            magnet.addCategory("android.intent.category.DEFAULT");
                            startActivity(magnet);
                            Toast.makeText(getApplicationContext(), "准备下载", Toast.LENGTH_SHORT).show();
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "请先安装迅雷等下载软件", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.content_item_view_button:
                        Intent online = new Intent(getApplicationContext(), ActivityForVideo.class);
                        online.putExtra("url", results.get(position).getOnline());
                        startActivity(online);
                        Toast.makeText(getApplicationContext(), "准备播放", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
        mFilmRecycleView.setLayoutManager(linearLayoutManager);
        mFilmRecycleView.setAdapter(activityFilmContentListAdapter);
        mTitleContent.setText(name);

        mFilmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                url = filmSeasonLists.get(position).getUri();
                new FilmContentTask().execute();
                Toast.makeText(ActivityFilmContent.this, seasonList.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private class FilmContentTask extends AsyncTask<String, Integer, String> {

        String title_imgUrl;
        String update_time;
        String status;
        String filmClass;
        String last_update_time;
        String backTime;
        String countdown;

        String film_summary;

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "start11 " + System.currentTimeMillis());

            if (!utils.isNetworkConnected(getApplicationContext())) {
                return null;
            }

            Log.d(TAG, "start " + System.currentTimeMillis());
            Document document = null;
            try {
                document = Jsoup.connect(url).get();
                Elements imgUrl = document.select("div[class=seedpic]").get(0).select("img");
                Elements texts = document.select("div[class=seedlink]").get(0).select("span");
                Elements summary = document.select("div[class=newstxt]").get(0).select("p");
                Elements search_list = document.select("div[class=seedlistdiv]").get(0).select("tr");
                Elements film_season_uri = document.select("div[class=seasondiv]").get(0).select("a");
                Elements film_season_name = document.select("div[class=seasondiv]").get(0).select("h3");
                Log.d(TAG, "rows size is " + imgUrl.size() + "  " + texts.size());
                if (imgUrl.size() == 0 || texts.size() < 6) {

                } else {

                    results.clear();
                    for (int i = 2; i < search_list.size() - 2; i++) {
                        FilmContentResult filmContentResult = new FilmContentResult();
                        Elements elements = search_list.get(i).select("a");
                        if (elements.size() > 0) {
                            int size = elements.size();
                            String name = elements.get(0).text();
                            String megnet = "";
                            String online = "";
                            Log.d("asd", "size is " + size);
                            Log.d("asd", "name is " + name);
                            for (Element element : elements) {
                                if (element.attr("title").equals("美剧在线播放")) {
                                    online = "http://www.ttmeiju.vip" + element.attr("href");
                                    Log.d("asd", "online is " + online);
                                    break;
                                }
                            }
                            for (Element element : elements) {
                                if (element.attr("title").equals("迅雷高清美剧下载")) {
                                    megnet = element.attr("href");
                                    Log.d("asd", "href is " + megnet);
                                    break;
                                }
                            }
                            filmContentResult.setName(name);
                            filmContentResult.setOnline(online);
                            filmContentResult.setMagnet(megnet);
                            results.add(filmContentResult);
                        }
                    }
                    if (seasonList.size() == 0) {
                        for (int i = 0; i < film_season_name.size(); i++) {
                            String name = film_season_name.get(i).text();
                            String uri = "http://www.ttmeiju.vip/" + film_season_uri.get(i).attr("href");

                            Log.d("asd12", name + " " + uri);
                            FilmSeasonList filmSeasonList = new FilmSeasonList();
                            filmSeasonList.setName(name);
                            filmSeasonList.setUri(uri);
                            seasonList.add(name);
                            filmSeasonLists.add(filmSeasonList);
                        }
                    }

                    title_imgUrl = imgUrl.get(0).attr("src");
                    update_time = texts.get(0).text();
                    status = texts.get(1).text();
                    filmClass = texts.get(2).text();
                    last_update_time = texts.get(3).text();
                    backTime = texts.get(4).text();
                    countdown = texts.get(5).text();

                    film_summary = summary.get(0).text();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "end " + System.currentTimeMillis());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Glide.with(ActivityFilmContent.this).load(title_imgUrl).into(mTitleImage);
            mTitleUpdateTime.setText(update_time);
            mTitleStatus.setText(status);
            mTitleClass.setText(filmClass);
            mTitleLastUpdateTime.setText(last_update_time);
            mTitleCountDown.setText(countdown);
            mTitleBackTime.setText(backTime);
            mFilmSummary.setText(film_summary);
            mProgressBar.setVisibility(View.GONE);
            activityFilmContentListAdapter.notifyDataSetChanged();
            mAppbarLayout.setVisibility(View.VISIBLE);
            mFilmRecycleView.setVisibility(View.VISIBLE);
            ArrayAdapter<String> seasonAdapter = new ArrayAdapter<String>(ActivityFilmContent.this, android.R.layout.simple_spinner_dropdown_item, seasonList);
            seasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            if (mFilmSpinner.getAdapter()==null){
                mFilmSpinner.setAdapter(seasonAdapter);
            }
            Log.d(TAG, " execute");
        }
    }
}
