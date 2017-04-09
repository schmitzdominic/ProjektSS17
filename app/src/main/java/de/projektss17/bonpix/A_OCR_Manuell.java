package de.projektss17.bonpix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class A_OCR_Manuell extends AppCompatActivity {

    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_ocr_manuell_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Refernzieren des Speichern-Button
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

    }
}
