package de.projektss17.bonpix;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class A_OCR_Manuell extends AppCompatActivity{

    private Button saveButton;
    private Spinner spinnerMarke;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_ocr_manuell_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Kalender
        dateView = (TextView) findViewById(R.id.ocr_manuell_Datum);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        //Referenzieren Spinner Element um Marke auszuwählen
        spinnerMarke = (Spinner) findViewById(R.id.ocr_manuell_spinnerMarke);

        // Referenzieren des Speichern-Button
        saveButton = (Button) findViewById(R.id.ocr_manuell_save_button);


        // Aktion welches beim drücken des Save-Buttons ausgeführt wird
        // In diesem Fall wird ein Hinweis-Fenster (POPUP) geöffnet (Nachfragen ob Speicherung durchgenommen werden soll)
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Aufruf der Static-Methode popUpDialog(), welches ein Hinweis-Fenster öffnet
                S.popUpDialog(A_OCR_Manuell.this, A_Main.class,
                        R.string.a_ocr_manuell_pop_up_title,
                        R.string.a_ocr_manuell_pop_up_message,
                        R.string.a_ocr_manuell_pop_up_cancel,
                        R.string.a_ocr_manuell_pop_up_confirm);
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterMarke = ArrayAdapter.createFromResource(this,
                                             R.array.marke_auswaehlen_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterMarke.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerMarke.setAdapter(adapterMarke);

        //Spinner selected listener => Aktion beim selektieren
        spinnerMarke.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            /**
             * Durch diese Methode lassen sich Aktionen beim selektieren der Spinner Werte durchführen
             * @param parentView
             * @param selectedItemView
             * @param position
             * @param id
             */
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int itemId = (int) id;

                /*
                 * Itemid == 1 = Benutzerdefiniert, d.h. Wenn manuell eine Marke eingegeben werden soll
                 */
                if(itemId == 1) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(A_OCR_Manuell.this);
                    View mView = getLayoutInflater().inflate(R.layout.box_ocr_manuell_marke_dialog, null);
                    EditText dialogMarke = (EditText)mView.findViewById(R.id.ocr_manuell_editTextDialog);
                    Button dialogButton = (Button)mView.findViewById(R.id.ocr_manuell_buttonDialog);

                    dialogButton.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            //Hier soll später ein Wert zurückgegben werden

                        }
                    });

                    //PopUp Dialog box_ocr_manuell_marke_dialog aufbauen
                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();

                }
            }
            /**
             * Ähnlich wie obige, nur wenn nichts selektiert wurde
             * @param parentView
             */
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    //Kalender
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Bitte Datum auswählen",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append(".")
                .append(month).append(".").append(year));
    }
    //Ende Kalender


}
