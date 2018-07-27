package com.jinchao.looklook.Utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ljc on 18-6-8.
 */

public class SharePictureToWeChat {
    public static final String AUTHORITY = "com.jinchao.homeofzhihu";
    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/jisumeiju";
    public static String testPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "DCIM" + File.separator + "Camera";

    public static void sharePhotoToWX(Bitmap bitmap, Context context, String text) {
        if (!isWeChatExist(context)) {
            Log.d("asd", "wechat is not exist");
            return;
        }


        File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "DCIM" + File.separator + "Camera");
        String paths = "";
        File[] files = myFile.listFiles();
        if (files != null){
            for (File file:files) {
                if (file.getName().equals("IMG20180425155912.jpg")){
                    paths = file.getAbsolutePath();
                    Log.d("asd", "paths ok");
                    break;
                }

            }
        }


        savePicture(context, bitmap);

        File pic = new File(paths + "IMG20180425155912.jpg");
//        File pic = new File(paths);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, AUTHORITY, pic);
        } else {
            uri = Uri.fromFile(pic);
        }

        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(componentName);
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("Kdescription", "heheihiehi");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(intent);
    }

    /*****/

    public static boolean isWeChatExist(Context context) {
        String name = "com.tencent.mm";
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(name, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            if (packageInfo != null) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void savePicture(Context context, Bitmap bitmap) {
        File file = new File(testPath);
        if (!file.exists()) {
            file.mkdir();
        }
        String fileName = "IMG20180425155912.jpg";
        File picture = new File(testPath, fileName);
        if (!picture.exists()) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(picture);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                picture.canRead();
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(), picture.getAbsolutePath(), fileName, null);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(testPath+fileName)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}

