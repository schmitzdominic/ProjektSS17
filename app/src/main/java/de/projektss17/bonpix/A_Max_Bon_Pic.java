package de.projektss17.bonpix;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

public class A_Max_Bon_Pic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_max_bon_pic_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView image = (ImageView) findViewById(R.id.max_bon_pic_bild);

        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));

        image.setImageURI(null);
        image.setImageURI(imageUri);
    }
}
