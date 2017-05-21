package de.projektss17.bonpix;

/**
 * Created by Domi on 28.03.2017.
 * Hier bitte die Logik des zweiten Tabs
 */

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.projektss17.bonpix.daten.C_DatabaseHandler;
import de.projektss17.bonpix.daten.C_Statistik_Adapter;


public class A_Tab2Statistik extends Fragment{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private C_Statistik_Adapter mAdapter;
    private TabLayout tabLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.box_tab2_statistik_content, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.statistik_recyclerview);
        mAdapter = new C_Statistik_Adapter(new C_DatabaseHandler(getActivity()));
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
                        mAdapter.filter="ALLE";     // keine Filter, d.h. alles wird angezeigt
                        mAdapter.notifyDataSetChanged();
                        S.outShort((AppCompatActivity)getActivity(),"Gesamte Statistiken");
                        break;
                    case 1:
                        mAdapter.filter="TAG";      // Filterung auf den Tag, d.h. Alles des gegenwärtigen Tages wird angezeigt
                        mAdapter.notifyDataSetChanged();
                        S.outShort((AppCompatActivity)getActivity(),"Statistiken des heutigen Tages");
                        break;
                    case 2:
                        mAdapter.filter="MONAT";    // Filterung auf den Monat, d.h. Alles des gegenwärtigen Monats wird angezeigt
                        mAdapter.notifyDataSetChanged();
                        S.outShort((AppCompatActivity)getActivity(),"Statistiken des gegenwärtigen Monats");
                        break;
                    case 3:
                        mAdapter.filter="JAHR";     // Filterung auf den Jahr, d.h. Alles des gegenwärtigen Jahr wird angezeigt
                        mAdapter.notifyDataSetChanged();
                        S.outShort((AppCompatActivity)getActivity(),"Statistiken des gegenwärtigen Jahres");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {/* MÜSSEN LEIDER mit implementiert werden, machen jedoch nichts! */}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {/* MÜSSEN LEIDER mit implementiert werden, machen jedoch nichts! */}
        });


        return rootView;
    }
}