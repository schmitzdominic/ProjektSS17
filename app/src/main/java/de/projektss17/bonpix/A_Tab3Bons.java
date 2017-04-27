package de.projektss17.bonpix;

/**
 * Created by Domi on 28.03.2017.
 * Hier bitte die Logik des dritten Tabs
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.daten.C_Bons_Adapter;


public class A_Tab3Bons extends Fragment{

    private List<C_Bon> bonsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private C_Bons_Adapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.box_bons_content, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.view_bons);
        mAdapter = new C_Bons_Adapter(bonsList);
        prepareBonData();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        return rootView;
    }

    /**
     * Set Test Data for RecyclerViewList
     */
    private void prepareBonData(){
        for(int i = 0; i < 20; i++) {
            C_Bon bons = new C_Bon("TEST"+i, "TEST"+(char)(i+65), "Test", "Test", "Test", false, false);
            bonsList.add(bons);
        }
    }
}