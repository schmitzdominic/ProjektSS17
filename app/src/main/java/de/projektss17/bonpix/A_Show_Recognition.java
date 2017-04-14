package de.projektss17.bonpix;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.util.ArrayList;

import de.projektss17.bonpix.recognition.C_Rules;

public class A_Show_Recognition extends AppCompatActivity {

    ImageView imageView;
    TextView txtResult;
    C_Rules rules;

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

    /**
     * Ließt den String eines Bildes über OCR aus und
     * gibt diesen an eine TextView weiter
     * @param bitmap
     */
    private void recognize(Bitmap bitmap){
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if(!textRecognizer.isOperational()){
            Log.e("ERROR","Detector dependencies are not yet available");
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for(int i=0; i<items.size();++i){
                TextBlock item = items.valueAt(i);
                stringBuilder.append(item.getValue());
                stringBuilder.append("\n");
            }

            // String wird richtig formatiert
            String textFormated = this.rules.formater(stringBuilder.toString());
            String all = textFormated + "\n\n\n";
            String plz = "PLZ.: " + this.rules.getPLZ(textFormated) + "\n";
            String tel = "Tel.: " + this.rules.getTel(textFormated) + "\n";
            String prices = "Preise: " + this.rules.getPrices(textFormated) + "\n";

            // Formatierte Strings werden an eine TextView gegeben und angezeigt.
            txtResult.setText(all+plz+tel+prices);
        }
    }
}