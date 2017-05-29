package de.projektss17.bonpix.daten;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import de.projektss17.bonpix.S;

/**
 * Created by Marcus on 25.05.2017.
 */

public class C_BackupAgent_DB extends BackupAgentHelper {

    @Override
    public void onCreate() {
        addHelper("db", new FileBackupHelper(this, "bonpix"));
    }

    @Override
    public File getFilesDir(){
        File path = getDatabasePath("bonpix");
        return path.getParentFile();
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