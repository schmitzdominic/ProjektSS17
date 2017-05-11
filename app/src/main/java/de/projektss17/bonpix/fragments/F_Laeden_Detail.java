package de.projektss17.bonpix.fragments;


import android.app.DialogFragment;
import android.content.Context;
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

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;
import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.daten.C_Laeden_Detail_Adapter;

public class F_Laeden_Detail extends DialogFragment {
    private List<C_Bon> bonsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.box_laeden_detail_content, container, false);
        RecyclerView recyclerViewDetailLaeden = (RecyclerView) rootView.findViewById(R.id.laeden_detail_view);
        C_Laeden_Detail_Adapter mAdapter = new C_Laeden_Detail_Adapter(bonsList);
        prepareBonData();

        recyclerViewDetailLaeden.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewDetailLaeden.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerViewDetailLaeden.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDetailLaeden.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        return rootView;
    }

    /**
     * Set Data for RecyclerView Bons
     */
    private void prepareBonData(){
        bonsList.clear();
        for(C_Bon bon : S.dbHandler.getAllBons(S.db)){
            this.bonsList.add(bon);
        }
    }

}