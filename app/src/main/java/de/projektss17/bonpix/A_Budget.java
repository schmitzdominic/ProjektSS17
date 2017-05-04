package de.projektss17.bonpix;

import android.content.DialogInterface;
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


    /**
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
        //------------------------------------------------------------
        recyclerView = (RecyclerView) findViewById(R.id.view_budget); // Recycler Liste
        bAdapter = new C_Budget_CardView_Adapter(budgetList);          // CardView


        // FAB - Drücken öffnet ein Dialog mit Eingabefeldern
        //---------------------------------------------------
        this.fab = (FloatingActionButton) findViewById(R.id.budget_fab);    //Floating Action Button
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Implementierung des Layouts für den Dialog-Fenster
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.box_budget_alert_dialog, null);
                final EditText titleContent = (EditText) alertLayout.findViewById(R.id.budget_alert_dialog_title);
                final EditText budgetContent = (EditText) alertLayout.findViewById(R.id.budget_alert_dialog_betrag);
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

                        // CARD VIEW wird befüllt wenn auf OK gedrückt wird
                        addItem(budgetContent.getText().toString(),
                                budgetContent.getText().toString(),
                                monthContent.getText().toString(),
                                titleContent.getText().toString(),
                                yearContent.getText().toString());
                    }

                }).create().show();
            }
        });


        // SWIPPER - Implementierung des Swipper-Funktion
        //------------------------------------------------------------
        swipper = new ItemTouchHelper(createHelperCallBack());      // ItemTouch -> Swipper
        swipper.attachToRecyclerView(recyclerView);

    }



    /**
     *  SWIPPER -> Created by Johanns on 02.05.2017.
     */
    //-------------------------------------------------------------------------------------------
    // ---------------------------------- ITEM SWIPPER BEGINN -----------------------------------

    // CREATE HELPER CALLBACK - Funktion zum Swippen (bewegen od. löschen von Items durch swippen)
    private ItemTouchHelper.Callback createHelperCallBack(){

        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

                    @Override
                    public  boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

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


    // MOVE ITEM - Bewegen eines Items in der RecyclerView (Swip nach Oben oder Unten)
    private void moveItem(int oldPos, int newPos){

        C_Budget item = (C_Budget)budgetList.get(oldPos);
        budgetList.get(oldPos);
        budgetList.remove(oldPos);
        budgetList.add(newPos,item);
        bAdapter.notifyItemMoved(oldPos,newPos);

    }


    // DELETE ITEM - Löschen eines Items in der RecyclerView (Swip nach Links)
    private void deleteItem(int position){

        /*
        *  >>>> Hier DELETE-Verbindung zur DB herstellen (wenn DB fertig)
        *  >>>> Für's erste dient der untere Code
        *
        * */

        budgetList.remove(position);
        bAdapter.notifyItemRemoved(position);
    }


    // ADD ITEM - Befüllung der RecyclerView mit EINER Datenmenge
    private void addItem(String budgetMax, String budgetCurrently, String turnus, String title, String year){

        /*
        *  >>>> Hier INSERT-Verbindung zur DB herstellen (wenn DB fertig)
        *  >>>> Für's erste dient der untere Code
        *
        * */

        int budgetMaxParse = Integer.parseInt(budgetMax);
        int budgetCurrentlyParse = Integer.parseInt(budgetCurrently); // Betrag wird beim ersten mal das selbe wie budgetMaxParse sein
        int testcurrentBudget = randomNumber(budgetMaxParse,150); // Betrag zum Testen -> wird später gelöscht!!!!!!

        C_Budget budget = new C_Budget(budgetMaxParse,testcurrentBudget,
                percentageCalculator(budgetMaxParse,testcurrentBudget), turnus, title,year);

        budgetList.add(budget);

        layoutManager = new LinearLayoutManager(A_Budget.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bAdapter);
        bAdapter.notifyDataSetChanged();

    }
    // ----------------------------------ITEM SWIPPER ENDE --------------------------------------
    //-------------------------------------------------------------------------------------------



    /**
     *  TESTDATEN -> Created by Johanns on 30.04.2017.
     */
    // -------------------------------------------------------------------------------------------
    // ------------------------------------- TESTDATEN BEGINN ------------------------------------

    // RANDOM NUMBER - Zufällige Auswahl eines Int zwischen Max und Min
    public int randomNumber(int max, int min){

        Random rand = new Random();
        return rand.nextInt((max - min) + min);

    }


    // RANDOM TITLE - Zufällige Auswahl eines Titels
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


    // RANDOM MONTH - Zufällige Auswahl eines Monats
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


    // CALCULATE PERCANTAGE - Berechnen des Prozentsatzes
    private int percentageCalculator(int max, int currently){
        return (int)(currently*100/max);
    }

    // ------------------------------------ TESTDATEN ENDE ---------------------------------------
    // -------------------------------------------------------------------------------------------
}