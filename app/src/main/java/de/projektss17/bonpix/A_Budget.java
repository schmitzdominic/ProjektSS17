package de.projektss17.bonpix;

import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ListMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.projektss17.bonpix.daten.C_Budget;
import de.projektss17.bonpix.daten.C_Budget_CardView_Adapter;

import static android.text.InputType.*;


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


        // FAB - Drücken fügt ein Item (CardView) hinzu
        //------------------------------------------------------------

         String m_Text;

        this.fab = (FloatingActionButton) findViewById(R.id.budget_fab);    //Floating Action Button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.box_budget_alert_dialog, null);
                final EditText titleContent = (EditText) alertLayout.findViewById(R.id.budget_alert_dialog_title);
                final EditText budgetContent = (EditText) alertLayout.findViewById(R.id.budget_alert_dialog_betrag);
                final EditText yearContent = (EditText) alertLayout.findViewById(R.id.budget_alert_dialog_jahr);
                final EditText monthContent = (EditText) alertLayout.findViewById(R.id.budget_alert_dialog_monat);

                AlertDialog.Builder alert = new AlertDialog.Builder(A_Budget.this);
                alert.setTitle("Budget");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                alert.setNegativeButton("Abbruch", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        S.outShort(A_Budget.this,"Item Hinzugefügt!");
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();

            }
        });


        // SWIPPER - Implementierung des Swipper-Funktion
        //------------------------------------------------------------
        swipper = new ItemTouchHelper(createHelperCallBack());              // ItemTouch -> Swipper
        swipper.attachToRecyclerView(recyclerView);

    }


    /**
     *  Created by Johanns on 02.05.2017.
     */
    //-------------------------------------------------------------------------------------------
    // ---------------------------------- ITEM SWIPPER BEGINN -----------------------------------

    // CREATE HELPER CALLBACK - Funktion zum Swippen (bewegen od. löschen von Items durch swippen)
    private ItemTouchHelper.Callback createHelperCallBack(){

        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

                    @Override
                    public  boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                        moveItem(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                        return true;// true if moved, false otherwise
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
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
    private void addItem(){

        /*
        *  >>>> Hier ADD-Verbindung zur DB herstellen (wenn DB fertig)
        *  >>>> Für's erste dient der untere Code
        *
        * */

        final int budgetMax = 1000;
        int budgetCurrently = 0;

        budgetCurrently = randomNumber(budgetMax,150);

        C_Budget budget = new C_Budget(budgetMax,budgetCurrently,
                percentageCalculator(budgetMax,budgetCurrently), randomMonth(randomNumber(11,0)),
                randomTitle(randomNumber(5,0)));
        budgetList.add(budget);

        layoutManager = new LinearLayoutManager(A_Budget.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bAdapter);
        bAdapter.notifyDataSetChanged();

    }

    // ----------------------------------ITEM SWIPPER ENDE --------------------------------------
    //-------------------------------------------------------------------------------------------



    /**
     *  Created by Johanns on 30.04.2017.
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