package de.projektss17.bonpix.daten;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.projektss17.bonpix.S;

public class C_Drive_UT {  private C_Drive_UT() {}

    private static final String L_TAG = "_X_";

    public static final String MYROOT = "DEMORoot";
    public static final String MIME_TEXT = "text/plain";
    public static final String MIME_FLDR = "application/vnd.google-apps.folder";
    public static final String MIME_PNG = "image/png";
    public static final String MIME_JPEG = "image/jpeg";


    public static final String TITL = "titl";
    public static final String GDID = "gdid";
    public static final String MIME = "mime";

    private static final String TITL_FMT = "yyMMdd-HHmmss";

    static Context acx;
    public static void init(Context ctx) {
        acx = ctx.getApplicationContext();
    }

    public static class AM { private AM(){}
        private static final String ACC_NAME = "account_name";
        private static String mEmail = null;

        public static void setEmail(String email) {
            S.prefs.savePrefString(ACC_NAME, mEmail = email);
        }
        public static String getEmail() {
            return mEmail != null ? mEmail : (mEmail = S.prefs.getPrefString(ACC_NAME));
        }
    }

    public static ContentValues newCVs(String titl, String gdId, String mime) {
        ContentValues cv = new ContentValues();
        if (titl != null) cv.put(TITL, titl);
        if (gdId != null) cv.put(GDID, gdId);
        if (mime != null) cv.put(MIME, mime);
        return cv;
    }

    private static File cchFile(String flNm) {
        File cche = C_Drive_UT.acx.getExternalCacheDir();
        return (cche == null || flNm == null) ? null : new File(cche.getPath() + File.separator + flNm);
    }
    public static File str2File(String str, String name) {
        if (str == null) return null;
        byte[] buf = str.getBytes();
        File fl = cchFile(name);
        if (fl == null) return null;
        BufferedOutputStream bs = null;
        try {
            bs = new BufferedOutputStream(new FileOutputStream(fl));
            bs.write(buf);
        } catch (Exception e) { le(e); }
        finally {
            if (bs != null) try {
                bs.close();
            } catch (Exception e) { le(e); }
        }
        return fl;
    }
    public static File bitToFile(String path){
        String fuck = Environment.getExternalStorageDirectory().toString() + "/Download";
        String fFile = "1_big.jpg";
        File file = new File(fuck, fFile);

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap != null){
            Log.e("BITMAP ########","CHECK");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    public static byte[] is2Bytes(InputStream is) {
        byte[] buf = null;
        BufferedInputStream bufIS = null;
        if (is != null) try {
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            bufIS = new BufferedInputStream(is);
            buf = new byte[4096];
            int cnt;
            while ((cnt = bufIS.read(buf)) >= 0) {
                byteBuffer.write(buf, 0, cnt);
            }
            buf = byteBuffer.size() > 0 ? byteBuffer.toByteArray() : null;
        } catch (Exception ignore) {}
        finally {
            try {
                if (bufIS != null) bufIS.close();
            } catch (Exception ignore) {}
        }
        return buf;
    }

    public static String time2Titl(Long milis) {       // time -> yymmdd-hhmmss
        Date dt = (milis == null) ? new Date() : (milis >= 0) ? new Date(milis) : null;
        return (dt == null) ? null : new SimpleDateFormat(TITL_FMT, Locale.US).format(dt);
    }
    public static String titl2Month(String titl) {
        return titl == null ? null : ("20" + titl.substring(0, 2) + "-" + titl.substring(2, 4));
    }

    public static void le(Throwable ex) {  Log.e(L_TAG, Log.getStackTraceString(ex));  }
    public static void lg(String msg) {  Log.d(L_TAG, msg); }
}


