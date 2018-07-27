package com.jinchao.looklook.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by ljc on 18-5-23.
 */

public class SharedPreferenceHelper {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferenceHelper(Context context, String name) {
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void put(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String get(String key, String  value) {
        return sharedPreferences.getString(key, "");
    }

    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    public Boolean contain(String key) {
        return sharedPreferences.contains(key);
    }

    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }
}