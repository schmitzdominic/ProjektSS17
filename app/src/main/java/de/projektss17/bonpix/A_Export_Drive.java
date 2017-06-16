package de.projektss17.bonpix;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import de.projektss17.bonpix.S;



public class A_Export_Drive extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private String email;
    private Button button_create_file;
    private Button button_upload_to_google_drive;
    private TextView selectAccount;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "<< DRIVE >>";
    protected static final int REQUEST_CODE_RESOLUTION = 1337;
    private String FOLDER_NAME = "BonPix";
    public DriveId driveId;
    public ArrayList pathlist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_export_drive_content);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        button_create_file = (Button) findViewById(R.id.button_import_to_google_drive);
        button_upload_to_google_drive = (Button) findViewById(R.id.button_export_to_google_drive);
        selectAccount = (TextView) findViewById(R.id.google_konto_verwaltung);

        //---- use this to get the fucking SHA1 for the fucking google project....
        //Log.v(">SHA > ", Utils.getCertificateSHA1Fingerprint(A_Export_Images.this));
        button_create_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WRITE A SIMPLE TEXT FILE IN SDCARD. BE CAREFUL TO GRANT PERMISSION IN ANDROID 6+
                writeToFile("tehfile", " fuck google...");
            }
        });
        selectAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               changeAccount();
            }
        });
        button_upload_to_google_drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoogleApiClient != null) {
                    upload_to_drive();
                } else {
                    Log.e(TAG, "Could not fucking connect to google drive manager");
                }
            }
        });
    }

    public void getAllBons(){
        pathlist = S.dbHandler.getAllBonsPath(S.db);
    }


    public void imageUpload(String path){
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




    private void upload_to_drive() {
        //async check if folder exists... if not, create it. continue after with create_file_in_folder(driveId);
        check_folder_exists();
    }

    private void check_folder_exists() {
        Query query =
                new Query.Builder().addFilter(Filters.and(Filters.eq(SearchableField.TITLE, FOLDER_NAME), Filters.eq(SearchableField.TRASHED, false)))
                        .build();
        Drive.DriveApi.query(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(DriveApi.MetadataBufferResult result) {
                if (!result.getStatus().isSuccess()) {
                    Log.e(TAG, "Cannot create folder in the root.");
                } else {
                    boolean isFound = false;
                    for (Metadata m : result.getMetadataBuffer()) {
                        if (m.getTitle().equals(FOLDER_NAME)) {
                            Log.e(TAG, "Folder exists");
                            isFound = true;
                            driveId = m.getDriveId();
                            create_file_in_folder(driveId);
                            break;
                        }
                    }
                    if (!isFound) {
                        Log.i(TAG, "Folder not found; creating it.");
                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(FOLDER_NAME).build();
                        Drive.DriveApi.getRootFolder(mGoogleApiClient)
                                .createFolder(mGoogleApiClient, changeSet)
                                .setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
                                    @Override
                                    public void onResult(DriveFolder.DriveFolderResult result) {
                                        if (!result.getStatus().isSuccess()) {
                                            Log.e(TAG, "U AR A MORON! Error while trying to create the folder");
                                        } else {
                                            Log.i(TAG, "Created a folder");
                                            DriveId driveId = result.getDriveFolder().getDriveId();
                                            create_file_in_folder(driveId);
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    private void create_file_in_folder(final DriveId driveId) {
        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                if (!driveContentsResult.getStatus().isSuccess()) {
                    Log.e(TAG, "U AR A MORON! Error while trying to create new file contents");
                    return;
                }
                OutputStream outputStream = driveContentsResult.getDriveContents().getOutputStream();
                //------ THIS IS AN EXAMPLE FOR PICTURE ------
                //ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
                //image.compress(Bitmap.CompressFormat.PNG, 100, bitmapStream);
                //try {
                //  outputStream.write(bitmapStream.toByteArray());
                //} catch (IOException e1) {
                //  Log.i(TAG, "Unable to write file contents.");
                //}
                //// Create the initial metadata - MIME type and title.
                //// Note that the user will be able to change the title later.
                //MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                //    .setMimeType("image/jpeg").setTitle("Android Photo.png").build();
                //------ THIS IS AN EXAMPLE FOR FILE --------
                Toast.makeText(A_Export_Drive.this, "Uploading to drive. If you didn't fucked up something like usual you should see it there", Toast.LENGTH_LONG).show();
                final File theFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/xtests/tehfile.txt"); //>>>>>> WHAT FILE ?
                try {
                    FileInputStream fileInputStream = new FileInputStream(theFile);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e1) {
                    Log.i(TAG, "U AR A MORON! Unable to write file contents.");
                }
                MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(theFile.getName()).setMimeType("text/plain").setStarred(false).build();
                DriveFolder folder = driveId.asDriveFolder();
                folder.createFile(mGoogleApiClient, changeSet, driveContentsResult.getDriveContents())
                        .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
                            @Override
                            public void onResult(@NonNull DriveFolder.DriveFileResult driveFileResult) {
                                if (!driveFileResult.getStatus().isSuccess()) {
                                    Log.e(TAG, "U AR A MORON!  Error while trying to create the file");
                                    return;
                                }
                                Log.v(TAG, "Created a file: " + driveFileResult.getDriveFile().getDriveId());
                            }
                        });
            }
        });
    }


    public void changeAccount() {

        mGoogleApiClient.clearDefaultAccountAndReconnect();
        String accountName = getAccountName();
        Log.e("##########TEST", "Accountname:"+accountName);
        selectAccount.setText(accountName);

    }




    private String getAccountName() {

        String accountName = "test";
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] list = manager.getAccounts();

        for (Account account : list) {
            if (account.type.equalsIgnoreCase("com.google")) {
                accountName = account.name;
            }
        }
        return accountName;
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    public void writeToFile(String fileName, String body) {
        FileOutputStream fos = null;
        try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/xtests/");
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.e("ALERT", "U AR A MORON!  could not create the directories. CHECK THE FUCKING PERMISSIONS SON!");
                }
            }
            final File myFile = new File(dir, fileName + "_" + String.valueOf(System.currentTimeMillis()) + ".txt");
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            fos = new FileOutputStream(myFile);
            fos.write(body.getBytes());
            fos.close();
            Toast.makeText(A_Export_Drive.this, "File created ok! Let me give you a fucking congratulations!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "+++++++++++++++++++ onConnected +++++++++++++++++++");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended [" + String.valueOf(i) + "]");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "U AR A MORON! Exception while starting resolution activity", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == RESULT_OK) {
            //String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }
}