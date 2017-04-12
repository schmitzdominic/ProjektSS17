package de.projektss17.bonpix;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class A_OCR_Manuell extends AppCompatActivity {

    private Button saveButton, kameraButton;
    private Spinner spinnerLaden;
    private Calendar calendar;
    private TextView dateView;
    private ImageView imageOCR;
    private ArrayAdapter<String> spinnerAdapter;
    private String year, month, day;
    private String imageOCRUriString;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_ocr_manuell_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Kalender
        this.dateView = (TextView) findViewById(R.id.ocr_manuell_datum);
        this.calendar = Calendar.getInstance();
        this.year = "" + this.calendar.get(Calendar.YEAR);
        this.month = "" + this.getNumberWithZero(calendar.get(Calendar.MONTH) + 1);
        this.day = "" + this.getNumberWithZero(calendar.get(Calendar.DAY_OF_MONTH));
        this.showDate(year, month, day);
        this.dateView.setTextColor(Color.RED);

        this.imageOCR = (ImageView) findViewById(R.id.ocr_manuell_image_ocr); // Image OCR Element
        this.kameraButton = (Button) findViewById(R.id.ocr_manuell_image_button_auswahl); // Image auswahl Button
        this.spinnerLaden = (Spinner) findViewById(R.id.ocr_manuell_spinner_laden); // Spinner Laden Element
        this.saveButton = (Button) findViewById(R.id.ocr_manuell_save_button); // Speichern Button

        this.imageOCR.setClickable(false); // Icon ist am anfang nicht klickbar
        this.refreshSpinner(); // Spinner Refresh

        // Aktion welches beim drücken des Save-Buttons ausgeführt wird
        // In diesem Fall wird ein Hinweis-Fenster (POPUP) geöffnet (Nachfragen ob Speicherung durchgenommen werden soll)
        this.saveButton.setOnClickListener(new View.OnClickListener() {
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

        // Onclick listener image OCR
        this.imageOCR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(imageOCRUriString != null){
                    S.showMaxBonPic(A_OCR_Manuell.this, imageOCRUriString);
                }

            }
        });

        // klick auf den Kamera auswahl button
        this.kameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        // Wenn Spinner Bitte Laden auswählen anzeigt, wird der Text Rot markiert
        if(this.spinnerLaden.getSelectedItemPosition() == 0){
            spinnerLaden.setSelection(0, true);
            View v = spinnerLaden.getSelectedView();
            ((TextView)v).setTextColor(Color.RED);
        }

        //Spinner selected listener => Aktion beim selektieren
        spinnerLaden.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            /**
             * Durch diese Methode lassen sich Aktionen beim selektieren der Spinner Werte durchführen
             * @param parentView
             * @param selectedItemView
             * @param position
             * @param id
             */
            @Override
            public void onItemSelected(final AdapterView<?> parentView, View selectedItemView, int position, long id) {

                int itemId = (int) id;

                /*
                 * Itemid == 1 = Benutzerdefiniert, d.h. Wenn manuell eine Marke eingegeben werden soll
                 */
                if (itemId == 1) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(A_OCR_Manuell.this);

                    final EditText input = new EditText(A_OCR_Manuell.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setMessage("Bitte Laden eingeben")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    /*
                                    TODO
                                    1. in die Datenbank schreiben
                                    über input.getText().toString() den eingegebenen Text auslesen
                                    und in die datenbank schreiben
                                    Mit z.B. S.addSpinnerLaedenManuell()
                                    2. datenauslesen und in die parentView eintragen
                                    Mit z.B. S.getSpinnerLaedenManuell() in eine entsprechende Liste
                                    refreshSpinner();
                                    3. selection setzen
                                    parentView.setSelection(PLATZINDERENTSPRECHENDENLISTE);
                                    */
                                    // TODO Remove later the following 2 lines!
                                    Toast.makeText(A_OCR_Manuell.this, input.getText().toString(), Toast.LENGTH_LONG).show();
                                    parentView.setSelection(0);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    parentView.setSelection(0);
                                }
                            })
                            .create().show();
                }
            }

            /**
             * Ähnlich wie obige, nur wenn nichts selektiert wurde
             * @param parentView
             */
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                parentView.setSelection(0);
            }


        });
    }

    /**
     * Kleine Notification (als Toast)
     * beim öffnen des kalenders
     *
     * @param view
     */
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Bitte Datum auswählen",
                Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * TODO Bitte änderrn, da deprecated!
     *
     * @param id
     * @return
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate("" + getNumberWithZero(arg1),
                            "" + getNumberWithZero(arg2 + 1),
                            "" + getNumberWithZero(arg3));
                }
            };

    /**
     * Setzt das Aktuelle Datum in die TextView
     *
     * @param year
     * @param month
     * @param day
     */
    private void showDate(String year, String month, String day) {
        String separator = ".";
        dateView.setText(day + separator +
                month + separator +
                year);
    }

    /**
     * Gibt eine Zahl wenn sie kleiner 10 ist mit einer 0 davor aus
     *
     * @param zahl
     * @return
     */
    public String getNumberWithZero(int zahl) {
        if (zahl > 0 && zahl < 10) {
            return "0" + zahl;
        } else {
            return "" + zahl;
        }
    }

    /**
     * Methode zum Auslesen Daten aus Datenbank für Spinner / Refresh
     */
    public void refreshSpinner() {

        // 1. lese Optionen aus Datenbank
        // Mit z.B. S.getSpinnerLaedenManuell() in eine entsprechende Liste
        // 2. füge diese Daten zu einem String Array hinzu
        // TODO Remove next line after database has implmented
        String array[] = {"Bitte Laden auswählen","Hinzufügen", "EDEKA", "REWE", "MEDIA MARKT"};

        this.spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array);
        this.spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerLaden.setAdapter(this.spinnerAdapter);
    }

    /**
     * Was passiert wenn das Bild ausgewählt wurde
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri imageUri = data.getData();
            this.imageOCR.setImageURI(null);
            this.imageOCR.setImageURI(imageUri);
            this.imageOCRUriString = imageUri.toString();
            this.imageOCR.setClickable(true);
        }
    }
}
