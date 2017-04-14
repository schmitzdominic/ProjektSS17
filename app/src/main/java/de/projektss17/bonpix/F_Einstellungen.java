package de.projektss17.bonpix;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

/**
 * Created by Marcus on 14.04.2017.
 */

public class F_Einstellungen extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    final public String KEY_NOTE = "pref_notifications";
    final public String KEY_MAIL = "pref_email";
    final public String KEY_NAME = "pref_name";
    final public String KEY_SOUND = "pref_sound";
    private Preference backup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.box_einstellungen_preferences);

        this.backup = (Preference) findPreference("pref_backup");

        this.backup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                S.showBackup(F_Einstellungen.this);
                return true;
            }
        });

    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(KEY_NOTE)) {
            Log.e("#Settings Checkbox", " ### " + sharedPreferences.getBoolean(KEY_NOTE, true));
        }
        else if (key.equals(KEY_SOUND)){
            Log.e("#Settings Checkbox 2", " ### " + sharedPreferences.getBoolean(KEY_SOUND, true));
        }
        else if (key.equals(KEY_MAIL)){
            Preference connectionPref = findPreference(key);
            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
        }
        else if (key.equals(KEY_NAME)){
            Preference connectionPref = findPreference(key);
            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}