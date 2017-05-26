package de.projektss17.bonpix;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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
import de.projektss17.bonpix.daten.C_Adapter_Bons;


public class A_Tab3Bons extends Fragment{

    private List<C_Bon> bonsList = new ArrayList<>();
    public FloatingActionButton fabPlus;
    private C_Adapter_Bons mAdapter;
    private RecyclerView recyclerView;
    private ViewGroup container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.box_bons_content, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.view_bons);
        fabPlus = ((A_Main) getActivity()).getFloatingActionButtonPlus();
        this.container = container;

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

        return rootView;
    }

    /**
     * Set Data for RecyclerView Bons
     */
    private void prepareBonData(){
        bonsList.clear();

        ArrayList<C_Bon> bonList = S.dbHandler.getAllBons(S.db);

        for(int i = bonList.size()-1; i >= 0; i--){
            this.bonsList.add(bonList.get(i));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter = new C_Adapter_Bons(bonsList);
        prepareBonData();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}