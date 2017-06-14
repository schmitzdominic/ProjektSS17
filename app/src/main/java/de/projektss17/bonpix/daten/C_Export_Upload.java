package de.projektss17.bonpix.daten;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.projektss17.bonpix.A_Export;
import de.projektss17.bonpix.R;

import static android.content.ContentValues.TAG;
import static de.projektss17.bonpix.R.id.image;

/**
 * Created by domin on 14.06.2017.
 */

public class C_Export_Upload extends A_Export {



    public void test(String path){



        String[] separated = path.split("/");
        final String title = separated[separated.length - 1];





        final Bitmap image = BitmapFactory.decodeFile(path);

    Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(
    new ResultCallback<DriveApi.DriveContentsResult>() {



        @Override
        public void onResult(DriveApi.DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                Log.i(TAG, "Failed to create new contents.");
                return;
            }
            OutputStream outputStream = result.getDriveContents().getOutputStream();
            // Write the bitmap data from it.
            ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 80, bitmapStream);
            try {
                outputStream.write(bitmapStream.toByteArray());
            } catch (IOException e1) {
                Log.i(TAG, "Unable to write file contents.");
            }
            image.recycle();
            outputStream = null;
            MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                    .setMimeType("image/jpeg").setTitle(title)
                    .build();
            Log.i(TAG, "Creating new pic on Drive (" + title + ")");
            Drive.DriveApi.getFolder(mGoogleApiClient,
                    driveId).createFile(mGoogleApiClient,
                    metadataChangeSet, result.getDriveContents());
        }
    });
}

}
