package de.projektss17.bonpix.fragments;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import de.projektss17.bonpix.R;
import de.projektss17.bonpix.A_Backup;

import de.projektss17.bonpix.daten.C_Drive_API;
import de.projektss17.bonpix.daten.C_Drive_UT;

public class F_Backup extends PreferenceFragment{

    private static final int REQ_ACCPICK = 1;
    private static final int REQ_CONNECT = 2;
    private static final int REQ_CREATE = 3;
    private static final int REQ_PICKFILE = 4;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.box_backup_preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}