package com.vthree.rentbaseapplication.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "meal";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setLogin(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public boolean isLogin() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, false);
    }

    public String setString(String key, String value) {

        editor.putString(key, value);
        editor.apply();
        return key;
    }

    public String getString(String key) {
        return pref.getString(key, "");
    }

    public void setInt(String key, int value)

    {
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key)

    {
        return pref.getInt(key, 0);
    }
}