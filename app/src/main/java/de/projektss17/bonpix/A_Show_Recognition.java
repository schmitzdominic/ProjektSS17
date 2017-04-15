package de.projektss17.bonpix;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import de.projektss17.bonpix.recognition.C_OCR;
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

        C_OCR ocr = new C_OCR(getApplicationContext());
        ocr.recognize(bitmap);

        String all = ocr.getRecognizedText() + "\n\n\n";
        String plz = "Adresse: " + ocr.getAdresse() + "\n";
        String products = "Produkte: " + ocr.getProdukte() + "\n";
        String prices = "Preise: " + ocr.getPreise() + "\n";

        // Formatierte Strings werden an eine TextView gegeben und angezeigt.
        txtResult.setText(all+plz+products+prices);

    }
}

