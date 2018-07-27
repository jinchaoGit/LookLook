package com.jinchao.looklook.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.jinchao.looklook.Adapter.SearchResultAdapter;
import com.jinchao.looklook.Model.FilmSearchResult;
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
 * Created by ljc on 18-5-25.
 */

public class ActivitySearch extends Activity {
    private static final String TAG = "ActivitySearch";
    private EditText mSearchEditText;
    private Button mSearchButton;
    private RecyclerView mSearchResult;
    private LinearLayoutManager mLinearLayoutManager;
    private SearchResultAdapter mSearchResultAdapter;
    private List<FilmSearchResult> list_result;
    private String filmName;
    private ProgressBar mSearchProgressBar;
    private TextView mSearchResultTip;

    private NetworkUtils utils;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        mSearchButton = (Button) findViewById(R.id.activity_search_button);
        mSearchEditText = (EditText) findViewById(R.id.activity_search_edittext);
        mSearchResult = (RecyclerView) findViewById(R.id.activity_search_result);
        utils = new NetworkUtils();
        list_result = new ArrayList<>();

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchEditText.setText("");
                list_result.clear();
                mSearchResultAdapter.notifyDataSetChanged();
                mSearchResultTip.setVisibility(View.GONE);
                showInputMethod();
            }
        });

        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });

        mSearchResultAdapter = new SearchResultAdapter(getApplicationContext(), list_result);
        mSearchResultAdapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String url = list_result.get(position).getUrl();
                String name = list_result.get(position).getName();
                Intent intent = new Intent();
                intent.setClass(ActivitySearch.this, ActivityFilmContent.class);
                intent.putExtra("url", url);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLinearLayoutManager.setReverseLayout(false);
        mSearchResult.setLayoutManager(mLinearLayoutManager);
        mSearchResult.setAdapter(mSearchResultAdapter);

        mSearchProgressBar = (ProgressBar) findViewById(R.id.activity_search_progressbar);
        mSearchResultTip = (TextView) findViewById(R.id.activity_search_result_tip);
    }

    private void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }
    public void showInputMethod() {
        mSearchEditText.setFocusable(true);
        mSearchEditText.setFocusableInTouchMode(true);
        mSearchEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mSearchEditText, 0);
    }

    private void search() {
        filmName = mSearchEditText.getText().toString();
        list_result.clear();
        mSearchResultAdapter.notifyDataSetChanged();
        new SearchTask().execute();
        mSearchProgressBar.setVisibility(View.VISIBLE);
        mSearchResultTip.setVisibility(View.GONE);
        hideInputMethod();
    }

    private class SearchTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "start11 " + System.currentTimeMillis());

            if (!utils.isNetworkConnected(getApplicationContext())) {
                return null;
            }

            Log.d(TAG, "start " + System.currentTimeMillis());
            Document document = null;
            try {
                document = Jsoup.connect("http://www.ttmeiju.vip/index.php/search/index.html?keyword=" + filmName).get();
                Elements urls = document.select("div[class=contentbox]").get(0).select("a");
                Log.d(TAG, "rows size is " + urls.size());
                if (urls.size() > 0) {
                    for (Element element : urls) {
                        String name = element.text();
                        String url = "http://www.ttmeiju.vip/" + element.attr("href");
                        FilmSearchResult result = new FilmSearchResult();
                        result.setName(name);
                        result.setUrl(url);
                        list_result.add(result);
                        Log.d("asd", " " + name + " \n " + url);
                        if(list_result.size()>=20){
                            break;
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
            if (list_result.size() > 0) {
                mSearchResultTip.setText("为你找到如下结果：");
                mSearchResultTip.setVisibility(View.VISIBLE);
            } else {
                mSearchResultTip.setText("换个关键词试试吧");
                mSearchResultTip.setVisibility(View.VISIBLE);
            }
            mSearchResultAdapter.notifyDataSetChanged();
            mSearchProgressBar.setVisibility(View.GONE);
            Log.d(TAG, " execute");
        }
    }
}
