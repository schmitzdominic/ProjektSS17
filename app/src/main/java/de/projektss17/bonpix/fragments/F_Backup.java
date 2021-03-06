package de.projektss17.bonpix.fragments;

import android.app.backup.BackupManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

import de.projektss17.bonpix.R;

public class F_Backup extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    final public String KEY_SETTINGS = "backup_incl_settings";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.box_backup_preferences);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        BackupManager backupManager = new BackupManager(getActivity());
        backupManager.dataChanged();

        if (key.equals(KEY_SETTINGS)) {
            Log.e("#Settings Checkbox", " ### " + sharedPreferences.getBoolean(KEY_SETTINGS, true));
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