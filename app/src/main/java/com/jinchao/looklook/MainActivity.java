package com.jinchao.looklook;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jinchao.looklook.Activity.FragmentDiscover;
import com.jinchao.looklook.Activity.FragmentFilm;
import com.jinchao.looklook.Activity.FragmentHome;
import com.jinchao.looklook.Activity.FragmentMe;

public class MainActivity extends Activity implements View.OnClickListener {

    private long firstTime = 0;

    private FragmentManager fgManager;
    private FragmentTransaction fgTransaction;

    private FragmentHome fgHome;
    private FragmentDiscover fgDiscover;
    private FragmentFilm fgFilm;
    private FragmentMe fgMe;

    private LinearLayout mHome;
    private LinearLayout mFilm;
    private LinearLayout mDiscover;
    private LinearLayout mMe;

    private ImageView mImageHome, mImageFilm, mImageDiscover, mImageMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        Log.d("jinchao1", "init");

        mHome = (LinearLayout) findViewById(R.id.bottom_home);
        mFilm = (LinearLayout) findViewById(R.id.bottom_film);
        mDiscover = (LinearLayout) findViewById(R.id.bottom_discover);
        mMe = (LinearLayout) findViewById(R.id.bottom_me);

        mImageHome = (ImageView) findViewById(R.id.bottom_home_image);
        mImageFilm = (ImageView) findViewById(R.id.bottom_film_image);
        mImageDiscover = (ImageView) findViewById(R.id.bottom_discover_image);
        mImageMe = (ImageView) findViewById(R.id.bottom_me_image);

        mHome.setOnClickListener(this);
        mFilm.setOnClickListener(this);
        mDiscover.setOnClickListener(this);
        mMe.setOnClickListener(this);

        fgManager = getFragmentManager();
        mImageHome.setImageResource(R.mipmap.bottom_navigation_home_select);
        fgTransaction = fgManager.beginTransaction();
        fgHome = FragmentHome.getInstance();
        fgTransaction.add(R.id.content, fgHome);
        fgTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        fgTransaction = fgManager.beginTransaction();
        switch (v.getId()) {
            case R.id.bottom_home:
                setSelectStatusAndHideFragment(fgTransaction);

                mImageHome.setImageResource(R.mipmap.bottom_navigation_home_select);
                if (fgHome == null) {
                    fgHome = FragmentHome.getInstance();
                    fgTransaction.add(R.id.content, fgHome);
                } else {
                    fgTransaction.show(fgHome);
                }
                break;
            case R.id.bottom_film:
                setSelectStatusAndHideFragment(fgTransaction);

                mImageFilm.setImageResource(R.mipmap.bottom_navigation_film_select);
                if (fgFilm == null) {
                    fgFilm = FragmentFilm.getInstance();
                    fgTransaction.add(R.id.content, fgFilm);
                } else {
                    fgTransaction.show(fgFilm);
                }
                break;
            case R.id.bottom_discover:
                Log.d("asd", "botton_discover");
                setSelectStatusAndHideFragment(fgTransaction);

                mImageDiscover.setImageResource(R.mipmap.bottom_navigation_discover_select);
                if (fgDiscover == null) {
                    fgDiscover = FragmentDiscover.getInstance();
                    fgTransaction.add(R.id.content, fgDiscover);
                } else {
                    fgTransaction.show(fgDiscover);
                }
                break;
            case R.id.bottom_me:
                setSelectStatusAndHideFragment(fgTransaction);
                mImageMe.setImageResource(R.mipmap.bottom_navigation_me_select);
                if (fgMe == null) {
                    fgMe = FragmentMe.getInstance();
                    fgTransaction.add(R.id.content, fgMe);
                } else {
                    fgTransaction.show(fgMe);
                }
                break;
            default:
                break;
        }
        fgTransaction.commit();
    }

    public void setSelectStatusAndHideFragment(FragmentTransaction fgTransaction) {
        mImageHome.setImageResource(R.mipmap.bottom_navigation_home);
        mImageFilm.setImageResource(R.mipmap.bottom_navigation_film);
        mImageDiscover.setImageResource(R.mipmap.bottom_navigation_discover);
        mImageMe.setImageResource(R.mipmap.bottom_navigation_me);
        if (fgHome != null) {
            fgTransaction.hide(fgHome);
        }
        if (fgMe != null) {
            fgTransaction.hide(fgMe);
        }
        if (fgDiscover != null) {
            fgTransaction.hide(fgDiscover);
        }
        if (fgFilm != null) {
            fgTransaction.hide(fgFilm);
        }
    }

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime-firstTime>2000){
            Toast.makeText(this, "继续点击退出", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        }else {
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }}
