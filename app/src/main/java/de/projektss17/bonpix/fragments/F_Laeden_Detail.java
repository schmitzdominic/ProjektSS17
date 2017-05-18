package de.projektss17.bonpix.fragments;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;
import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.daten.C_Laeden_Detail_Adapter;

import static de.projektss17.bonpix.S.db;

public class F_Laeden_Detail extends DialogFragment {
    private ArrayList<C_Bon> bonsList = new ArrayList<>();
    private EditText shopName;
    private String name;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.box_laeden_detail_content, container, false);
        RecyclerView recyclerViewDetailLaeden = (RecyclerView) rootView.findViewById(R.id.laeden_detail_view);
        C_Laeden_Detail_Adapter mAdapter = new C_Laeden_Detail_Adapter(bonsList);

        //Erhalt des shopnamen von C_Laeden_Adapter
        this.name = getArguments().getString("ShopName");
        prepareBonData(name);

        shopName = (EditText)rootView.findViewById(R.id.laeden_detail_shop_name);


        recyclerViewDetailLaeden.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewDetailLaeden.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerViewDetailLaeden.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDetailLaeden.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        return rootView;
    }

    @Override
    public void onResume() {
        shopName.setText(this.name);
        super.onResume();
    }

    /**
     * Set Data for RecyclerView Bons
     */
    private void prepareBonData(String name){
        bonsList.clear();
        for(C_Bon bon : S.dbHandler.getBonsOfStore(db, name)){
            this.bonsList.add(bon);
            Log.e("### prepareDATA","" + bon.getShopName());
        }

    }
}