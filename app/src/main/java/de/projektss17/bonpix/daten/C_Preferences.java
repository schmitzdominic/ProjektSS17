package de.projektss17.bonpix.daten;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Marcus on 14.04.2017.
 */

public class C_Preferences {
    public static final String APP_SHARED_PREFS = C_Preferences.class.getSimpleName();
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    public C_Preferences(Context context) {
        this.sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = sharedPrefs.edit();
    }

    // Restore
    public String getPrefString(String key) {
        return sharedPrefs.getString(key, "");
    }

    public Boolean getPrefBoolean(String key){
        return sharedPrefs.getBoolean(key, false);
    }

    public int getPrefInteger(String key){
        return sharedPrefs.getInt(key, 0);
    }

    public float getPrefFloat(String key){
        return sharedPrefs.getFloat(key, 0);
    }

    public long getPrefLong(String key){
        return sharedPrefs.getLong(key, 0);
    }

    // Store
    public void savePrefString(String key, String value) {
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public void savePrefBoolean(String key, boolean value){
        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }

    public void savePrefInteger(String key, int value){
        prefsEditor.putInt(key, value);
        prefsEditor.apply();
    }

    public void savePrefFloat(String key, float value){
        prefsEditor.putFloat(key, value);
        prefsEditor.apply();
    }

    public void savePrefLong(String key, long value){
        prefsEditor.putLong(key, value);
        prefsEditor.apply();
    }
}