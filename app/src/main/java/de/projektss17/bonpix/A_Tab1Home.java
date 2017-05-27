package de.projektss17.bonpix;

/**
 * ReCreated by Johanns on 27.05.2017.
 * Hier bitte die Logik des ersten Tabs
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import de.projektss17.bonpix.daten.C_Home_Adapter;


public class A_Tab1Home extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private C_Home_Adapter homeAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.box_tab1_home_content, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.tab_home_recycler_view);
        homeAdapter = new C_Home_Adapter(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(homeAdapter);
        homeAdapter.notifyDataSetChanged();

        return rootView;
    }

}