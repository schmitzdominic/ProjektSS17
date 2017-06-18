package de.projektss17.bonpix.daten;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.drive.query.Filter;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.google.android.gms.drive.DriveApi.MetadataBufferResult;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.DriveFolder.DriveFolderResult;
import com.google.android.gms.drive.DriveResource.MetadataResult;
import com.google.android.gms.drive.MetadataChangeSet.Builder;


public class C_Drive_API { public C_Drive_API() {}
    public interface ConnectCBs {
        void onConnFail(ConnectionResult connResult);
        void onConnOK();
    }
    private static GoogleApiClient mGAC;
    private static ConnectCBs mConnCBs;

    /************************************************************************************************
     * initialize Google Drive Api
     * @param act activity context
     */
    public static boolean init(Activity act) {
        if (act != null) {
            String email = C_Drive_UT.AM.getEmail();                                                 //UT.lg("emil " + email);
            if (email != null) try {
                mConnCBs = (ConnectCBs) act;
                mGAC = new GoogleApiClient.Builder(act)
                        .addApi(Drive.API)
                        .addScope(Drive.SCOPE_FILE)
                        .addScope(Drive.SCOPE_APPFOLDER)
                        .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnectionSuspended(int i) {
                            }

                            @Override
                            public void onConnected(Bundle bundle) {
                                mConnCBs.onConnOK();
                            }
                        })
                        .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(ConnectionResult connectionResult) {
                                mConnCBs.onConnFail(connectionResult);
                            }
                        })
                        .setAccountName(email)
                        .build();
                return true;
            } catch (Exception e) {C_Drive_UT.le(e);}
        }
        return false;
    }
    /**
     * connect    connects GoogleApiClient
     */
    public static void connect() {
        if (C_Drive_UT.AM.getEmail() != null && mGAC != null && !mGAC.isConnecting() && !mGAC.isConnected()) {  //UT.lg("conn");
            mGAC.connect();
        }
    }
    /**
     * disconnect    disconnects GoogleApiClient
     */
    public static void disconnect() {
        if (mGAC != null && mGAC.isConnected()) {
            mGAC.disconnect();
        }
    }

    /*
     * find folder in GOODrive
     * @param prnId parent ID (optional),
     *                null searches full drive,
     *                "root" searches Drive root
     *                "appfolder"  searches Drive app folder
     * @param titl  file/folder name (optional)
     * @param mime  file/folder mime type (optional)
     * @return arraylist of found objects
     */
    public static ArrayList<ContentValues> search(String prnId, String titl, String mime) {
        ArrayList<ContentValues> gfs = new ArrayList<>();
        if (mGAC != null && mGAC.isConnected()) try {
            // add query conditions, build query
            ArrayList<Filter> fltrs = new ArrayList<>();
            if (prnId != null) {
                if (prnId.equalsIgnoreCase("root")) {
                    fltrs.add(Filters.in(SearchableField.PARENTS, Drive.DriveApi.getRootFolder(mGAC).getDriveId()));
                } else if (prnId.equalsIgnoreCase("appfolder")) {
                    fltrs.add(Filters.in(SearchableField.PARENTS, Drive.DriveApi.getAppFolder(mGAC).getDriveId()));
                } else {
                    fltrs.add(Filters.in(SearchableField.PARENTS, DriveId.decodeFromString(prnId)));
                }
            }
            if (titl != null) fltrs.add(Filters.eq(SearchableField.TITLE, titl));
            if (mime != null) fltrs.add(Filters.eq(SearchableField.MIME_TYPE, mime));
            Query qry = new Query.Builder().addFilter(Filters.and(fltrs)).build();

            // fire the query
            MetadataBufferResult rslt = Drive.DriveApi.query(mGAC, qry).await();
            if (rslt.getStatus().isSuccess()) {
                MetadataBuffer mdb = null;
                try {
                    mdb = rslt.getMetadataBuffer();
                    for (Metadata md : mdb) {
                        if (md == null || !md.isDataValid() || md.isTrashed()) continue;
                        gfs.add(C_Drive_UT.newCVs(md.getTitle(), md.getDriveId().encodeToString(), md.getMimeType()));
                    }
                } finally { if (mdb != null) mdb.close(); }
            }
        } catch (Exception e) { C_Drive_UT.le(e); }
        return gfs;
    }
    /************************************************************************************************
     * create file/folder in GOODrive
     * @param prnId parent's ID, null or "root" for root, "appfolder" for app folder
     * @param titl  file name
     * @return file id  / null on fail
     */
    public static String createFolder(String prnId, String titl) {
        DriveId dId = null;
        if (mGAC != null && mGAC.isConnected() && titl != null) try {
            DriveFolder pFldr;
            if (prnId != null) {
                if (prnId.equalsIgnoreCase("root")) {
                    pFldr =  Drive.DriveApi.getRootFolder(mGAC);
                } else if (prnId.equalsIgnoreCase("appfolder")) {
                    pFldr =  Drive.DriveApi.getAppFolder(mGAC);
                } else {
                    pFldr = Drive.DriveApi.getFolder(mGAC, DriveId.decodeFromString(prnId));
                }
            } else
                pFldr = Drive.DriveApi.getRootFolder(mGAC);
            if (pFldr == null) return null;

            MetadataChangeSet meta;
            meta = new Builder().setTitle(titl).setMimeType(C_Drive_UT.MIME_FLDR).build();
            DriveFolderResult r1 = pFldr.createFolder(mGAC, meta).await();
            DriveFolder dFld = (r1 != null) && r1.getStatus().isSuccess() ? r1.getDriveFolder() : null;
            if (dFld != null) {
                MetadataResult r2 = dFld.getMetadata(mGAC).await();
                if ((r2 != null) && r2.getStatus().isSuccess()) {
                    dId = r2.getMetadata().getDriveId();
                }
            }
        } catch (Exception e) { C_Drive_UT.le(e); }
        return dId == null ? null : dId.encodeToString();
    }
    /************************************************************************************************
     * create file in GOODrive
     * @param prnId parent's ID, (null or "root") for root
     * @param titl  file name
     * @param mime  file mime type
     * @param file  file (with content) to create
     * @return file id  / null on fail
     */
    public static String createFile(String prnId, String titl, String mime, File file) {
        DriveId dId = null;
        if (mGAC != null && mGAC.isConnected() && titl != null && mime != null && file != null) try {
            DriveFolder pFldr = (prnId == null || prnId.equalsIgnoreCase("root")) ?
                    Drive.DriveApi.getRootFolder(mGAC) :
                    Drive.DriveApi.getFolder(mGAC, DriveId.decodeFromString(prnId));
            if (pFldr != null) {
                DriveContents cont = file2Cont(null, file);
                MetadataChangeSet meta = new Builder().setTitle(titl).setMimeType(mime).build();
                DriveFileResult r1 = pFldr.createFile(mGAC, meta, cont).await();
                DriveFile dFil = r1 != null && r1.getStatus().isSuccess() ? r1.getDriveFile() : null;
                if (dFil != null) {
                    MetadataResult r2 = dFil.getMetadata(mGAC).await();
                    if (r2 != null && r2.getStatus().isSuccess()) {
                        dId = r2.getMetadata().getDriveId();
                    }
                }
            }
        } catch (Exception e) { C_Drive_UT.le(e); }
        return dId == null ? null : dId.encodeToString();
    }
    /************************************************************************************************
     * get file contents
     * @param id file driveId
     * @return file's content  / null on fail
     */
    public static byte[] read(String id) {
        byte[] buf = null;
        if (mGAC != null && mGAC.isConnected() && id != null) try {
            DriveFile df = Drive.DriveApi.getFile(mGAC, DriveId.decodeFromString(id));
            DriveContentsResult rslt = df.open(mGAC, DriveFile.MODE_READ_ONLY, null).await();
            if ((rslt != null) && rslt.getStatus().isSuccess()) {
                DriveContents cont = rslt.getDriveContents();
                buf = C_Drive_UT.is2Bytes(cont.getInputStream());
                cont.discard(mGAC);    // or cont.commit();  they are equiv if READONLY
            }
        } catch (Exception e) { C_Drive_UT.le(e); }
        return buf;
    }
    /************************************************************************************************
     * update file in GOODrive
     * @param drvId file  id
     * @param titl  new file name (optional)
     * @param mime  new mime type (optional, "application/vnd.google-apps.folder" indicates folder)
     * @param file  new file content (optional)
     * @return success status
     */
    public static boolean update(String drvId, String titl, String mime, String desc, File file) {
        Boolean bOK = false;
        if (mGAC != null && mGAC.isConnected() && drvId != null) try {
            Builder mdBd = new Builder();
            if (titl != null) mdBd.setTitle(titl);
            if (mime != null) mdBd.setMimeType(mime);
            if (desc != null) mdBd.setDescription(desc);
            MetadataChangeSet meta = mdBd.build();

            if (mime != null && C_Drive_UT.MIME_FLDR.equals(mime)) {
                DriveFolder dFldr = Drive.DriveApi.getFolder(mGAC, DriveId.decodeFromString(drvId));
                MetadataResult r1 = dFldr.updateMetadata(mGAC, meta).await();
                bOK = (r1 != null) && r1.getStatus().isSuccess();

            } else {
                DriveFile dFile = Drive.DriveApi.getFile(mGAC, DriveId.decodeFromString(drvId));
                MetadataResult r1 = dFile.updateMetadata(mGAC, meta).await();
                if ((r1 != null) && r1.getStatus().isSuccess() && file != null) {
                    DriveContentsResult r2 = dFile.open(mGAC, DriveFile.MODE_WRITE_ONLY, null).await();
                    if (r2.getStatus().isSuccess()) {
                        DriveContents cont = file2Cont(r2.getDriveContents(), file);
                        Status r3 = cont.commit(mGAC, meta).await();
                        bOK = (r3 != null && r3.isSuccess());
                    }
                }
            }
        } catch (Exception e) { C_Drive_UT.le(e); }
        return bOK;
    }
    /************************************************************************************************
     * trash file in GOODrive
     * @param drvId file  id
     * @return success status
     */
    public static boolean trash(String drvId) {
        Boolean bOK = false;
        if (mGAC != null && mGAC.isConnected() && drvId != null) try {
            DriveId dId = DriveId.decodeFromString(drvId);
            DriveResource driveResource;
            if (dId.getResourceType() == DriveId.RESOURCE_TYPE_FOLDER) {
                driveResource = Drive.DriveApi.getFolder(mGAC, dId);
            } else {
                driveResource = Drive.DriveApi.getFile(mGAC, dId);
            }
            Status rslt = driveResource == null ? null : driveResource.trash(mGAC).await();
            bOK = rslt != null && rslt.isSuccess();
        } catch (Exception e) { C_Drive_UT.le(e); }
        return bOK;
    }

    /************************************************************************************************
     * create file/folder in GOODrive
     * @param prnId parent's ID, (null or "root") for root
     * @param titl  file name
     * @param mime  file mime type
     * @param file  file (with content) to create
     * @return intent sender/ null on fail
     */
    public static IntentSender createFileAct(String prnId, String titl, String mime, File file) {
        if (mGAC != null && mGAC.isConnected() && titl != null && mime != null && file != null) try {
            DriveFolder pFldr = (prnId == null || prnId.equalsIgnoreCase("root")) ?
                    Drive.DriveApi.getRootFolder(mGAC) :
                    Drive.DriveApi.getFolder(mGAC, DriveId.decodeFromString(prnId));
            if (pFldr != null) {
                DriveContents dc = file2Cont(null, file);
                MetadataChangeSet meta = new Builder().setTitle(titl).setMimeType(mime).build();

                return Drive.DriveApi.newCreateFileActivityBuilder()
                        .setActivityStartFolder(pFldr.getDriveId())
                        .setInitialMetadata(meta).setInitialDriveContents(dc)
                        .build(mGAC);
            }
        } catch (Exception e) { C_Drive_UT.le(e); }
        return null;
    }
    public static String getId(Intent data){
        return ((DriveId)data.getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID)).encodeToString();
    }

    /************************************************************************************************
     * pick a file in GOODrive
     * @param prnId parent's ID, (null or "root") for root
     * @param mimes  file mime types
     * @return intent sender/ null on fail
     */
    public static IntentSender pickFile(String prnId, String[] mimes) {
        if (mGAC != null && mGAC.isConnected() && mimes != null) try {
            DriveFolder pFldr = (prnId == null || prnId.equalsIgnoreCase("root")) ?
                    Drive.DriveApi.getRootFolder(mGAC) :
                    Drive.DriveApi.getFolder(mGAC, DriveId.decodeFromString(prnId));
            if (pFldr != null) {
                return Drive.DriveApi.newOpenFileActivityBuilder()
                        .setActivityStartFolder(pFldr.getDriveId())
                        .setMimeType(mimes)
                        .build(mGAC);
            }
        } catch (Exception e) { C_Drive_UT.le(e); }
        return null;
    }

    /**
     * FILE / FOLDER type object inquiry
     *
     * @param cv oontent values
     * @return TRUE if FOLDER, FALSE otherwise
     */
    public static boolean isFolder(ContentValues cv) {
        String gdId = cv.getAsString(C_Drive_UT.GDID);
        DriveId dId = gdId != null ? DriveId.decodeFromString(gdId) : null;
        return dId != null && dId.getResourceType() == DriveId.RESOURCE_TYPE_FOLDER;
    }

    public static DriveContents file2Cont(DriveContents cont, File file) {
        if (file == null) return null;
        if (cont == null) {
            DriveApi.DriveContentsResult r1 = Drive.DriveApi.newDriveContents(mGAC).await();
            cont = r1 != null && r1.getStatus().isSuccess() ? r1.getDriveContents() : null;
        }
        if (cont != null) try {
            OutputStream oos = cont.getOutputStream();
            if (oos != null) try {
                InputStream is = new FileInputStream(file);
                byte[] buf = new byte[4096];
                int c;
                while ((c = is.read(buf, 0, buf.length)) > 0) {
                    oos.write(buf, 0, c);
                    oos.flush();
                }
            }
            finally { oos.close();}
            return cont;
        } catch (Exception ignore)  {}
        return null;
    }
}

