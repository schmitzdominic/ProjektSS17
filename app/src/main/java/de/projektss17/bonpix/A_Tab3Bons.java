package de.projektss17.bonpix;

import android.app.SearchManager;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.daten.C_Adapter_Bons;


public class A_Tab3Bons extends Fragment{

    private List<C_Bon> bonsList = new ArrayList<>();
    private C_Adapter_Bons mAdapter;
    private RecyclerView recyclerView;
    public FloatingActionButton fabPlus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.box_bons_content, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.view_bons);
        mAdapter = new C_Adapter_Bons(bonsList);
        prepareBonData();
        fabPlus = ((A_Main) getActivity()).getFloatingActionButtonPlus();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        setHasOptionsMenu(true);

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
    public void onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem item = menu.findItem(R.id.menu_main_search);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = new SearchView(((A_Main) getActivity()).getSupportActionBar().getThemedContext());
        sv.setIconifiedByDefault(false);
        sv.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        sv.setSubmitButtonEnabled(true);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String filterString){
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pass){
                final List<C_Bon> filteredBonsList = filter(bonsList, pass);
                mAdapter.setFilter(filteredBonsList);
                return true;
            }
        });
        MenuItemCompat.setActionView(item, sv);
    }

    private List<C_Bon> filter(List<C_Bon> bons, String query) {
        query = query.toLowerCase();
        final List<C_Bon> filteredBonsList = new ArrayList<>();
        for (C_Bon bon : bons) {
            if (bon.getOtherInformations().toLowerCase().contains(query) || bon.getShopName().toLowerCase().contains(query) || bon.getTotalPrice().contains(query) || bon.getDate().contains(query)) {
                filteredBonsList.add(bon);
            }
        }
        return filteredBonsList;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
    }
}