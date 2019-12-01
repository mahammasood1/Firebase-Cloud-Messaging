package com.example.cloudmessaging;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "sharedPref";
    private static final String KEY_ACCESS_TOKEN = "FCM_token";

    private static Context mContext;
    private static SharedPrefManager manager;

    private SharedPrefManager(Context mContext){
        this.mContext = mContext;
    }

    public static synchronized SharedPrefManager getInstance(Context mContext){
        if(manager == null){
            manager = new SharedPrefManager(mContext);
        }
        return manager;
    }

    public boolean storeToken(String token){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN,token);
        editor.apply();
        return true;
    }
    public String getToken(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN,null);
    }
}
