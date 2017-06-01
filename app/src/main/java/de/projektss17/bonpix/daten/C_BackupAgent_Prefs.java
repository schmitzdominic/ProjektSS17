package de.projektss17.bonpix.daten;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.IOException;

import de.projektss17.bonpix.S;

/**
 * Created by Marcus on 25.05.2017.
 */

public class C_BackupAgent_Prefs extends BackupAgentHelper {

    static final String PREFS_BACKUP_KEY = "pref_notifications";
    static final String PREFS_BACKUP_KEY_FIRST_TIME = "first_time";

    @Override
    public void onCreate() {
        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, PREFS_BACKUP_KEY, PREFS_BACKUP_KEY_FIRST_TIME);
        addHelper("prefs", helper);
    }

    @Override
    public void onRestore(BackupDataInput data, int version, ParcelFileDescriptor newState){
        try {
            super.onRestore(data, version, newState);
            S.prefs.savePrefBoolean("first_time", false);
        } catch (IOException e){
            Log.e("IOException","onRestore in Agent_DB");
        }
    }
}