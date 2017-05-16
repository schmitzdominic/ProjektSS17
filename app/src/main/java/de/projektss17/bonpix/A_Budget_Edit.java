package de.projektss17.bonpix;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;


/**
 * Created by Johanns am 11.05.2017
 */

public class A_Budget_Edit extends AppCompatActivity implements View.OnClickListener{

    Button saveButton;
    EditText title, betrag, info;
    TextView zeitraumVon, zeitraumBis;
    int year, month, day;
    String [] contents;

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
        this.zeitraumVon = (TextView) findViewById(R.id.budget_alert_dialog_zeitraum_von);
        this.zeitraumBis = (TextView) findViewById(R.id.budget_alert_dialog_zeitraum_bis);
        this.info = (EditText) findViewById(R.id.budget_alert_dialog_info);

        // Setzen des Befehls zum klicken der Edit Text
        saveButton.setOnClickListener(this);
        zeitraumVon.setOnClickListener(this);
        zeitraumBis.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        // Je nachdem welche EditText angeklickt wird,so wird die Methode createDatePicker ausgeführt
        if(v==saveButton)
            prepareAndSave();
        else if(v==zeitraumVon)
            createDatePicker(zeitraumVon);
        else if(v==zeitraumBis)
            createDatePicker(zeitraumBis);
    }


    /**
     * Vorbereitung zur Befüllung der DB -> Nutzung der proof-Methoden (siehe unteren Code)
     * Speicherung der Daten wenn vollständig und korrekt
     * Wechseln auf die Haupt-Activity (A_Budget) nach dem speichern
     */
    public void prepareAndSave(){
        if(proofContent()) {

            S.outShort(A_Budget_Edit.this, "Daten korrekt!");

            //*****************************************************************************************************
            //**** HIER Anbindung zur DB herstellen - Befüllung dieser mit den Inhalten wenn alles korrekt ist ****
            //*****************************************************************************************************

            //CARD VIEW - Übergabe der Inhalte an A_Budget -> Lediglich ein Test (wird entfernt wenn Anbindung zur DB besteht)
            contents = new String[5];
            contents[0] = title.getText().toString();
            contents[1] = betrag.getText().toString();
            contents[2] = zeitraumVon.getText().toString();
            contents[3] = zeitraumBis.getText().toString();
            contents[4] = info.getText().toString();

            Intent intent = new Intent(A_Budget_Edit.this, A_Budget.class);
            intent.putExtra("content", contents);
            startActivity(intent);

        }else
            S.outShort(A_Budget_Edit.this,"Einige Daten unvollständig!");
    }


    /**
     * Created by Johanns am 13.05.2017
     *
     * Erstellt einen Kalender und führt diesen aus beim drücken der EditTextView
     * (Sobald Datum ausgewählt ist, werden die Inhalte in die EditText befüllt)
     * @param content Übergabe einer Edit Text View
     */
    public void createDatePicker(final TextView content){

        final Calendar c = Calendar.getInstance();
        this.day = c.get(Calendar.DAY_OF_MONTH);
        this.month = c.get(Calendar.MONTH);
        this.year = c.get(Calendar.YEAR);

        DatePickerDialog datePicker = new DatePickerDialog(A_Budget_Edit.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                content.setText(proofNumber(dayOfMonth)+"."+proofNumber(month+1)+"."+year);

            }
        },this.year, this.month, this.day);

        datePicker.show();
    }


    /**
     * Created by Johanns am 13.05.2017
     *
     * Überprüft eine Zahl und fügt eine 0 hinzu sofern die Zahl < 10
     * (wird für den Kalender verwendet)
     * @param content Übergabe der Zahl
     * @return Rückgabe der Zahl - je nachdem ob transformiert oder  nicht
     */
    public String proofNumber(int content){
        if(content<10)
            return "0"+Integer.toString(content);

        return Integer.toString(content);
    }


    /**
     * Created by Johanns am 14.05.2017
     *
     * Überprüft ob alle Felder befüllt sind - wenn nicht wird die Edit Text rot markiert
     * @return Rückgabe des Boolean-Wertes
     */
    public boolean proofContent(){

        boolean noError = true;

        if(title.getText()== null || title.getText().toString().isEmpty()){
            title.setHintTextColor(Color.RED);
            noError = false;
        }

        if(betrag.getText()== null || betrag.getText().toString().isEmpty()){
            betrag.setHintTextColor(Color.RED);
            noError = false;
        }

        if(zeitraumBis.getText()== null || zeitraumBis.getText().toString().isEmpty()) {
            zeitraumBis.setHintTextColor(Color.RED);
            noError = false;
        }

        if(zeitraumVon.getText()== null || zeitraumVon.getText().toString().isEmpty()) {
            zeitraumVon.setHintTextColor(Color.RED);
            noError = false;
        }

        return noError;
    }
}

