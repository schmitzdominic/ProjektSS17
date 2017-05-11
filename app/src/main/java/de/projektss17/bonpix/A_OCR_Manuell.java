package de.projektss17.bonpix;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import de.projektss17.bonpix.daten.C_Laden;
import de.projektss17.bonpix.recognition.C_OCR;
import de.projektss17.bonpix.recognition.C_PicChanger;


public class A_OCR_Manuell extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private String year, month, day, imageOCRUriString, sonstigesText;
    private boolean setFocusOnLine = true, negPos;
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
    private C_OCR ocr;


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

        this.ocr = new C_OCR(this); // Erstellt eine OCR instanz.
        this.doState(this.getState()); // Überprüft den Status und befüllt ggf.
        this.createCalendar(); // Calendar wird befüllt
        this.refreshSpinner(); // Spinner Refresh
        this.ocrImageView.setClickable(false); // Icon ist am anfang nicht klickbar


        // Garantie Button onClickListener
        this.garantieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bonGarantie==false) {
                    bonGarantie = true;
                    garantieButton.setColorFilter(R.color.colorPrimary);

                    // PopUp Info Fenster wird geöffnet und automatisch geschlossen
                    S.outShort(A_OCR_Manuell.this, "Garantie wurde hinzugefügt!");
                }else{
                    bonGarantie = false;
                    garantieButton.setColorFilter(Color.WHITE);

                    // PopUp Info Fenster wird geöffnet und automatisch geschlossen
                    S.outShort(A_OCR_Manuell.this, "Garantie wurde entfernt!");
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
                                                break;
                                            }
                                        }
                                    } else {
                                        parentView.setSelection(0);
                                    }
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
            this.fillMaskOCR(this.getBitmapFromUri(data.getData()));
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
                negPos = false;
                positiveNegativeButton.performClick();
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

        //R.mipmap.ic_indeterminate_check_box_black_24dp

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
     * @param imageUri Uri zum Bild
     * @param ladenName Ladenname
     * @param anschrift Anschrift
     * @param datum Datum
     * @param sonstiges Sonstiges
     * @param articles Array mit Articles
     */
    private void fillMask(Uri imageUri, String ladenName, String anschrift, String datum, String sonstiges, ArrayList<C_Artikel> articles){

        if(imageUri != null) {
            this.ocrImageView.setImageURI(null);
            this.ocrImageView.setImageURI(imageUri);
            this.imageOCRUriString = imageUri.toString();
            this.ocrImageView.setClickable(true);
            this.kameraButton.setTextColor(Color.BLACK);
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
            sonstigesView.setText(sonstigesText);
        }

        if(articles != null){

            this.removeAllArticles();

            for(C_Artikel article : articles){
                this.setFocusOnLine = false;
                this.inflateEditRow(article.getName(), ""+article.getPrice());
            }
            this.totalPrice.setText(getFinalPrice());
        }
    }

    /**
     * Versucht anhand eines Bitmaps über OCR die Maske zu befüllen!
     * @param myBitmap Bitmap
     */
    private void fillMaskOCR(Bitmap myBitmap){

        this.ocr.recognize(myBitmap);

        //myBitmap = picChanger.convertBitmapBlackAndWhite(myBitmap);

        //Bitmap changedBitmap = picChanger.convertBitmapGrayscale(picChanger.getOnlyPrices(myBitmap, this.ocr.getPointList()));

        //changedBitmap = picChanger.cutBitmapHorizontal(changedBitmap, (changedBitmap.getWidth()/3)*2)[0];

        /*
        this.ocr.recognize(changedBitmap);
        Log.e("######## ARTIKEL-ZEILEN", ocr.getPreise().size() + "");
        int preisCount = ocr.getPreise().size();

        ArrayList<Integer> x,y = new ArrayList<>();

        x = picChanger.countBlackPixels(picChanger.getColumArray(changedBitmap,1).get(changedBitmap.getWidth()/3));
        if(x.size() == 0){
            x = picChanger.countBlackPixels(picChanger.getColumArray(changedBitmap,1).get(changedBitmap.getWidth()/6));
        }

        /*y = picChanger.countBlackPixels(picChanger.getColumArray(changedBitmap,1).get((changedBitmap.getWidth()/3)+10));
        if(y.size() == 0){
            y = picChanger.countBlackPixels(picChanger.getColumArray(changedBitmap,1).get((changedBitmap.getWidth()/4)+10));
        }*/
        /*

        int min = (int)(double)(preisCount*0.8);
        int max = (int)(double)(preisCount*1.2);

        int schnitt = 0;

        int count = 0;
        for(int black : x){
            count += black;
        }

        if(x.size() >= min && x.size() <= max){
            schnitt = count / preisCount;
        } else {
            schnitt = count / x.size();
        }

        Log.e("###### SCHNITT ENDE", schnitt + "");
        */
        //myBitmap = picChanger.getLines(changedBitmap, x).get(3);




        //ArrayList<Integer> y = picChanger.countBlackPixels(picChanger.getColumArray(changedBitmap,1).get((changedBitmap.getWidth()/5)));

        //changedBitmap = picChanger.getLines(changedBitmap, x).get(3);

        //myBitmap = changedBitmap;

        //myBitmap = picChanger.getOnlyPrices(myBitmap, this.ocr.getPointList());

        this.fillMask(this.getImageUri(myBitmap),
                this.ocr.getLadenName(),
                null, // TODO LadenName über OCR suchen!
                null,  // TODO Anschrift über OCR suchen!
                this.ocr.getRecognizedText(), // TODO Später wieder ausnehmen!
                this.ocr.getArticles());
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
            // TODO Anhand der Datenbank implementieren

        } else if (state.equals("foto")) { // Wenn die Maske den Status foto hat (z.B. wenn gerade ein Foto gemacht wurde)

            ArrayList<String> aList = getIntent().getStringArrayListExtra("ArrayList");
            File imgFile = new File(aList.get(aList.size()-1));

            if (imgFile.exists()) {
                this.fillMaskOCR(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
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
}
