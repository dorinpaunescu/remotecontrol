package com.example.dorinpaunescu.remotecontrol.properties;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dorinpaunescu.remotecontrol.adapters.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dorin.paunescu on 10/26/2016.
 */
public class PropConfigHolder {

    private static class LazyHolder {
        private static final PropConfigHolder INSTANCE = new PropConfigHolder();
    }

    private Context context;
    private static final String PREFS_NAME = "RemoteControl";
    private Map<String, String> properties = new HashMap<>();

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public static PropConfigHolder getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static PropConfigHolder instance;

    private PropConfigHolder(){

    }

    public PropConfigHolder setContext(Context context){
        this.context = context;
        return this;
    }

    public PropConfigHolder init(){
        SharedPreferences settings = this.context.getSharedPreferences(PREFS_NAME, 0);

        Map<String, ?> all = settings.getAll();
        for(String key : all.keySet()) {
            System.out.println("[" + key + ":" + all.get(key) + "]");
        }

        String URL = settings.getString("URL", Constants.EMPTY_STRING);
        if(URL == null) {
            properties.put("URL", Constants.EMPTY_STRING);
        } else {
            properties.put("URL", URL);
        }

        String USERNAME = settings.getString("USERNAME", Constants.EMPTY_STRING);
        if(USERNAME == null) {
            properties.put("USERNAME", Constants.EMPTY_STRING);
        } else {
            properties.put("USERNAME", USERNAME);
        }

        String PASSWORD = settings.getString("PASSWORD", Constants.EMPTY_STRING);
        if(PASSWORD == null) {
            properties.put("PASSWORD", Constants.EMPTY_STRING);
        } else {
            properties.put("PASSWORD", PASSWORD);
        }

        return this;
    }

    public PropConfigHolder save() {

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        for(String key : properties.keySet()) {
            String value = properties.get(key);
            editor.putString(key, value);
        }

        // Commit the edits!
        editor.commit();

        return this;
    }

}
