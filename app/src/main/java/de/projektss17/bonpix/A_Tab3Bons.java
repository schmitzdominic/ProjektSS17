package de.projektss17.bonpix;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.box_bons_content, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.view_bons);
        C_Bons_Adapter mAdapter = new C_Bons_Adapter(bonsList);
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
     * Set Data for RecyclerView Bons
     */
    private void prepareBonData(){
        for(C_Bon bon : S.dbHandler.getAllBons(S.db)){
            this.bonsList.add(bon);
        }
    }
}