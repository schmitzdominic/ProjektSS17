
package de.projektss17.bonpix;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.projektss17.bonpix.daten.C_Budget;
import de.projektss17.bonpix.daten.C_Budget_CardView_Adapter;


public class A_Budget extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<C_Budget> budgetList = new ArrayList<>();
    private RecyclerView.Adapter bAdapter;
    private FloatingActionButton fab;
    private ItemTouchHelper swipper;


    /** Content für diese Activity wird erstellt / gebaut / vorbereitet
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_budget_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // LAYOUT - Implementierung aller Layouts
        recyclerView = (RecyclerView) findViewById(R.id.view_budget); // Recycler Liste
        bAdapter = new C_Budget_CardView_Adapter(budgetList);          // CardView


        // FAB - Drücken öffnet ein Dialog mit Eingabefeldern
        this.fab = (FloatingActionButton) findViewById(R.id.budget_fab);    //Floating Action Button
        fab.setOnClickListener(new View.OnClickListener() {

            /**
             * Funktion die beim drücken des FAB ausgeführt wird
             * @param v Übergabe einer view - In diesem Fall die View für die Card View
             */
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(A_Budget.this, A_Budget_Edit.class);
                startActivity(intent);
                /*
                // Implementierung des Layouts für den Dialog-Fenster
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.box_budget_alert_dialog, null);
                final EditText titleContent = (EditText) alertLayout.findViewById(R.id.budget_alert_dialog_title);
                final EditText budgetContent = (EditText)alertLayout.findViewById(R.id.budget_alert_dialog_betrag);
                final EditText yearContent = (EditText) alertLayout.findViewById(R.id.budget_alert_dialog_jahr);
                final EditText monthContent = (EditText) alertLayout.findViewById(R.id.budget_alert_dialog_monat);


                // DIALOG POPUP - Dialog Fenstern mit 4 Eingabefeldern wird erstellt
                new AlertDialog.Builder(A_Budget.this)
                    .setTitle("Budget")     // Layout für PopUp-Fenster wird gesetzt (siehe layout-XML)
                    .setView(alertLayout)
                    .setCancelable(false)
                    .setNegativeButton("Abbruch", null)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // CARD VIEW wird befüllt wenn EditTexts alle befült wurden - ansonsten Abbruch des AlertDialog
                        if(budgetContent.getText()== null || budgetContent.getText().toString().isEmpty())
                            dialog.dismiss();
                        else if(titleContent.getText()== null || titleContent.getText().toString().isEmpty())
                            dialog.dismiss();
                        else if(yearContent.getText()== null || yearContent.getText().toString().isEmpty())
                            dialog.dismiss();
                        else if(monthContent.getText()== null || monthContent.getText().toString().isEmpty())
                            dialog.dismiss();
                        else
                            addItem(budgetContent.getText().toString(),
                                budgetContent.getText().toString(),
                                monthContent.getText().toString(),
                                titleContent.getText().toString(),
                                yearContent.getText().toString());
                    }

                }).create().show(); */
            }
        });

        // SWIPPER - Implementierung des Swipper-Funktion
        swipper = new ItemTouchHelper(createHelperCallBack());      // ItemTouch -> Swipper
        swipper.attachToRecyclerView(recyclerView);

    }


    /**
     * Created by Johanns am 30.04.2017
     *
     * CREATE HELPER CALLBACK - Funktion zum Swippen (bewegen od. löschen von Items durch swippen)
     * @return ItemTouchHelper (Swipper-Funktion) wird zurückgegeben
     */
    private ItemTouchHelper.Callback createHelperCallBack(){

        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {


            @Override
            public  boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                   RecyclerView.ViewHolder target) {

                // CARD VIEW - Items können bewegt werden (oben nach unten und umgekehrt)
                moveItem(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return true;    // true wenn geswipped wird, ansonsten false
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {

                // CARD VIEW - Items werden gelöscht (nach Rechts oder Links)
                deleteItem(viewHolder.getAdapterPosition());
                S.outShort(A_Budget.this,"Item wurde gelöscht!");

            }
        };
    }

    /**
     * MOVE ITEM - Bewegen eines Items in der RecyclerView (Swip nach Oben oder Unten)
     * @param oldPos Alte Postion der View wird übergeben
     * @param newPos Neue Postition der View wird übergeben
     */
    private void moveItem(int oldPos, int newPos){

        C_Budget item = (C_Budget)budgetList.get(oldPos);
        budgetList.get(oldPos);
        budgetList.remove(oldPos);
        budgetList.add(newPos,item);
        bAdapter.notifyItemMoved(oldPos,newPos);

    }


    /**
     * DELETE ITEM - Löschen eines Items in der RecyclerView (Swip nach Links)
     * @param position Position der jeweiligen View wird übergeben
     */
    private void deleteItem(int position){

        /*
        *  >>>> Hier DELETE-Verbindung zur DB herstellen (wenn DB fertig)
        *  >>>> Für's erste dient der untere Code
        *
        * */

        budgetList.remove(position);
        bAdapter.notifyItemRemoved(position);
    }


    /**
     * ADD ITEM - Befüllung der RecyclerView mit EINER Datenmenge
     * @param budgetMax Übergabe des eingegeben Budget-Betrags
     * @param budgetCurrently Übergabe des budgetMax -> Ändert sich wenn DB angehängt wird
     * @param monatVon Übergabe des Monat
     * @param jahrVon Übergabe des Jahres
     * @param monatBis Übergabe des Monat
     * @param jahrBis Übergabe des Jahres
     * @param title Übergabe des Titels
     */
    private void addItem(String budgetMax, String budgetCurrently, String monatVon, String jahrVon, String monatBis, String jahrBis, String title){

        /*
        *  >>>> Hier INSERT-Verbindung zur DB herstellen (wenn DB fertig)
        *  >>>> Für's erste dient der untere Code
        *
        * */

        int budgetMaxParse = Integer.parseInt(budgetMax);
        int budgetCurrentlyParse = Integer.parseInt(budgetCurrently); // Betrag wird beim ersten mal das selbe wie budgetMaxParse sein
        int testcurrentBudget = randomNumber(budgetMaxParse,150); // Betrag zum Testen -> wird später gelöscht!!!!!!

        C_Budget budget = new C_Budget(budgetMaxParse,testcurrentBudget,
                percentageCalculator(budgetMaxParse,testcurrentBudget), monatVon,jahrVon,monatBis,jahrBis,title);

        budgetList.add(budget);

        layoutManager = new LinearLayoutManager(A_Budget.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bAdapter);
        bAdapter.notifyDataSetChanged();

    }


    /**
     * RANDOM NUMBER - Zufällige Auswahl eines Int zwischen Max und Min
     * @param max Übergabe der Obergrenze
     * @param min Übergabe der Untergrenze
     * @return Rückgabe einer Random Zahl zwischen Max & Min
     */
    public int randomNumber(int max, int min){

        Random rand = new Random();
        return rand.nextInt((max - min) + min);

    }


    /**
     * RANDOM TITLE - Zufällige Auswahl eines Titels
     * @param random Übergabe einer Randomzahl
     * @return Rückgabe eines Titels je nach Randomzahl
     */
    public String randomTitle(int random){

        String randomTitle = "";

        switch (random){
            case 0: randomTitle = "Lebensmittel";
                break;
            case 1: randomTitle = "Elektronik";
                break;
            case 2: randomTitle =  "Haushaltsware";
                break;
            case 3: randomTitle = "Putzmittel & Andere";
                break;
            case 4: randomTitle = "Getränke";
                break;
            case 5: randomTitle = "Sonstiges";
                break;
        }

        return randomTitle;
    }


    /**
     * RANDOM MONTH - Zufällige Auswahl eines Monats
     * @param random Übergabe einer Randomzahl
     * @return Rückgabe eines Monats je nach Randomzahl
     */
    public String randomMonth (int random){

        String randomMonth = "";

        switch (random){
            case 0: randomMonth = "Januar";
                break;
            case 1: randomMonth = "Februar";
                break;
            case 2: randomMonth = "März";
                break;
            case 3: randomMonth = "April";
                break;
            case 4: randomMonth = "Mai";
                break;
            case 5: randomMonth = "Juni";
                break;
            case 6: randomMonth = "Juli";
                break;
            case 7: randomMonth = "August";
                break;
            case 8: randomMonth = "September";
                break;
            case 9: randomMonth = "Oktober";
                break;
            case 10: randomMonth = "November";
                break;
            case 11: randomMonth = "Dezember";
                break;
        }

        return randomMonth;
    }


    /**
     * CALCULATE PERCANTAGE - Berechnen des Prozentsatzes
     * @param max Übergabe einer Obergrenze
     * @param currently Übergabe eines gegenwärtigen Betrags
     * @return Rückgabe des Prozentsatzes von Currently zu Max
     */
    private int percentageCalculator(int max, int currently){
        return (int)(currently*100/max);
    }

}