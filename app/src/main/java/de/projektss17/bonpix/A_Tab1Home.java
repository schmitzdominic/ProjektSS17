package de.projektss17.bonpix;

/**
 * ReCreated by Johanns on 27.05.2017.
 * Hier bitte die Logik des ersten Tabs
 */

import android.support.design.widget.FloatingActionButton;
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
    public FloatingActionButton fabPlus;
    private View rootView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.box_tab1_home_content, container, false);
        this.fabPlus = ((A_Main) getActivity()).getFloatingActionButtonPlus();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.tab_home_recycler_view);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) {
                    // Scroll Down
                    if (fabPlus.isShown()) {

                        if (((A_Main) getActivity()).getFabState()) {
                            ((A_Main) getActivity()).closeFABMenu();
                        }
                        fabPlus.hide();
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!fabPlus.isShown()) {
                        fabPlus.show();
                    }
                }
            }
        });

        homeAdapter = new C_Home_Adapter(getActivity());
        homeAdapter.prepareData();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(homeAdapter);
        homeAdapter.notifyDataSetChanged();

    }
}