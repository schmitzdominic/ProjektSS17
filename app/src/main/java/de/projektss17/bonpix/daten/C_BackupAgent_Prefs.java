package de.projektss17.bonpix.daten;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;

import java.io.IOException;

/**
 * Created by Marcus on 25.05.2017.
 */

public class C_BackupAgent_Prefs extends BackupAgentHelper {

    static final String PREFS_BACKUP_KEY = "pref_notifications";

    @Override
    public void onCreate() {
        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, PREFS_BACKUP_KEY);
        addHelper("prefs", helper);
    }
}
