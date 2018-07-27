package com.jinchao.looklook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jinchao.looklook.Adapter.NewsContentAdapter;
import com.jinchao.looklook.Model.NewsContent;
import com.jinchao.looklook.R;

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
 * Created by ljc on 18-6-1.
 */

public class ActivityNewsContent extends Activity {
    private static final String TAG = "ActivityNewsContent";
//    private X5WebView webView;
//    private String url;

    private RecyclerView newsContentRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private NewsContentAdapter newsContentAdapter;
    private List<NewsContent> newsContentsList = new LinkedList<>();
    private String html;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        newsContentRecyclerView = findViewById(R.id.news_content_recyclerView);
        initView();
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        if (url != null) {
            Log.d("asda", url);
            getNewsContentsList(url);
//            loadUrl(url);
//            webView.loadUrl(url);
        }


    }

    private void initView() {
        newsContentAdapter = new NewsContentAdapter(this, newsContentsList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(false);
        newsContentRecyclerView.setHasFixedSize(true);
        newsContentRecyclerView.setLayoutManager(linearLayoutManager);
        newsContentRecyclerView.setAdapter(newsContentAdapter);
    }


    public void getNewsContentsList(String url) {
        final List<NewsContent> newsList = new LinkedList<>();
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
                    String result = response.body().string();

                    Pattern pattern = Pattern.compile("<div class=\"articlecontent\"([\\s\\S]*)<!--底部评论区-->");
                    Matcher matcher = pattern.matcher(result);
                    if (matcher.find()) {
                        String content = matcher.group();
                        Log.d("asd", "find" + content);
                        String remove_section = content.replaceAll("<section>|</section>|<strong>|</strong>|<p>|&nbsp;|<br/>|<div class=\"articlecontent\" style=\"overflow: hidden;\">|<!--底部评论区-->|</div>|<em>|</em>", "");
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
                                        newsContentsList.add(news);
                                    }
                                } else {
                                    NewsContent news = new NewsContent();
                                    news.setType(0);
                                    news.setValue(res.replaceAll(" ", ""));
                                    newsContentsList.add(news);
                                }
                                Log.d("ads", res);
                            }
                        }
                    }

                    for (NewsContent news : newsContentsList) {
                        Log.d("result", news.getType() + " " + news.getValue());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            newsContentAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

//    private boolean checkPermissions(String... permissions) {
//        boolean needRequest = false;
//        for (String permission : permissions) {
//
//            boolean hasPermission = EasyPermissions.hasPermissions(this, permission);
//            if (!hasPermission) {
//                needRequest = true;
//                break;
//            }
//        }
//        return needRequest;
//    }

//    public void loadUrl(String url){
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url(url).build();
//
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String str = response.body().string();
//                    Pattern pattern = Pattern.compile("<div class=\"articlecontent\"([\\s\\S]*)<!--底部评论区-->");
//                    Matcher matcher = pattern.matcher(str);
//                    boolean result = matcher.find();
//
//                    String res = matcher.group(0);
//                    int count = matcher.groupCount();
//                    Log.d("asdaa", result + "" + count + res);
//                    html = res;
//                    new ResolveUrlTask().execute();
//                }
//            }
//        });
//    }

//    private class ResolveUrlTask extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String root_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/KotlinDo/";
//            writeToFile("test.html", html,root_path );
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            String root_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/KotlinDo/";
//            Log.d("asdaa", root_path);
//            webView.loadUrl("file:///" + root_path+"test.html");
//        }
//    }

//    public static void writeToFile(String fileName, String content, String path) {
//        File dirFile = null;
//        try {
//            dirFile = new File(path);
//            if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
//                boolean creadok = dirFile.mkdirs();
//                if (creadok) {
//                    Log.i(TAG, " ok:创建文件夹成功！ ");
//                } else {
//                    Log.i(TAG, " err:创建文件夹失败！ ");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String fullPath = dirFile + "/" + fileName;
//        write(fullPath, content);
//    }
//
//    /**
//     * 写文件
//     *
//     * @param path
//     * @param content
//     */
//    public static boolean write(String path, String content) {
//        String s = new String();
//        String s1 = new String();
//        BufferedWriter output = null;
//        try {
//            File f = new File(path);
//            if (f.exists()) {
//                f.delete();
//                Log.i(TAG, "文件已删除...");
//                if (f.createNewFile()) {
//                    Log.i(TAG, "文件创建成功...");
//                } else {
//                    Log.i(TAG, "文件创建失败...");
//                }
//
//            } else {
//                Log.i(TAG, "文件不存在，正在创建...");
//                if (f.createNewFile()) {
//                    Log.i(TAG, "文件创建成功...");
//                } else {
//                    Log.i(TAG, "文件创建失败...");
//                }
//            }
//            BufferedReader input = new BufferedReader(new FileReader(f));
//            while ((s = input.readLine()) != null) {
//                s1 += s + "\n";
//            }
//            input.close();
//            s1 += content;
//            output = new BufferedWriter(new FileWriter(f));
//            output.write(s1);
//            output.flush();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            if (output != null) {
//                try {
//                    output.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
