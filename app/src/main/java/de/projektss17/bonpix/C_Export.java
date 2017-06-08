package de.projektss17.bonpix;

import android.os.Bundle;


import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;

import java.lang.Override;
/**
 * Created by domin on 08.06.2017.
 */


public class C_Export extends A_Export {

        private DriveId mFolderDriveId;

        @Override
        public void onConnected(Bundle connectionHint) {
            super.onConnected(connectionHint);
            Drive.DriveApi.fetchDriveId(getGoogleApiClient(), EXISTING_FOLDER_ID)
                    .setResultCallback(idCallback);
        }

        final private ResultCallback<DriveApi.DriveIdResult> idCallback = new ResultCallback<DriveApi.DriveIdResult>() {
            @Override
            public void onResult(DriveApi.DriveIdResult result) {
                if (!result.getStatus().isSuccess()) {
                    showMessage("Cannot find DriveId. Are you authorized to view this file?");
                    return;
                }
                mFolderDriveId = result.getDriveId();
                Drive.DriveApi.newDriveContents(getGoogleApiClient())
                        .setResultCallback(driveContentsCallback);
            }
        };

        final private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
                new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(DriveApi.DriveContentsResult result) {
                        if (!result.getStatus().isSuccess()) {
                            showMessage("Error while trying to create new file contents");
                            return;
                        }
                        DriveFolder folder = mFolderDriveId.asDriveFolder();
                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle("New file")
                                .setMimeType("text/plain")
                                .setStarred(true).build();
                        folder.createFile(getGoogleApiClient(), changeSet, result.getDriveContents())
                                .setResultCallback(fileCallback);
                    }
                };

        final private ResultCallback<DriveFolder.DriveFileResult> fileCallback =
                new ResultCallback<DriveFolder.DriveFileResult>() {
                    @Override
                    public void onResult(DriveFolder.DriveFileResult result) {
                        if (!result.getStatus().isSuccess()) {
                            showMessage("Error while trying to create the file");
                            return;
                        }
                        showMessage("Created a file: " + result.getDriveFile().getDriveId());
                    }
                };


    }
