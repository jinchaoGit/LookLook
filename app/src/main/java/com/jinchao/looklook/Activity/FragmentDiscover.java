package com.jinchao.looklook.Activity;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.VideoView;


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

public class FragmentDiscover extends Fragment {

    private static final String TAG = "FragmentDiscover";
    private EditText editText_FilmName;
    private Button button_search;
    private ListView listView_search_result;
    private String filmName;
    private VideoView videoView;
    private List<String> list = new ArrayList<>();
    private List<String> listUrls = new ArrayList<>();

    private static FragmentDiscover sInstance;

    public synchronized static FragmentDiscover getInstance() {
        if (sInstance == null) {
            sInstance = new FragmentDiscover();
        }
        return sInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
//        videoView = view.findViewById(R.id.video_new);
//        Uri uri = Uri.parse("https://dl.fantasy.tv/video/151175/151659/1527558515554441_new.mp4?k=67d4da324a81a9b4d2665ce6561b22a1&amp;t=1527745534");
//        videoView.setMediaController(new MediaController(getContext()));
//        videoView.setVideoURI(uri);
//        videoView.start();
        return view;
    }


//    private void stat() {
//        ComponentName componetName = new ComponentName(
//                //这个是另外一个应用程序的包名
//                "com.baidu.netdisk",
//                //这个参数是要启动的Activity
//                "com.baidu.netdisk.ui.UrlLinkActivity");
//
//        try {
//            Intent intent = new Intent();
//            intent.setComponent(componetName);
//            startActivity(intent);
//        } catch (Exception e) {
////              Toast.makeText(getApplicationContext(), "可以在这里提示用户没有找到应用程序，或者是做其他的操作！", 0).show();
//        }
//    }

    private class SearchTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            if (!isNetworkConnected(getContext())) {
                return null;
            }

            Log.d(TAG, "start " + System.currentTimeMillis());
            Document document = null;
            try {
                document = Jsoup.connect("http://www.ttmeiju.vip/index.php/search/index.html?keyword=" + filmName).get();
                Elements urls = document.select("div[class=contentbox]").get(0).select("a");
                Log.d(TAG, "rows size is " + urls.size());
                if (urls.size() == 1) {

                } else {
                    for (Element element : urls) {
                        list.add(element.text());
                        listUrls.add("http://www.ttmeiju.vip/" + element.attr("href"));
                        Log.d("asd", " " + element.text() + " \n " + element.attr("href"));
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

}
