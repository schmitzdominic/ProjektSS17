package de.projektss17.bonpix;

/**
 * Created by Domi on 28.03.2017.
 * Hier bitte die Logik des zweiten Tabs
 */

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import de.projektss17.bonpix.daten.C_Artikel;
import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.daten.C_Budget;
import de.projektss17.bonpix.daten.C_Laden;
import de.projektss17.bonpix.daten.C_Statistik;
import de.projektss17.bonpix.daten.C_Statistik_Adapter;


public class A_Tab2Statistik extends Fragment{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private C_Statistik_Adapter mAdapter;
    private C_Statistik statistics;
    private TabLayout tabLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.box_tab2_statistik_content, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.statistik_recyclerview);
        mAdapter = new C_Statistik_Adapter();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        // Click eines Tabs bewirkt eine Aktion (in diesem Fall sollen die Charts gefiltert werden)
        tabLayout = (TabLayout)rootView.findViewById(R.id.statistik_tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        Toast.makeText(getActivity(),"Alle ausgewählt!",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(),"Tage ausgewählt!",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(),"Monate ausgewählt!",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(),"Jahre ausgewählt!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {/* Müssen leider mit implementiert werden, machen jedoch nichts! */}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {/* Müssen leider mit implementiert werden, machen jedoch nichts! */}
        });

        return rootView;
    }


    // -------------------------- TEST DATEN ANFANG -----------------------------------
    /*
    public ArrayList<C_Laden> createLaedenData(int anzahl){
        ArrayList<C_Laden> testLaeden = null;

        for(int i = 0; i < anzahl; i++)
            testLaeden.add(new C_Laden("Supermarkt "+i));

        return testLaeden;
    }

    public ArrayList<C_Bon> createBonData(int anzahl){

        ArrayList<C_Bon> testBons = null;

        for(int i = 0; i < anzahl; i++)
            testBons.add(new C_Bon("Pfad/Bon/" + i, "Supermarkt " + i, "Max-Mustermannstr. Nr. 1" + i,
                    "Sonstige Information des Supermarktes " + i, "19.05.2017", "30.05.2017", false, true,
                    createArticleData(anzahl)));

        return testBons;
    }

    public ArrayList<C_Artikel>createArticleData(int anzahl){

        ArrayList<C_Artikel> testArticles = null;

        for(int i = 0; i < anzahl; i++)
            testArticles.add(new C_Artikel("Produkt "+i, Math.random()));

        return testArticles;
    }

    // -------------------------- TEST DATEN ENDE ------------------------------------- */
}