package de.projektss17.bonpix;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

import de.projektss17.bonpix.daten.C_Artikel;
import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.daten.C_Laden;
import de.projektss17.bonpix.recognition.C_OCR;

import static de.projektss17.bonpix.S.db;


public class A_OCR_Manuell extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private int bonId;
    private String year, month, day, imageOCRUriString, sonstigesText;
    private boolean setFocusOnLine = true, negPos;
    private ArrayAdapter<String> spinnerAdapter;
    private Button  kameraButton, addArticleButton;
    private Spinner ladenSpinner;
    private Calendar calendar;
    private Calendar cal;
    private TextView dateTextView, totalPrice, sonstigesView;
    private ImageView ocrImageView;
    private View mExclusiveEmptyView;
    private EditText anschriftInput;
    private LinearLayout linearLayout;
    private ImageButton garantieButton, saveButton;
    private boolean garantieChanged = false;
    private C_OCR ocr;
    private C_Bon bon;
    private A_OCR_Manuell context = this;
    private int valuePicked, mYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_ocr_manuell_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // XML Instanziieren
        this.garantieButton = (ImageButton) findViewById(R.id.ocr_manuell_garantie_button); // Garantie Button
        this.saveButton = (ImageButton) findViewById(R.id.ocr_manuell_save_button); // Speichern Button
        this.linearLayout = (LinearLayout) findViewById(R.id.ocr_manuell_linear_layout); // Linear Layout
        this.ocrImageView = (ImageView) findViewById(R.id.ocr_manuell_image_ocr); // Image OCR Element
        this.kameraButton = (Button) findViewById(R.id.ocr_manuell_image_button_auswahl); // Image auswahl Button
        this.ladenSpinner = (Spinner) findViewById(R.id.ocr_manuell_spinner_laden); // Spinner Laden Element
        this.anschriftInput = (EditText) findViewById(R.id.ocr_manuell_edit_text_anschrift); // Anschrift eingabe
        this.dateTextView = (TextView) findViewById(R.id.ocr_manuell_datum); // Datumsanzeige
        this.sonstigesView = (TextView) findViewById(R.id.ocr_manuell_edit_text_sonstiges); // Sonstiges Button
        this.totalPrice = (TextView) findViewById(R.id.ocr_manuell_total_price); // Totaler Preis
        this.addArticleButton = (Button) findViewById(R.id.ocr_manuell_btn_add_new_article); // Neuen Artikel hinzufügen Button

        this.bon = new C_Bon("NA","", "", "", this.dateTextView.getText().toString(), "NA", "0", false, false, null); // Erstellt einen Leeren Bon
        this.ocr = new C_OCR(this); // Erstellt eine OCR instanz.
        this.createCalendar(); // Calendar wird befüllt
        this.refreshSpinner(); // Spinner Refresh
        this.doState(this.getState()); // Überprüft den Status und befüllt ggf.
        this.ocrImageView.setClickable(false); // Icon ist am anfang nicht klickbar


        /**
         * Guarantee Listener - Triggers Dialog (NumberPicker)
         * Is setting GuaranteeEnd with FormattedDate and Guarantee Boolean
         */
        this.garantieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bon.getGuarantee() == false) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.box_ocr_manuell_dialog_picker, null);
                    final NumberPicker picker = (NumberPicker) dialogView.findViewById(R.id.number_picker);
                    picker.setMaxValue(5);
                    picker.setMinValue(1);
                    picker.setValue(2);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(view.getContext().getResources().getString(R.string.a_ocr_manuell_garantie_laenge));
                    builder.setView(dialogView);
                    builder.setPositiveButton(view.getContext().getResources().getString(R.string.a_ocr_manuell_pop_up_confirm), new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int index){
                            int yearPicked = picker.getValue();
                            bon.setGuarantee(true);
                            valuePicked = yearPicked;
                            garantieButton.setColorFilter(R.color.colorPrimary);
                            garantieChanged = true;
                        }
                    });
                    builder.setNegativeButton(view.getContext().getResources().getString(R.string.a_ocr_manuell_pop_up_cancel), new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int index){
                            bon.setGuarantee(false);
                            bon.setGuaranteeEnd("NA");
                            garantieButton.setColorFilter(Color.WHITE);
                        }
                    });
                    final AlertDialog yearPickerDialog = builder.create();
                    yearPickerDialog.show();
                } else {
                    bon.setGuarantee(false);
                    bon.setGuaranteeEnd("NA");
                    garantieButton.setColorFilter(Color.WHITE);
                }
            }
        });


        // Save Button onClickListener
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkAllRelevantValues()) {

                    Intent upIntent = NavUtils.getParentActivityIntent(A_OCR_Manuell.this);

                    if(getState().equals("edit")){
                        S.dbHandler.updateBon(S.db, saveBon());
                        finish();

                    } else {
                        // Aufruf der Static-Methode popUpDialog(), welches ein Hinweis-Fenster öffnet
                        S.popUpDialogSaveBon(A_OCR_Manuell.this, upIntent,
                                R.string.a_ocr_manuell_pop_up_title,
                                R.string.a_ocr_manuell_pop_up_message,
                                R.string.a_ocr_manuell_pop_up_cancel,
                                R.string.a_ocr_manuell_pop_up_confirm,
                                saveBon());
                    }
                }
            }
        });

        // OCR Image onClickListener
        this.ocrImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(imageOCRUriString != null){
                    S.showMaxBonPic(A_OCR_Manuell.this, imageOCRUriString);
                }

            }
        });

        // kamera Button onClickListener
        this.kameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


        // Sonstiges Eingabe onClickListener
        this.sonstigesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(A_OCR_Manuell.this);

                final EditText input = new EditText(A_OCR_Manuell.this);
                input.setSingleLine(false);
                input.setLines(9);
                input.setGravity(Gravity.LEFT | Gravity.TOP);
                input.setHorizontalScrollBarEnabled(false);
                input.setText(sonstigesText);

                builder.setView(input);

                builder.setMessage(getResources().getString(R.string.a_ocr_manuell_hint_sonstige_infos))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sonstigesText = input.getText().toString();
                                sonstigesView.setText(sonstigesText);
                                bon.setOtherInformations(sonstigesText);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .create().show();
            }
        });

        //laden Spinner onClickListener
        ladenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parentView, final View selectedItemView, int position, long id) {

                // Itemid == 1 = Benutzerdefiniert, d.h. Wenn manuell eine Marke eingegeben werden soll
                if ((int) id == 1) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(A_OCR_Manuell.this);
                    final EditText input = new EditText(A_OCR_Manuell.this);

                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setMessage("Bitte Laden eingeben")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if(input.getText() != null && !input.getText().toString().isEmpty()){
                                        if(S.dbHandler.getLaden(S.db, input.getText().toString()) == null){
                                            S.dbHandler.addLaden(S.db, new C_Laden(input.getText().toString()));
                                        }
                                        refreshSpinner();

                                        for(int i = 0; i < parentView.getCount(); i++){
                                            if(parentView.getAdapter().getItem(i).equals(input.getText().toString())){
                                                parentView.setSelection(i);
                                                bon.setShopName(input.getText().toString());
                                                break;
                                            }
                                        }
                                    } else {
                                        parentView.setSelection(0);
                                        bon.setShopName("");
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    parentView.setSelection(0);
                                }
                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    parentView.setSelection(0);
                                }
                            })
                            .create().show();
                } else if ((int) id > 1){
                    bon.setShopName(parentView.getSelectedItem().toString());
                }
            }

            // Wenn nichts selektiert wurde
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                parentView.setSelection(0);
            }


        });


    }

    /**
     * Standard
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Standard
     * @param savedInstanceState Status
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * öffnet den Kalender
     * onClickHandler für dateTextView
     * @param view standard
     */
    public void setDate(View view) {
        this.showDialog(999);
    }

    /**
     * onClickHandler für den addArticleButton
     * @param v Standard
     */
    public void onAddNewClicked(View v) {
        this.inflateEditRow(null, null);
        v.setVisibility(View.GONE);
    }

    /**
     * onClickHandler für den artikel Löschen button (Mülleimer)
     * @param v Standard
     */
    public void onDeleteClicked(View v) {
        this.linearLayout.removeView((View) v.getParent());
        this.totalPrice.setText(String.format("%s", getFinalPrice()));
        this.addArticleButton.setVisibility(View.VISIBLE);

    }

    /**
     * TODO Bitte änderrn, da deprecated!
     * @param id Kalender ID
     * @return NULL
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    this.myDateListener, Integer.parseInt(this.year), Integer.parseInt(this.month), Integer.parseInt(this.day));
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
                    day = "" + getNumberWithZero(arg3);
                    month = "" + getNumberWithZero(arg2 + 1);
                    mYear = arg1;
                    year = getNumberWithZero(arg1);
                }
            };

    /**
     * Erzeugt eine Calendar Instanz
     * Weißt year, month, day einen aktuellen wert zu
     * Setzt über showDate die werte in die TextView
     */
    public void createCalendar(){
        this.calendar = Calendar.getInstance();
        this.year = "" + this.calendar.get(Calendar.YEAR);
        this.month = this.getNumberWithZero(calendar.get(Calendar.MONTH) +1);
        this.day = this.getNumberWithZero(calendar.get(Calendar.DAY_OF_MONTH));
        this.showDate(year, month, day);
    }

    /**
     * Setzt das Aktuelle Datum in die TextView
     * @param year Das ausgewählte Jahr
     * @param month Der ausgewählte Monat
     * @param day Der ausgewählte Tag
     */
    private void showDate(String year, String month, String day) {
        String sepa = ".";
        dateTextView.setText(day + sepa + month + sepa + year);
        dateTextView.setTextColor(Color.parseColor("#000000")); // Ändert die Farbe wenn Datum ausgewählt wird
    }

    /**
     * Gibt eine Zahl wenn sie kleiner 10 ist mit einer 0 davor aus
     * @param zahl Zahl die ggf mit einer 0 vorne zurück gegeben wird
     * @return Zahl mit ggf 0 vorne
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

        ArrayList<C_Laden> laeden = S.dbHandler.getAllLaeden(S.db);

        String array[] = new String[laeden.size()+2];
        array[0] = "Bitte Laden auswählen";
        array[1] = "Hinzufügen";

        int count = 2;

        for(C_Laden laden : laeden){
            array[count] = laden.getName();
            count++;
        }

        this.spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array);
        this.spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerAdapter.sort(new Comparator<String>(){
            public int compare(String laden1, String laden2){

                if(laden2.equals("Bitte Laden auswählen") && laden1.equals("Hinzufügen")){
                    return 1;
                } else if(laden1.equals("Bitte Laden auswählen") || laden1.equals("Hinzufügen")){
                    return -1;
                } else if (laden2.equals("Bitte Laden auswählen") || laden2.equals("Hinzufügen")){
                    return 1;
                }

                laden1 = laden1.toLowerCase();
                laden2 = laden2.toLowerCase();

                return laden1.compareTo(laden2);
            }
        });
        this.ladenSpinner.setAdapter(this.spinnerAdapter);
    }

    /**
     * Was passiert wenn das Bild ausgewählt wurde
     * @param requestCode Standard
     * @param resultCode Standard
     * @param data Bild daten
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            this.fillMaskOCR(picturePath);
        }
    }

    /**
     * Erzeugt eine neue Artikel Reihe
     * @param name Name des Artikels
     * @param preis Preis des Artikels WICHTIG Preis muss ein , enthalten!
     */
    private void inflateEditRow(String name, String preis) {

        // XML Instanziieren
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.box_ocr_manuell_listview, null);
        final ImageButton deleteAticleButton = (ImageButton) rowView
                .findViewById(R.id.ocr_manuell_button_delete_article);
        final ImageButton positiveNegativeButton = (ImageButton) rowView
                .findViewById(R.id.ocr_manuell_negativ_positiv_button);
        final EditText calculator = (EditText) rowView
                .findViewById(R.id.ocr_negativ_positiv_calculator_text);
        final EditText articleText = (EditText) rowView
                .findViewById(R.id.ocr_manuell_article_text);
        final EditText priceText = (EditText) rowView
                .findViewById(R.id.ocr_manuell_price_text);
        final EditText centText = (EditText) rowView
                .findViewById(R.id.ocr_manuell_cent_text);

        negPos = true;

        String preisArray[];

        // Wenn der Preis nicht leer ist dann setze ihn
        if (preis != null && !preis.isEmpty()){

            if(Double.parseDouble(preis.replace(",",".")) < 0){

                preis = preis.replace("-","");
                positiveNegativeButton.setImageResource(R.mipmap.ic_indeterminate_check_box_black_24dp);
                positiveNegativeButton.setColorFilter(Color.RED);
                calculator.setText("-");
                negPos = false;
                totalPrice.setText(String.format("%s", getFinalPrice()));
            }

            if(preis.contains(",")){
                preisArray = preis.split(",");
            } else if(preis.contains(".")){
                preisArray = preis.split("\\.");
            } else {
                preisArray = new String[2];
            }

            priceText.setText(preisArray[0]);
            centText.setText(preisArray[1]);
        }

        // Wenn der Name nicht leer ist dann setze ihn
        if (name != null && !name.isEmpty()) {
            articleText.setText(name);
        } else {
           this.mExclusiveEmptyView = rowView;
        }

        positiveNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(negPos) {
                    positiveNegativeButton.setImageResource(R.mipmap.ic_indeterminate_check_box_black_24dp);
                    positiveNegativeButton.setColorFilter(Color.RED);
                    calculator.setText("-");
                    negPos = false;
                    totalPrice.setText(String.format("%s", getFinalPrice()));

                } else {
                    positiveNegativeButton.setImageResource(R.mipmap.ic_add_box_black_24dp);
                    positiveNegativeButton.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.positiv));
                    calculator.setText("");
                    negPos = true;
                    totalPrice.setText(String.format("%s", getFinalPrice()));
                }
            }
        });

        // Artikel Text changeListener
        articleText.addTextChangedListener(new TextWatcher() {

            // Wenn der Text geändert wird
            @Override
            public void afterTextChanged(Editable s) {

                // Wenn der Text leer ist
                if (s.toString().isEmpty()) {

                    if (mExclusiveEmptyView != null
                            && mExclusiveEmptyView != rowView) {
                        linearLayout.removeView(mExclusiveEmptyView);
                    }
                    mExclusiveEmptyView = rowView;
                    addArticleButton.setVisibility(View.INVISIBLE);
                    addArticleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMenueIcon));

                // Wenn etwas eingegeben wurde
                } else {

                    if (mExclusiveEmptyView == rowView) {
                        mExclusiveEmptyView = null;
                    }

                    deleteAticleButton.setVisibility(View.VISIBLE);
                    if(priceText.getText() != null && !priceText.getText().toString().isEmpty()) {
                        addArticleButton.setVisibility(View.VISIBLE);
                        addArticleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMenueIcon));
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        // Preis Text changeListener
        priceText.addTextChangedListener(new TextWatcher() {

            // Wenn der Text geändert wird
            @Override
            public void afterTextChanged(Editable s) {

                int inputType = priceText.getInputType();

                // Wenn der Text leer ist
                if (s.toString().isEmpty()) {
                    addArticleButton.setVisibility(View.GONE);

                    if (mExclusiveEmptyView != null
                            && mExclusiveEmptyView != rowView) {
                        linearLayout.removeView(mExclusiveEmptyView);
                    }

                    totalPrice.setText(getFinalPrice());
                    mExclusiveEmptyView = rowView;

                // Wenn etwas eingegeben wurde
                } else {

                    if (mExclusiveEmptyView == rowView) {
                        mExclusiveEmptyView = null;
                    }

                    if(articleText.getText() != null && !articleText.getText().toString().isEmpty()) {
                        addArticleButton.setVisibility(View.VISIBLE);
                        addArticleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMenueIcon));
                    }

                    totalPrice.setText(getFinalPrice());
                    deleteAticleButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        // Preis Text changeListener
        centText.addTextChangedListener(new TextWatcher() {

            // Wenn der Text geändert wird
            @Override
            public void afterTextChanged(Editable s) {

                // Wenn der Text leer ist
                if (s.toString().isEmpty()) {
                    addArticleButton.setVisibility(View.GONE);

                    if (mExclusiveEmptyView != null
                            && mExclusiveEmptyView != rowView) {
                        linearLayout.removeView(mExclusiveEmptyView);
                    }
                    totalPrice.setText(getFinalPrice());
                    mExclusiveEmptyView = rowView;

                    // Wenn etwas eingegeben wurde
                } else {

                    if (mExclusiveEmptyView == rowView) {
                        mExclusiveEmptyView = null;
                    }

                    if(articleText.getText() != null && !articleText.getText().toString().isEmpty()) {
                        addArticleButton.setVisibility(View.VISIBLE);
                        addArticleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMenueIcon));
                    }

                    if(priceText.getText().toString().isEmpty()){
                        priceText.setText("0");
                    }

                    totalPrice.setText(getFinalPrice());
                    deleteAticleButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        this.linearLayout.addView(rowView, this.linearLayout.getChildCount() - 1); // Erzeugt eine neue Reihe

        if(this.setFocusOnLine){
            articleText.requestFocus(this.linearLayout.getChildCount() - 1); // Setzt den Focus auf die Zeile
        } else {
            this.setFocusOnLine = true;
        }

    }

    /**
     * Gibt alle Preise als String Array zurück
     * @return String Array mit allen Preisen
     */
    private String[] getAllPrices(){

        String[] arrayPrices = new String[linearLayout.getChildCount()];
        View view;
        EditText calculator, priceField, centField;

        for(int i = 0; i < linearLayout.getChildCount(); i++){
            view = linearLayout.getChildAt(i);
            calculator = (EditText) view.findViewById(R.id.ocr_negativ_positiv_calculator_text);
            priceField = (EditText) view.findViewById(R.id.ocr_manuell_price_text);
            centField = (EditText) view.findViewById(R.id.ocr_manuell_cent_text);

            if(priceField != null && !priceField.getText().toString().isEmpty()) {

                if(calculator != null && !calculator.getText().toString().isEmpty()){
                    arrayPrices[i] = calculator.getText().toString() + priceField.getText().toString();
                } else {
                    arrayPrices[i] = priceField.getText().toString();
                }

            } else {
                if(calculator != null && !calculator.getText().toString().isEmpty()){
                    arrayPrices[i] = calculator.getText().toString() + "0";
                } else {
                    arrayPrices[i] = "0";
                }
            }

            if(centField != null && !centField.getText().toString().isEmpty()){
                if(arrayPrices[i] != null) {
                    arrayPrices[i] += "." + centField.getText().toString();
                } else {
                    arrayPrices[i] = "." + centField.getText().toString();
                }
            } else {
                arrayPrices[i] += ".0";
            }
        }
        return arrayPrices;
    }

    /**
     * Summiert alle Preise und gibt die Summe als double zurück
     * @return Alle Preise summiert als double
     */
    private String getFinalPrice(){

        double finalPrice = 0;

        for(String wert : this.getAllPrices()){

            if(wert.contains(",")) {
                wert = wert.replaceAll(",", ".");
            }
            if(wert.length() == 1 && wert.charAt(0) == '-') {
                wert = "-0";
            }

            try{
              finalPrice += Double.parseDouble(wert);
            } catch (Exception e){
                finalPrice += Double.parseDouble(wert.substring(0, wert.length()-1));
            }
        }

        finalPrice = Math.round(finalPrice * 100) / 100.00;
        DecimalFormat df = new DecimalFormat("#0.00");

        return df.format(finalPrice);
    }

    /**
     * Befüllt alle Werte
     * @param path Pfad zum Bild
     * @param ladenName Ladenname
     * @param anschrift Anschrift
     * @param datum Datum
     * @param sonstiges Sonstiges
     * @param articles Array mit Articles
     */
    private void fillMask(String path, String ladenName, String anschrift, String datum, String sonstiges, ArrayList<C_Artikel> articles){

        if(path != null) {

            this.ocrImageView.setImageURI(null);
            this.ocrImageView.setImageBitmap(this.getBitmapFromPath(path));
            this.imageOCRUriString = path;
            this.ocrImageView.setClickable(true);
            this.kameraButton.setTextColor(Color.BLACK);
        }

        if(ladenName != null && !ladenName.isEmpty()){
            if(ladenName.equals("NOT SUPPORTED")){
                ladenSpinner.setSelection(0);
            } else {
                for(int i = 0; i < ladenSpinner.getCount(); i++){
                    if(ladenSpinner.getAdapter().getItem(i).toString().contains(ladenName)){
                        ladenSpinner.setSelection(i);
                        break;
                    }
                }
            }
        } else {
            ladenSpinner.setSelection(0);
        }

        if(anschrift != null && !anschrift.isEmpty()){
            this.anschriftInput.setText(anschrift);
        } else {
            this.anschriftInput.setText("");
            this.anschriftInput.setHint(this.getResources().getString(R.string.a_ocr_manuell_hint_anschrift));
        }

        if(datum != null && !datum.isEmpty()){
            this.dateTextView.setText(datum);
        } else {
            this.createCalendar();
        }

        if(sonstiges != null && !sonstiges.isEmpty()){
            this.sonstigesText = sonstiges;
            sonstigesView.setText(sonstigesText);
        } else {
            this.sonstigesView.setText(this.getResources().getString(R.string.a_ocr_manuell_hint_sonstiges));
            this.sonstigesText = "";
        }

        if(articles != null){

            this.removeAllArticles();

            for(C_Artikel article : articles){
                this.setFocusOnLine = false;
                this.inflateEditRow(article.getName(), ""+article.getPrice());
            }
            this.totalPrice.setText(getFinalPrice());
        } else {
            this.removeAllArticles();
            this.totalPrice.setText("0,00");
        }
    }

    /**
     * Versucht anhand eines Bitmaps über OCR die Maske zu befüllen!
     * @param path Pfad
     */
    private void fillMaskOCR(String path){

        boolean status = this.ocr.recognize(this.getBitmapFromPath(path));

        if(status){
            this.removeAllArticles();
            this.fillMask(path,
                    this.ocr.getLadenName(),
                    this.ocr.getAdresse(),
                    null,  // TODO Datum über OCR suchen!
                    this.ocr.getTel(),
                    this.ocr.getArticles());
        } else {
            this.removeAllArticles();

            if(this.ocr.getLadenName() != null && !this.ocr.getLadenName().isEmpty()){
                this.fillMask(path, this.ocr.getLadenName(), null, null, null, null);
            } else {
                this.fillMask(path, null, null, null, null, null);
            }
        }

    }

    /**
     * Prüft ob alle relevanten Felder befüllt wurden
     * Zeigt über die Rote Farbe an ob das Feld befüllt wurde oder nicht
     * @return true, alles wurde befüllt. false ein wert fehlt
     */
    public boolean checkAllRelevantValues(){

        boolean allRelevantFieldsFull = true;

        // Prüft ob ein Laden ausgewählt wurde
        if(this.ladenSpinner.getSelectedItemPosition() == 0){
            this.ladenSpinner.setSelection(0,true);
            View v = this.ladenSpinner.getSelectedView();
            ((TextView)v).setTextColor(Color.RED);
            allRelevantFieldsFull = false;
        } else {
            View v = this.ladenSpinner.getSelectedView();
            ((TextView)v).setTextColor(Color.BLACK);
        }

        // Prüft ob noch kein Artikel angegeben wurde
        if(this.linearLayout.getChildCount() == 2){
            this.addArticleButton.setTextColor(Color.RED);
            allRelevantFieldsFull = false;
        } else {
            EditText articleField, priceField;
            ImageButton deleteArticleButton;

            for(int i = 0; i < linearLayout.getChildCount(); i++){
                articleField = (EditText) linearLayout.getChildAt(i).findViewById(R.id.ocr_manuell_article_text);
                priceField = (EditText) linearLayout.getChildAt(i).findViewById(R.id.ocr_manuell_price_text);
                deleteArticleButton = (ImageButton) linearLayout.getChildAt(i).findViewById(R.id.ocr_manuell_button_delete_article);


                if(articleField == null || articleField.getText().toString().isEmpty() || articleField.getText().toString().equals("")) {
                    if(articleField instanceof EditText && deleteArticleButton instanceof ImageButton) {
                        articleField.setHintTextColor(Color.RED);
                        deleteArticleButton.setVisibility(View.VISIBLE);
                        allRelevantFieldsFull = false;
                    }
                }

                if(priceField == null || articleField.getText().toString().isEmpty() || priceField.getText().toString().equals("")) {
                    if(priceField instanceof EditText && deleteArticleButton instanceof ImageButton) {
                        priceField.setHintTextColor(Color.RED);
                        deleteArticleButton.setVisibility(View.VISIBLE);
                        allRelevantFieldsFull = false;
                    }
                }
            }
            this.addArticleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMenueIcon));
        }
        return allRelevantFieldsFull;
    }

    /**
     * Bekommt die Uri aus einem Bitmap zurück
     * @param inImage Bitmap
     * @return Uri des Bitmap
     */
    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * Gibt ein Bitmap bei Angabe der Uri zurück
     * @param uri Uri
     * @return Bitmap aus der Uri
     */
    public Bitmap getBitmapFromUri(Uri uri){
        try {
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            Log.e("### getBitmapFromUri", e.toString());
        }
        return null;
    }

    public Bitmap getBitmapFromPath(String path){

        File image = new File(path);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
    }

    /**
     * Prüft nochmal expliziet den Status und gibt diesen wieder
     * @return Status der Maske
     */
    public String getState(){

        String state = getIntent().getStringExtra("manuellState");

        switch(state){
            case "new": return "new";
            case "edit": return "edit";
            case "foto": return "foto";
            default: return "UNDEFINED";
        }
    }

    /**
     * Handle, jeh nach state
     * @param state Status der Maske
     */
    public void doState(String state){

        if (state.equals("edit")) { // Wenn die Maske den Status edit hat (z.B. ein Bon aufgerufen wird)
            int bonId = 0;
            Intent mIntent = getIntent();
            bonId = mIntent.getIntExtra("bonId", bonId);
            C_Bon bon = S.dbHandler.getBon(db, bonId);

            this.bon.setId(bonId);
            this.bon.setFavourite(bon.getFavourite());
            this.bon.setGuarantee(bon.getGuarantee());
            this.bon.setGuaranteeEnd(bon.getGuaranteeEnd());

            if(this.bon.getGuarantee()){
                garantieButton.setColorFilter(R.color.colorPrimary);
            }

            if(bon.getPath() != null && bon.getPath().contains(".")) {
                this.fillMask(bon.getPath(), bon.getShopName(), bon.getAdress(), bon.getDate(), bon.getOtherInformations(), bon.getArticles());
            } else {
                this.fillMask(null, bon.getShopName(), bon.getAdress(), bon.getDate(), bon.getOtherInformations(), bon.getArticles());
            }

        } else if (state.equals("foto")) { // Wenn die Maske den Status foto hat (z.B. wenn gerade ein Foto gemacht wurde)

            ArrayList<String> aList = getIntent().getStringArrayListExtra("ArrayList");
            File imgFile = new File(aList.get(aList.size()-1));

            if (imgFile.exists()) {
                this.fillMaskOCR(imgFile.getAbsolutePath());
            }
        } else if (state.equals("new")) { // Wenn die Maske den Status new hat (z.B. bei einer neuen Maske)
            return;
        }
    }

    /**
     * Löscht alle Artikel aus dem Kassenzettel
     */
    public void removeAllArticles(){

        View view;
        ArrayList<View> foundViews = new ArrayList<>();

        for(int i = 0; i < linearLayout.getChildCount(); i++){
            view = linearLayout.getChildAt(i);
            if(view.findViewById(R.id.ocr_manuell_article_line) != null){
                foundViews.add(view);
            }
        }

        for(View foundView : foundViews){
            linearLayout.removeView(foundView);
        }
    }

    /**
     * Gibt alle Artikel zurück die in die Maske eingetragen wurden
     * @return ArrayList mit allen Artikeln aus der Maske
     */
    public ArrayList<C_Artikel> getAllArticle(){

        EditText articleField, priceField, centField, calculator;
        ImageButton deleteArticleButton;
        ArrayList<C_Artikel> articles = new ArrayList<>();

        for(int i = 0; i < linearLayout.getChildCount(); i++){
            articleField = (EditText) linearLayout.getChildAt(i).findViewById(R.id.ocr_manuell_article_text);
            priceField = (EditText) linearLayout.getChildAt(i).findViewById(R.id.ocr_manuell_price_text);
            centField = (EditText) linearLayout.getChildAt(i).findViewById(R.id.ocr_manuell_cent_text);
            calculator = (EditText) linearLayout.getChildAt(i).findViewById(R.id.ocr_negativ_positiv_calculator_text);
            deleteArticleButton = (ImageButton) linearLayout.getChildAt(i).findViewById(R.id.ocr_manuell_button_delete_article);

            if(calculator instanceof EditText &&
                    articleField instanceof  EditText &&
                    priceField instanceof  EditText &&
                    centField instanceof EditText &&
                    deleteArticleButton instanceof ImageButton){

                double price = 0;

                if(!calculator.getText().toString().isEmpty()){
                    price = Double.parseDouble("-" + priceField.getText().toString() + "." + centField.getText().toString());
                } else {
                    price = Double.parseDouble(priceField.getText().toString() + "." + centField.getText().toString());
                }

                articles.add(new C_Artikel(articleField.getText().toString(), price));
            }
        }
        return articles;
    }

    /**
     * Baut einen Bon der abgespeichert werden kann
     * @return Bon mit allen werten aus der Maske
     */
    public C_Bon saveBon(){

        String guaranteeEnd = "NA";
        C_Bon saveBon;

        if(this.bon.getGuarantee()){
            guaranteeEnd = this.dateTextView.getText().toString().split("\\.")[0] + "." +
                    this.dateTextView.getText().toString().split("\\.")[1] + "." +
                    (Integer.parseInt(this.dateTextView.getText().toString().split("\\.")[2]) + valuePicked);
        }

        if(getState().equals("edit")){

            if(garantieChanged){
                bon.setGuaranteeEnd(guaranteeEnd);
            }

            saveBon = new C_Bon(this.bon.getId(),
                    this.imageOCRUriString,
                    this.bon.getShopName(),
                    this.anschriftInput.getText().toString(),
                    this.sonstigesText,
                    this.dateTextView.getText().toString(),
                    this.bon.getGuaranteeEnd(),
                    this.totalPrice.getText().toString(),
                    this.bon.getFavourite(),
                    this.bon.getGuarantee(),
                    this.getAllArticle());
        } else {
            saveBon = new C_Bon(this.imageOCRUriString,
                    this.bon.getShopName(),
                    this.anschriftInput.getText().toString(),
                    this.sonstigesText,
                    this.dateTextView.getText().toString(),
                    guaranteeEnd,
                    this.totalPrice.getText().toString(),
                    false,
                    this.bon.getGuarantee(),
                    this.getAllArticle());
        }

        Log.e("BON", saveBon.toString());

        return saveBon;
    }
}