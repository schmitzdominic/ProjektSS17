package de.projektss17.bonpix.fragments;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import de.projektss17.bonpix.A_Main;
import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;

/**
 * Created by Marcus on 14.04.2017.
 */

public class F_Einstellungen extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    final public String KEY_NOTE = "pref_notifications";
    final public String KEY_MAIL = "pref_email";
    final public String KEY_NAME = "pref_name";
    final public String KEY_SOUND = "pref_sound";
    private Preference backup;
    private Preference tutorial;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.box_einstellungen_preferences);
        this.context = getActivity();

        this.backup = (Preference) findPreference("pref_backup");
        this.tutorial = (Preference) findPreference("category_tutorial");

        this.backup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                S.showBackup(F_Einstellungen.this);
                return true;
            }
        });

        this.tutorial.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                S.showTutorial(context);
                return true;
            }
        });



    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        BackupManager backupManager = new BackupManager(getActivity());
        backupManager.dataChanged();

        if (key.equals(KEY_NOTE)) {
            Log.e("#Settings Checkbox", " ### " + sharedPreferences.getBoolean(KEY_NOTE, true));
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