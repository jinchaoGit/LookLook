package com.jinchao.looklook.Utils;

import android.util.Log;


import com.jinchao.looklook.Model.NewsContent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ljc on 18-6-30.
 */

public class HtmlUtil {
    private static final String TAG = "HttpUtils";
    private static HtmlUtil downloadUtil;
    private final OkHttpClient okHttpClient;
    private List<NewsContent> newsList;

    public static HtmlUtil getInstance() {
        if (downloadUtil == null) {
            downloadUtil = new HtmlUtil();
        }
        return downloadUtil;
    }

    public HtmlUtil() {
        okHttpClient = new OkHttpClient();
        newsList = new LinkedList<>();
//        getHtml("");
    }

    public String getHtml(String url) {

        final String[] result = new String[1];
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    result[0] = response.body().string();

                    Pattern pattern = Pattern.compile("<div class=\"articlecontent\"([\\s\\S]*)<!--底部评论区-->");
                    Matcher matcher = pattern.matcher(result[0]);
                    if (matcher.find()) {
                        String content = matcher.group();
                        Log.d("asd", "find" + content);
                        String remove_section = content.replaceAll("<section>|</section>|<strong>|</strong>|<p>|&nbsp;|<br/>|<div class=\"articlecontent\" style=\"overflow: hidden;\">|<!--底部评论区-->|</div>", "");
                        Log.d("asd", "remove_section " + remove_section);

                        String[] split = remove_section.split("</p>");
                        for (String res : split) {
                            if (!res.equals("")) {
                                if (res.contains("<img")) {
                                    Document document = Jsoup.parse(res);
                                    Elements elements = document.select("img");
                                    for (Element element : elements) {
                                        NewsContent news = new NewsContent();
                                        news.setType(1);
                                        news.setValue(element.attr("src"));
                                        Log.d("asd1", element.attr("src"));
                                        newsList.add(news);
                                    }
                                } else {
                                    NewsContent news = new NewsContent();
                                    news.setType(0);
                                    news.setValue(res);
                                    newsList.add(news);
                                }
                                Log.d("ads", res);
                            }
                        }
                    }

                    for (NewsContent news : newsList) {
                        Log.d("result", news.getType() + " " + news.getValue());
                    }
                }
            }
        });

        return result[0];
    }
//
//    public void getUrl() {
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url(START).build();
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String str = response.body().string();
//                    Pattern r = Pattern.compile("<div id=\"left\">([\\s\\S]*)<div id=\"right\">");
//                    Matcher m = r.matcher(str);
//                    m.find();
//                    String remove = m.group();
//                    Pattern pattern = Pattern.compile("href=\"/.*.html");
//                    Matcher matcher = pattern.matcher(remove);
//
//                    while (matcher.find()) {
//                        String result = matcher.group().substring(6);
//                        String path = matcher.group().substring(6, matcher.group().length() - 5);
//
//                        Pattern split = Pattern.compile("/");
//                        String[] tmp = split.split(result, 3);
//
//                        Log.d("asss", tmp[0] + " " + tmp[1] + " " + tmp[2]);
//
//                    }
//                }
//            }
//        });
//
//    }
}