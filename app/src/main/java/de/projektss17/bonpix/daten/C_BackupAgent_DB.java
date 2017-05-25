package de.projektss17.bonpix.daten;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.IOException;

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
}