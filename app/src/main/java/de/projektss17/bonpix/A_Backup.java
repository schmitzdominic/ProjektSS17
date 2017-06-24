package de.projektss17.bonpix;

import android.accounts.AccountManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.projektss17.bonpix.daten.C_Drive_API;
import de.projektss17.bonpix.daten.C_Drive_UT;



public class A_Backup extends PreferenceActivity implements C_Drive_API.ConnectCBs, Preference.OnPreferenceClickListener{

    private static final int REQ_ACCPICK = 1;
    private static final int REQ_CONNECT = 2;
    private static final int REQ_CREATE = 3;
    private static final int REQ_PICKFILE = 4;

    private static boolean mBusy;
    public Context context;
    public Preference change;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("A_BACKUP","ONCREATE #########################");
        addPreferencesFromResource(R.xml.box_backup_preferences);

        if (savedInstanceState == null) {
            C_Drive_UT.init(this);
            if (!C_Drive_API.init(this)) {
                startActivityForResult(AccountPicker.newChooseAccountIntent(null,
                        null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null),
                        REQ_ACCPICK);
            }
        }

        change = findPreference("change_account");
        change.setSummary(S.prefs.getPrefString("account_name"));
        change.setOnPreferenceClickListener(this);
        Preference export = findPreference("export_photos");
        export.setOnPreferenceClickListener(this);
        Preference _import = findPreference("import_photos");
        _import.setOnPreferenceClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        C_Drive_API.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        C_Drive_API.disconnect();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        Toolbar bar;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
            bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.box_backup_toolbar, root, false);
            root.addView(bar, 0);
        } else {
            ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            ListView content = (ListView) root.getChildAt(0);
            root.removeAllViews();
            bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.box_backup_toolbar, root, false);
            int height;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            } else {
                height = bar.getHeight();
            }
            content.setPadding(0, height, 0, 0);
            root.addView(content);
            root.addView(bar);
        }
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onPreferenceClick(Preference preference){
        switch(preference.getKey()){
            case "change_account":
                startActivityForResult(AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null), REQ_ACCPICK);
                break;
            case "export_photos":
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> pathList = S.dbHandler.getAllBonsPath(S.db);
                        for (String path : pathList){
                            Log.e("EXPORT PHOTOS LOOP", "###" + path);
                            File fl = C_Drive_UT.bitToFile(path);
                            String titl = C_Drive_UT.time2Titl(null);
                            IntentSender is = null;
                            if (fl != null) {
                                is = C_Drive_API.createFileAct(null, titl, C_Drive_UT.MIME_PNG, fl);
                                fl.delete();
                                boolean x = false;
                                if (is != null){
                                    x = true;
                                }
                            }
                            if (is == null){
                                Log.e("WEHE!!!","xxx");
                            }
                            else try {
                                startIntentSenderForResult(is, REQ_CREATE, null, 0, 0, 0);
                            } catch (Exception e) {
                                C_Drive_UT.le(e);
                            }
                        }
                    }
                }).start();
                break;
            case "import_photos":
                // TODO
                Log.e("IMPORT PHOTOS","##############");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //{UT.MIME_FLDR} or {DriveFolder.MIME_TYPE} for folder
                        IntentSender is = C_Drive_API.pickFile(null, new String[] {C_Drive_UT.MIME_TEXT});
                        if (is == null){
                            //mDispTxt.append("\n failed ");
                        }
                        else try {
                            startIntentSenderForResult( is, REQ_PICKFILE, null, 0, 0, 0);
                        } catch (Exception e) { C_Drive_UT.le(e); }
                    }
                }).start();
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int request, int result, Intent data) {
        switch (request) {
            case REQ_CONNECT:
                if (result == RESULT_OK)
                    C_Drive_API.connect();
                else
                    suicide(R.string.err_author);
                break;
            case REQ_ACCPICK:
                if (data != null && data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME) != null)
                    C_Drive_UT.AM.setEmail(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
                    change.setSummary(S.prefs.getPrefString("account_name"));
                if (!C_Drive_API.init(this))
                    suicide(R.string.err_author);
                break;
            case REQ_CREATE:
                if (result == RESULT_OK) {
                    //mDispTxt.append("\n success " + C_Drive_API.getId(data));
                } else {
                    //mDispTxt.append("\n failed");
                }
                break;
            case REQ_PICKFILE:
                if (result == RESULT_OK) {
                    //mDispTxt.append("\nPICKFILE OK " + C_Drive_API.getId(data));
                } else {
                    //mDispTxt.append("\nPICKFILE FAIL ");
                }
                break;
        }
        super.onActivityResult(request, result, data);
    }

    private void suicide(int rid) {
        C_Drive_UT.AM.setEmail(null);
        Toast.makeText(context, rid, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onConnOK() {
        //mDispTxt.append("\n\nCONNECTED TO: " + C_Drive_UT.AM.getEmail());
    }

    @Override
    public void onConnFail(ConnectionResult connResult) {
        if (connResult != null  && connResult.hasResolution()) try {                    //UT.lg("connFail - has res");
            connResult.startResolutionForResult(this, REQ_CONNECT);
            return;  //++++++++++++++++++++++++++++++++++++++++++++++++++++++++>>>
        } catch (Exception e) { C_Drive_UT.le(e); }                                              //UT.lg("connFail - no res");
        suicide(R.string.err_author);
    }
    /**
     * creates a directory tree to house a text file
     * @param titl file name (confirms to 'yyMMdd-HHmmss' and it's name is used
     *             to create it's parent folder 'yyyy-MM' under a common root 'GDRTDemo'
     *             GDAADemo ---+--- yyyy-MM ---+--- yyMMdd-HHmmss
     *                         |               +--- yyMMdd-HHmmss
     *                         |                   ...
     *                         +--- yyyy-MM ---+--- yyMMdd-HHmmss
     *                                         +--- yyMMdd-HHmmss
     *                                              ....
     */
    private void createTree(final String titl) {
        if (titl != null && !mBusy) {
            //mDispTxt.setText("UPLOADING\n");
            new AsyncTask<Void, String, Void>() {
                private String findOrCreateFolder(String prnt, String titl){
                    ArrayList<ContentValues> cvs = C_Drive_API.search(prnt, titl, C_Drive_UT.MIME_FLDR);
                    String id, txt;
                    if (cvs.size() > 0) {
                        txt = "found ";
                        id =  cvs.get(0).getAsString(C_Drive_UT.GDID);
                    } else {
                        id = C_Drive_API.createFolder(prnt, titl);
                        txt = "created ";
                    }
                    if (id != null)
                        txt += titl;
                    else
                        txt = "failed " + titl;
                    publishProgress(txt);
                    return id;
                }
                @Override
                protected Void doInBackground(Void... params) {
                    mBusy = true;
                    //String rsid = findOrCreateFolder("appfolder", UT.MYROOT);  // app folder test
                    String rsid = findOrCreateFolder("root", C_Drive_UT.MYROOT);
                    if (rsid != null) {
                        rsid = findOrCreateFolder(rsid, C_Drive_UT.titl2Month(titl));
                        if (rsid != null) {
                            File fl = C_Drive_UT.str2File("content of " + titl, "tmp" );
                            String id = null;
                            if (fl != null) {
                                id = C_Drive_API.createFile(rsid, titl, C_Drive_UT.MIME_TEXT, fl);
                                fl.delete();
                            }
                            if (id != null)
                                publishProgress("created " + titl);
                            else
                                publishProgress("failed " + titl);
                        }
                    }
                    return null;
                }
                @Override
                protected void onProgressUpdate(String... strings) { super.onProgressUpdate(strings);
                    //mDispTxt.append("\n" + strings[0]);
                }
                @Override
                protected void onPostExecute(Void nada) { super.onPostExecute(nada);
                    //mDispTxt.append("\n\nDONE");
                    mBusy = false;
                }
            }.execute();
        }
    }
    /**
     *  scans folder tree created by this app listing folders / files, updating file's
     *  'description' meadata in the process
     */
    private void testTree() {
        if (!mBusy) {
            //mDispTxt.setText("DOWNLOADING\n");
            new AsyncTask<Void, String, Void>() {
                private void iterate(ContentValues gfParent) {
                    ArrayList<ContentValues> cvs = C_Drive_API.search(gfParent.getAsString(C_Drive_UT.GDID), null, null);
                    if (cvs != null) for (ContentValues cv : cvs) {
                        String gdid = cv.getAsString(C_Drive_UT.GDID);
                        String titl = cv.getAsString(C_Drive_UT.TITL);
                        if (C_Drive_API.isFolder(cv)) {
                            publishProgress(titl);
                            iterate(cv);
                        } else {
                            byte[] buf = C_Drive_API.read(gdid);
                            if (buf == null)
                                titl += " failed";
                            publishProgress(titl);
                            String str = buf == null ? "" : new String(buf);
                            File fl = C_Drive_UT.str2File(str + "\n updated " + C_Drive_UT.time2Titl(null), "tmp" );
                            if (fl != null) {
                                String desc = "seen " + C_Drive_UT.time2Titl(null);
                                C_Drive_API.update(gdid, null, null, desc, fl);
                                fl.delete();
                            }
                        }
                    }
                }
                @Override
                protected Void doInBackground(Void... params) {
                    mBusy = true;
                    //ArrayList<ContentValues> gfMyRoot = GDAA.search("appfolder", UT.MYROOT, null);  // app folder test
                    ArrayList<ContentValues> gfMyRoot = C_Drive_API.search("root", C_Drive_UT.MYROOT, null);
                    if (gfMyRoot != null && gfMyRoot.size() == 1 ){
                        publishProgress(gfMyRoot.get(0).getAsString(C_Drive_UT.TITL));
                        iterate(gfMyRoot.get(0));
                    }
                    return null;
                }
                @Override
                protected void onProgressUpdate(String... strings) {
                    super.onProgressUpdate(strings);
                    //mDispTxt.append("\n" + strings[0]);
                }
                @Override
                protected void onPostExecute(Void nada) {
                    super.onPostExecute(nada);
                    //mDispTxt.append("\n\nDONE");
                    mBusy = false;
                }
            }.execute();
        }
    }
    /**
     *  scans folder tree created by this app deleting folders / files in the process
     */
    private void deleteTree() {
        if (!mBusy) {
            //mDispTxt.setText("DELETING\n");
            new AsyncTask<Void, String, Void>() {
                private void iterate(ContentValues gfParent) {
                    ArrayList<ContentValues> cvs = C_Drive_API.search(gfParent.getAsString(C_Drive_UT.GDID), null, null);
                    if (cvs != null) for (ContentValues cv : cvs) {
                        String titl = cv.getAsString(C_Drive_UT.TITL);
                        String gdid = cv.getAsString(C_Drive_UT.GDID);
                        if (C_Drive_API.isFolder(cv))
                            iterate(cv);
                        publishProgress("  " + titl + (C_Drive_API.trash(gdid) ? "  DELETED" : " FAIL"));
                    }
                }
                @Override
                protected Void doInBackground(Void... params) {
                    mBusy = true;
                    //ArrayList<ContentValues> gfMyRoot = GDAA.search("appfolder", UT.MYROOT, null);  // app folder test
                    ArrayList<ContentValues> gfMyRoot = C_Drive_API.search("root", C_Drive_UT.MYROOT, null);
                    if (gfMyRoot != null && gfMyRoot.size() == 1 ){
                        ContentValues cv = gfMyRoot.get(0);
                        iterate(cv);
                        String titl = cv.getAsString(C_Drive_UT.TITL);
                        String gdid = cv.getAsString(C_Drive_UT.GDID);
                        publishProgress("  " + titl + (C_Drive_API.trash(gdid) ? " DELETED" : " FAIL"));
                    }
                    return null;
                }
                @Override
                protected void onProgressUpdate(String... strings) {
                    super.onProgressUpdate(strings);
                    //mDispTxt.append("\n" + strings[0]);
                }
                @Override
                protected void onPostExecute(Void nada) {
                    super.onPostExecute(nada);
                    //mDispTxt.append("\n\nDONE");
                    mBusy = false;
                }
            }.execute();
        }
    }
}