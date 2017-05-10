package de.projektss17.bonpix;

/**
 * Created by Domi on 28.03.2017.
 * Hier bitte die Logik des zweiten Tabs
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.projektss17.bonpix.daten.C_Statistik_Adapter;


public class A_Tab2Statistik extends Fragment{

    private RecyclerView recyclerView;
    private C_Statistik_Adapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.box_tab2_statistik_content, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.statistik_recyclerview);
        mAdapter = new C_Statistik_Adapter();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        return rootView;
    }
}