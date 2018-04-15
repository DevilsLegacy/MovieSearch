package com.devilslegacy.moviesearch.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SharedPreferenceHelper {

    private static SharedPreferenceHelper sInstance;

    public static SharedPreferenceHelper getInstance() {
        if (sInstance == null) {
            synchronized (SharedPreferences.class) {
                if (sInstance == null)
                    sInstance = new SharedPreferenceHelper();
            }
        }
        return sInstance;
    }

    private SharedPreferenceHelper() {

    }

    public void putPref(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PrefConstants.PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
    }

    private SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PrefConstants.PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public void putPrefInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(key, value).commit();
    }

    public void putPrefBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(key, value).commit();
    }

    public void putPrefFloat(Context context, String key, float value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putFloat(key, value).commit();
    }

    public void putPrefLong(Context context, String key, long value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putLong(key, value).commit();
    }

    public void putPrefString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(key, value).commit();
    }

    public void putPrefStringSet(Context context, String key, Set<String> value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putStringSet(key, value).commit();
    }

    public int getPrefInt(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public boolean getPrefBoolean(Context context, String key, boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public float getPrefFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    public long getPrefLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    public String getPrefString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public Set<String> getPrefStringSet(Context context, String key, Set<String> defValue) {
        return getPreferences(context).getStringSet(key, defValue);
    }

}
