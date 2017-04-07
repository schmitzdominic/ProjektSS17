package de.projektss17.bonpix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

public class A_Einstellungen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_einstellungen_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton versionButton = (ImageButton) findViewById(R.id.einstellungen_version_button);

        versionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                S.showVersion(A_Einstellungen.this);
            }
        });
    }

}
