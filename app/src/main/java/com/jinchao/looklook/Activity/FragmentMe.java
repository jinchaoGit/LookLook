package com.jinchao.looklook.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jinchao.looklook.R;


/**
 * Created by ljc on 18-5-18.
 */

public class FragmentMe extends Fragment implements View.OnClickListener{
//    private Button button;
//    private Button new_button;

    private LinearLayout history;
    private LinearLayout money;
    private LinearLayout sub;
    private LinearLayout share;
    private LinearLayout contact;
    private LinearLayout about;
    private static FragmentMe sInstance;

    public synchronized static FragmentMe getInstance() {
        if (sInstance == null) {
            sInstance = new FragmentMe();
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
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        history = view.findViewById(R.id.me_history);
        money = view.findViewById(R.id.me_money);
        sub = view.findViewById(R.id.me_sub);
        share = view.findViewById(R.id.me_share);
        contact = view.findViewById(R.id.me_contact);
        about = view.findViewById(R.id.me_about);
        about.setOnClickListener(this);
        contact.setOnClickListener(this);
        share.setOnClickListener(this);
        sub.setOnClickListener(this);
        money.setOnClickListener(this);
        history.setOnClickListener(this);
//        button = (Button) view.findViewById(R.id.me_button);
//        new_button = (Button) view.findViewById(R.id.video_new);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), ActivityForVideo.class);
//                startActivity(intent);
//            }
//        });
//
//        new_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(getContext(), VideoActivity.class);
////                openVideo(getContext(), "https://www.fantasy.tv/videoAd/videoAd.html?id=1929504&channelId=151659&code=923fdb7872fe8e113d801a534fdfa54e");
////                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.service_start);
////                Log.d("asd", "mkdi2r");
////
////                SharePictureToWeChat.sharePhotoToWX(bitmap, getContext(), "jisumeiju");
//            }
//        });
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_about:
                Intent intent = new Intent(getContext(), ActivityAbout.class);
                startActivity(intent);
                break;
            case R.id.me_contact:
                Toast.makeText(getContext(), "请发邮件至liujinchao21@qq.com，谢谢！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.me_share:
                Toast.makeText(getContext(), "使用分享功能", Toast.LENGTH_SHORT).show();
                break;
            case R.id.me_sub:
                Toast.makeText(getContext(), "使用订阅功能", Toast.LENGTH_SHORT).show();
                break;
            case R.id.me_money:
                Toast.makeText(getContext(), "使用打赏功能", Toast.LENGTH_SHORT).show();
                break;
            case R.id.me_history:
                Toast.makeText(getContext(), "使用历史功能", Toast.LENGTH_SHORT).show();
                break;
                default:break;
        }
    }
}
