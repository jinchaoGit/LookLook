package com.jinchao.looklook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.jinchao.looklook.MainActivity;
import com.jinchao.looklook.R;


/**
 * Created by ljc on 18-6-1.
 */

public class ActivityWelcome extends Activity {
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        handler = new Handler(new Handler.Callback() {
////            @Override
////            public boolean handleMessage(Message msg) {
////                Intent intent = new Intent();
////                intent.setClass(ActivityWelcome.this, MainActivity.class);
////                startActivity(intent);
////                finish();
////                return true;
////            }
////        });
////        handler.sendEmptyMessageDelayed(1, 2000);
        Intent intent = new Intent();
        intent.setClass(ActivityWelcome.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
