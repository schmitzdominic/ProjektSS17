package de.projektss17.bonpix;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import de.projektss17.bonpix.recognition.C_Rules;

public class A_Show_Recognition extends AppCompatActivity {

    ImageView imageView;
    TextView txtResult;
    C_Rules rules;
    public static final String TESS_DATA = "/tessdata";
    private static final String DATA_PATH = Environment.getExternalStorageDirectory() + "/BonPix/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_show_recognition_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtResult = (TextView) findViewById(R.id.show_recognition_textview_result);

        ArrayList<String> aList = getIntent().getStringArrayListExtra("ArrayList");
        this.rules = new C_Rules();

        File imgFile = new File(aList.get(aList.size()-1));
        if(imgFile.exists())
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView=(ImageView)findViewById(R.id.show_recognition_imageview);
            imageView.setImageBitmap(myBitmap);
            this.recognize(myBitmap);
        }

    }

    private void recognize(Bitmap bitmap){
        prepareTessDAta();
        txtResult.setText(startOCR(bitmap));
    }

    private void prepareTessDAta(){

        try{
            File dir = new File(DATA_PATH + TESS_DATA);
            if(!dir.exists()){
                dir.mkdirs();
            }
            String fileList[] = getAssets().list("");
            for(String fileName : fileList){
                String pathToDataFile = DATA_PATH+TESS_DATA+"/"+fileName;
                if(!(new File(pathToDataFile)).exists()){
                    InputStream in = getAssets().open(fileName);
                    OutputStream out = new FileOutputStream(pathToDataFile);
                    byte [] buff = new byte[1024];
                    int len ;
                    while((len = in.read(buff)) > 0){
                        out.write(buff,0,len);
                    }
                    in.close();
                    out.close();
                }
            }

        } catch (IOException e) {
            //e.printStackTrace();
        }

    }

    private String startOCR(Bitmap bitmap){
        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.init(DATA_PATH,"deu");
        tessBaseAPI.setImage(bitmap);
        String result = "No Result";
        result = tessBaseAPI.getUTF8Text();
        tessBaseAPI.end();

        return result;
    }

}
