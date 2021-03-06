package de.projektss17.bonpix;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.projektss17.bonpix.adapter.C_Adapter_Home;

public class A_Tab1Home extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private C_Adapter_Home homeAdapter;
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

                    if (fabPlus.isShown()) { // Scroll Down

                        if (((A_Main) getActivity()).getFabState()) {
                            ((A_Main) getActivity()).closeFABMenu();
                        }
                        fabPlus.hide();
                    }
                }
                else if (dy <0) {

                    if (!fabPlus.isShown()) { // Scroll Up
                        fabPlus.show();
                    }
                }
            }
        });

        homeAdapter = new C_Adapter_Home(getActivity());
        homeAdapter.prepareBonData();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(homeAdapter);
        homeAdapter.notifyDataSetChanged();
    }
}