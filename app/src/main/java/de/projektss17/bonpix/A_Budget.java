package de.projektss17.bonpix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

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

    private ProgressBar progressBar;
    private int progressStatus = 0;
    //private Handler handler = new Handler();

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


        // PROGRESSBAR in CARD VIEW
        //------------------------------------------------------------

        //------------------------------------------------------------



        // CARD VIEW in RECYCLER VIEW
        //------------------------------------------------------------
        recyclerView = (RecyclerView) findViewById(R.id.view_budget);
        bAdapter = new C_Budget_CardView_Adapter(budgetList);

        prepareBudgetData();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bAdapter);
        bAdapter.notifyDataSetChanged();
        //------------------------------------------------------------
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


    // Befüllung der RecyclerView mit den Random-Funktionen
    private void prepareBudgetData() {

        final int budgetMax = 1000;
        int budgetCurrently = 0;

        for (int i = 0; i < 20; i++) {

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