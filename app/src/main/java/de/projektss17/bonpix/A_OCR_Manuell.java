package de.projektss17.bonpix;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import de.projektss17.bonpix.daten.C_Article;


public class A_OCR_Manuell extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private String year, month, day, imageOCRUriString, sonstigesText;
    private ArrayAdapter<String> spinnerAdapter;
    private Button  kameraButton, addArticleButton;
    private Spinner ladenSpinner;
    private Calendar calendar;
    private TextView dateTextView, totalPrice, sonstigesView;
    private ImageView ocrImageView;
    private View mExclusiveEmptyView;
    private EditText anschriftInput;
    private LinearLayout linearLayout;
    private ImageButton garantieButton, saveButton;
    private boolean bonGarantie = false;


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

        this.createCalendar(); // Calendar wird befüllt
        this.refreshSpinner(); // Spinner Refresh
        this.ocrImageView.setClickable(false); // Icon ist am anfang nicht klickbar


        // Garantie Button onClickListener
        this.garantieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bonGarantie==false) {
                    bonGarantie = true;
                    S.popUpDialog(A_OCR_Manuell.this, A_OCR_Manuell.class,
                            R.string.a_ocr_manuell_pop_up_title_garantie_on,
                            R.string.a_ocr_manuell_pop_up_message_garantie_on,
                            R.string.a_ocr_manuell_pop_up_cancel_garantie_on,
                            R.string.a_ocr_manuell_pop_up_confirm_garantie_on);
                }else{
                    bonGarantie = false;
                    S.popUpDialog(A_OCR_Manuell.this, A_OCR_Manuell.class,
                            R.string.a_ocr_manuell_pop_up_title_garantie_off,
                            R.string.a_ocr_manuell_pop_up_message_garantie_off,
                            R.string.a_ocr_manuell_pop_up_cancel_garantie_off,
                            R.string.a_ocr_manuell_pop_up_confirm_garantie_off);
                }
            }
        });


        // Save Button onClickListener
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkAllRelevantValues()) {
                    // Aufruf der Static-Methode popUpDialog(), welches ein Hinweis-Fenster öffnet
                    S.popUpDialog(A_OCR_Manuell.this, A_Main.class,
                            R.string.a_ocr_manuell_pop_up_title,
                            R.string.a_ocr_manuell_pop_up_message,
                            R.string.a_ocr_manuell_pop_up_cancel,
                            R.string.a_ocr_manuell_pop_up_confirm);
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
            public void onItemSelected(final AdapterView<?> parentView, View selectedItemView, int position, long id) {

                // Itemid == 1 = Benutzerdefiniert, d.h. Wenn manuell eine Marke eingegeben werden soll
                if ((int) id == 1) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(A_OCR_Manuell.this);
                    final EditText input = new EditText(A_OCR_Manuell.this);

                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setMessage("Bitte Laden eingeben")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    /*
                                    TODO Bitte so implementieren:
                                    S.dbHandler.setLaeden(S.db, input.getText().toString());
                                    ArrayList<String> x = S.dbHandler.getAllLaeden(S.db);
                                    refreshSpinner();
                                    parentView.setSelection(x.indexOf(input.getText().toString())+2);


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
        this.month = "" + this.getNumberWithZero(calendar.get(Calendar.MONTH) + 1);
        this.day = "" + this.getNumberWithZero(calendar.get(Calendar.DAY_OF_MONTH));
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

        // 1. lese Optionen aus Datenbank
        // Mit z.B. S.getSpinnerLaedenManuell() in eine entsprechende Liste
        // 2. füge diese Daten zu einem String Array hinzu
        // TODO Remove next line after database has implmented
        String array[] = {"Bitte Laden auswählen","Hinzufügen", "EDEKA", "REWE", "MEDIA MARKT"};

        /* TODO mit Datenbank bitte so implementieren!
        ArrayList<String> x = S.dbHandler.getAllLaeden(S.db);

        String array[] = new String[x.size()+2];

        int count = 2;

        array[0] = "Bitte Laden auswählen";
        array[1] = "Hinzufügen";

        for(String a : x){
            array[count] = a;
            count ++;
        } */

        this.spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array);
        this.spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
            Uri imageUri = data.getData();
            this.ocrImageView.setImageURI(null);
            this.ocrImageView.setImageURI(imageUri);
            this.imageOCRUriString = imageUri.toString();
            this.ocrImageView.setClickable(true);
            this.kameraButton.setTextColor(Color.BLACK);
        }
    }

    /**
     * Erzeugt eine neue Artikel Reihe
     * @param name Name des Artikels
     * @param preis Preis des Artikels
     */
    private void inflateEditRow(String name, String preis) {

        // XML Instanziieren
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.box_ocr_manuell_listview, null);
        final ImageButton deleteAticleButton = (ImageButton) rowView
                .findViewById(R.id.ocr_manuell_button_delete_article);
        final EditText articleText = (EditText) rowView
                .findViewById(R.id.ocr_manuell_article_text);
        final EditText priceText = (EditText) rowView
                .findViewById(R.id.ocr_manuell_price_text);

        // Wenn der Preis nicht leer ist dann setze ihn
        if (preis != null && !preis.isEmpty()){
            priceText.setText(preis);
        }

        // Wenn der Name nicht leer ist dann setze ihn
        if (name != null && !name.isEmpty()) {
            articleText.setText(name);
        } else {
           this.mExclusiveEmptyView = rowView;
        }

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
                    deleteAticleButton.setVisibility(View.INVISIBLE);

                    if (mExclusiveEmptyView != null
                            && mExclusiveEmptyView != rowView) {
                        linearLayout.removeView(mExclusiveEmptyView);
                    }
                    priceText.setKeyListener(DigitsKeyListener.getInstance("0123456789-"));
                    totalPrice.setText(String.format("%s", getFinalPrice()));
                    mExclusiveEmptyView = rowView;

                // Wenn etwas eingegeben wurde
                } else {

                    if (mExclusiveEmptyView == rowView) {
                        mExclusiveEmptyView = null;
                    }

                    // Wenn die Eingabe ein - ist, dann sperre das Komma danach.
                    if(priceText.getText().toString().length() == 1 && priceText.getText().charAt(0) == '-'){
                        priceText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                    } else {
                        priceText.setKeyListener(DigitsKeyListener.getInstance("0123456789,"));
                    }

                    // Wenn der Text ein Komma enthält
                    if(priceText.getText().toString().contains(",")){

                        // Deaktiviere das Komma
                        priceText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

                        // Splitte den String beim Komma
                        String[] array = priceText.getText().toString().split(",");
                        if(array.length == 2){ // Array muss mind 2 Werte haben (1 vor dem Komma, 1 Nach dem Komma)
                            if(array[1].length() == 2){ // Wenn 2 Stellen nach dem Komma vorhanden sind, sperre die Tastatur
                                priceText.setKeyListener(DigitsKeyListener.getInstance(""));
                            } else {
                                priceText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                            }
                        }
                    }

                    totalPrice.setText(String.format("%s", getFinalPrice()));
                    if(articleText.getText() != null && !articleText.getText().toString().isEmpty()) {
                        addArticleButton.setVisibility(View.VISIBLE);
                        addArticleButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMenueIcon));
                    }
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
        articleText.requestFocus(this.linearLayout.getChildCount() - 1); // Setzt den Focus auf die Zeile
    }

    /**
     * Gibt alle Preise als String Array zurück
     * @return String Array mit allen Preisen
     */
    private String[] getAllPrices(){

        String[] arrayPrices = new String[linearLayout.getChildCount()];
        View view;
        EditText textField;

        for(int i = 0; i < linearLayout.getChildCount(); i++){
            view = linearLayout.getChildAt(i);
            textField = (EditText) view.findViewById(R.id.ocr_manuell_price_text);
            if(textField != null && !textField.getText().toString().isEmpty())
                arrayPrices[i] = textField.getText().toString();
            else
                arrayPrices[i] = "0.00";
        }

        return arrayPrices;
    }

    /**
     * Gibt alle Artikel als String Array zurück
     * @return String Array mit allen Artikeln
     */
    private String[] getAllArticles(){
        String[] arrayArticles = new String[linearLayout.getChildCount()];
        View view;
        EditText textField;

        for(int i = 0; i < linearLayout.getChildCount(); i++){
            view = linearLayout.getChildAt(i);
            textField = (EditText) view.findViewById(R.id.ocr_manuell_article_text);
            if(textField != null && !textField.getText().toString().isEmpty())
                arrayArticles[i] = textField.getText().toString();
            else
                arrayArticles[i] = "";
        }

        return arrayArticles;
    }

    /**
     * Summiert alle Preise und gibt die Summe als double zurück
     * @return Alle Preise summiert als double
     */
    private double getFinalPrice(){

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

        return finalPrice;
    }

    /**
     * Befüllt alle Werte
     * @param imageUri Uri zum Bild
     * @param ladenName Ladenname
     * @param anschrift Anschrift
     * @param datum Datum
     * @param sonstiges Sonstiges
     * @param articles Array mit Articles
     */
    private void fillMask(Uri imageUri, String ladenName, String anschrift, String datum, String sonstiges, C_Article[] articles){


        if(imageUri != null) {
            this.ocrImageView.setImageURI(null);
            this.ocrImageView.setImageURI(imageUri);
            this.imageOCRUriString = imageUri.toString();
            this.ocrImageView.setClickable(true);
        }

        if(ladenName != null && !ladenName.isEmpty()){
            // TODO Datenbank anbindung und dann refresh spinner
        }

        if(anschrift != null && !anschrift.isEmpty()){
            this.anschriftInput.setText(anschrift);
        }

        if(datum != null && !datum.isEmpty()){
            this.dateTextView.setText(datum);
        }

        if(sonstiges != null && !sonstiges.isEmpty()){
            this.sonstigesText = sonstiges;
        }

        if(articles != null){
            for(C_Article article : articles){
                this.inflateEditRow(article.getName(), article.getPrice());
            }
            this.totalPrice.setText(String.format("%s", getFinalPrice()));
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
}
