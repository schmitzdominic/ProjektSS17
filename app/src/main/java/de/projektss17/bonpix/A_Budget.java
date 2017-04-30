package de.projektss17.bonpix;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Handler;

import de.projektss17.bonpix.daten.C_Budget;
import de.projektss17.bonpix.daten.C_Budget_CardView_Adapter;


public class A_Budget extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<C_Budget> budgetList = new ArrayList<>();
    private RecyclerView.Adapter bAdapter;
    private FloatingActionButton fab;


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
        recyclerView = (RecyclerView) findViewById(R.id.view_budget);
        bAdapter = new C_Budget_CardView_Adapter(budgetList);


        // FAB in Recycler View - Drücken fügt eine CARD hinzu
        //------------------------------------------------------------
        this.fab = (FloatingActionButton) findViewById(R.id.budget_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOneData();
            }
        });


        // CARD VIEW mit 20 Testdaten
        //------------------------------------------------------------
        // createMoreData(20);
        // createOneData();

    }


    /**
     *  Created by Johanns on 30.04.2017.
     */
    // -----------------------------------------------------------------------
    // ------------------------- TESTDATEN BEGINN ----------------------------

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


    // Befüllung der RecyclerView mit EINER Datenmenge
    private void createOneData(){

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


    // Befüllung der RecyclerView mit mehreren Daten
    private void createMoreData(int menge) {

        final int budgetMax = 1000;
        int budgetCurrently = 0;

        for (int i = 0; i < menge; i++) {

            budgetCurrently = randomNumber(budgetMax,150);

            C_Budget budget = new C_Budget(budgetMax,budgetCurrently,
                    percentageCalculator(budgetMax,budgetCurrently), randomMonth(randomNumber(11,0)),
                    randomTitle(randomNumber(5,0)));
            budgetList.add(budget);
        }
    }

    // --------------------------- TESTDATEN ENDE ----------------------------
    // -----------------------------------------------------------------------
}