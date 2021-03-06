package de.projektss17.bonpix.fragments;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;

public class F_Einstellungen extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

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

    /**
     * Listener for SharedPreferences - Triggered if Preference has changed
     * @param sharedPreferences
     * @param key
     */
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        BackupManager backupManager = new BackupManager(getActivity());
        backupManager.dataChanged();
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