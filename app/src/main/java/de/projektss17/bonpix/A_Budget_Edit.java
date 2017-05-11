package de.projektss17.bonpix;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Johanns am 11.05.2017
 */

public class A_Budget_Edit extends AppCompatActivity {

    Button saveButton;
    EditText title, betrag, zeitraumVon, zeitraumBis, info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_budget_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Implementierung der Objekte in dieser Activity
        this.saveButton = (Button) findViewById(R.id.budget_save_button);
        this.betrag = (EditText) findViewById(R.id.budget_alert_dialog_betrag);
        this.title = (EditText) findViewById(R.id.budget_alert_dialog_title);
        this.zeitraumVon = (EditText) findViewById(R.id.budget_alert_dialog_zeitraum_von);
        this.zeitraumBis = (EditText) findViewById(R.id.budget_alert_dialog_zeitraum_bis);
        this.info = (EditText) findViewById(R.id.budget_alert_dialog_info);


        /**
         * Button OnClickListener - Beim Drücken werden dein Eingaben gespeichert
         */
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                S.outShort(A_Budget_Edit.this, "Eingaben gespeichert!");

            }
        }
        );

    }

}
