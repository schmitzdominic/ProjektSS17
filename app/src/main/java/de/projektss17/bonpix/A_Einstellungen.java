package de.projektss17.bonpix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class A_Einstellungen extends AppCompatActivity {

    TextView backup, sprache, kontakt, appinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_einstellungen_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Text views instanziieren
        this.backup = (TextView) findViewById(R.id.einstellungen_backup_click);
        this.sprache = (TextView) findViewById(R.id.einstellungen_sprache_click);
        this.kontakt = (TextView) findViewById(R.id.einstellungen_kontakt_click);
        this.appinfo = (TextView) findViewById(R.id.einstellungen_app_info_click);

        // onClick listener
        this.backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.showBackup(A_Einstellungen.this);
            }
        });

        this.sprache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.showSprache(A_Einstellungen.this);
            }
        });

        this.kontakt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.showKontakt(A_Einstellungen.this);
            }
        });

        this.appinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.showVersion(A_Einstellungen.this);
            }
        });
    }

}
