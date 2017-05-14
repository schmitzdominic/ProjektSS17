package de.projektss17.bonpix;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

import de.projektss17.bonpix.daten.C_Artikel;
import de.projektss17.bonpix.daten.C_Bon;

import static de.projektss17.bonpix.S.db;

public class A_Bon_Anzeigen extends AppCompatActivity {

    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_bon_anzeigen_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent mIntent = getIntent();
        this.pos = mIntent.getIntExtra("BonPos", pos);
        C_Bon bon = S.dbHandler.getBon(db, pos);


        TextView textView_ladenname = (TextView) findViewById(R.id.ladenname);
        textView_ladenname.setText("Ladenname: " + bon.getPath());

        TextView textView_adresse = (TextView) findViewById(R.id.adresse);
        textView_adresse.setText("Adresse: " + bon.getAdress());

        TextView textView_datum = (TextView) findViewById(R.id.datum);
        textView_datum.setText("Datum: " + bon.getDate());

        double gesBetrag = 0;

        for(C_Artikel article : bon.getArticles()){
            gesBetrag += article.getPrice();
        }

        gesBetrag = Math.round(gesBetrag * 100) / 100.00;

        DecimalFormat df = new DecimalFormat("#0.00");

        TextView textView_gesamtbetrag = (TextView) findViewById(R.id.gesamtbetrag);
        textView_gesamtbetrag.setText("Adresse: " + df.format(gesBetrag));

        Log.e("### BonAnzeigen","BonPosition: " + pos);

/*        for(C_Artikel article : bon.getArticles()){
            this.inflateEditRow(article.getName(), article.getPrice());
        }*/


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                S.showManuell(A_Bon_Anzeigen.this, pos, "edit");
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
