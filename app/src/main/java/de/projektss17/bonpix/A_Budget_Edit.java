package de.projektss17.bonpix;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.projektss17.bonpix.daten.C_Budget;


/**
 * Created by Johanns am 11.05.2017
 */

public class A_Budget_Edit extends AppCompatActivity implements View.OnClickListener{

    Button saveButton;
    EditText title, betrag, info;
    TextView zeitraumVon, zeitraumBis;
    int year, month, day;
    C_Budget budget;

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


        if("edit".equals(getIntent().getStringExtra("state"))){

            this.budget = S.dbHandler.getBudget(S.db, Integer.parseInt(getIntent().getStringExtra("budget")));
            this.betrag.setText(this.budget.getBudgetMax() + "");
            this.title.setText(this.budget.getTitle());
            this.zeitraumVon.setText(this.budget.getZeitraumVon());
            this.zeitraumBis.setText(this.budget.getZeitraumBis());
            this.info.setText(this.budget.getSonstiges());
        }
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

            if("edit".equals(getIntent().getStringExtra("state"))){
                this.budget.setBudgetMax(Integer.parseInt(betrag.getText().toString()));
                this.budget.setBudgetLost((int) S.dbHandler.getTotalPriceFromBonsSumup(S.dbHandler.getBonsBetweenDate(S.db, zeitraumVon.getText().toString(), zeitraumBis.getText().toString())));
                this.budget.setZeitraumVon(zeitraumVon.getText().toString());
                this.budget.setZeitraumBis(zeitraumBis.getText().toString());
                this.budget.setTitle(title.getText().toString());
                this.budget.setSonstiges(info.getText().toString());
                this.budget.setBons(S.dbHandler.getBonsBetweenDate(S.db, zeitraumVon.getText().toString(), zeitraumBis.getText().toString()));

                S.dbHandler.updateBudget(S.db, this.budget);

            } else {
                S.dbHandler.addBudget(S.db, new C_Budget(
                        Integer.parseInt(betrag.getText().toString()),
                        (int) S.dbHandler.getTotalPriceFromBonsSumup(S.dbHandler.getBonsBetweenDate(S.db, zeitraumVon.getText().toString(), zeitraumBis.getText().toString())),
                        zeitraumVon.getText().toString(),
                        zeitraumBis.getText().toString(),
                        title.getText().toString(),
                        info.getText().toString(),
                        S.dbHandler.getBonsBetweenDate(S.db, zeitraumVon.getText().toString(), zeitraumBis.getText().toString())));
            }

            finish();

        }
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

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if(content.getText().toString().contains(".")){
            DatePickerDialog datePicker = new DatePickerDialog(A_Budget_Edit.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    content.setText(proofNumber(dayOfMonth)+"."+proofNumber(month+1)+"."+year);

                }
            },Integer.parseInt(content.getText().toString().split("\\.")[2]),
                    Integer.parseInt(content.getText().toString().split("\\.")[1])-1,
                    Integer.parseInt(content.getText().toString().split("\\.")[0]));
            datePicker.show();

        } else {
            DatePickerDialog datePicker = new DatePickerDialog(A_Budget_Edit.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    content.setText(proofNumber(dayOfMonth)+"."+proofNumber(month+1)+"."+year);

                }
            },this.year, this.month, this.day);
            datePicker.show();
        }



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

        if(zeitraumVon.getText() != null && !zeitraumVon.getText().toString().isEmpty()
                && zeitraumBis.getText() != null || !zeitraumBis.getText().toString().isEmpty()){

            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

            try {

                Date date1 = df.parse(zeitraumVon.getText().toString());
                Date date2 = df.parse(zeitraumBis.getText().toString());

                if(date1.after(date2)){
                    zeitraumBis.setTextColor(Color.RED);
                    zeitraumVon.setTextColor(Color.RED);
                    if(noError){
                        S.outLong(A_Budget_Edit.this, getApplicationContext().getResources().getString(R.string.a_budget_edit_vonbis_error));
                        noError = false;
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return noError;
    }

}