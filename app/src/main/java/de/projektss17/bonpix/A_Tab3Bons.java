package de.projektss17.bonpix;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
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


public class A_Tab3Bons extends Fragment implements SearchView.OnQueryTextListener{

    private List<C_Bon> bonsList = new ArrayList<>();
    public FloatingActionButton fabPlus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.box_bons_content, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.view_bons);
        C_Adapter_Bons mAdapter = new C_Adapter_Bons(bonsList);
        prepareBonData();
        fabPlus = ((A_Main) getActivity()).getFloatingActionButtonPlus();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem item = menu.findItem(R.id.menu_main_search);

        //SearchView sv = new SearchView(((A_Main) getActivity()).getSupportActionBar().getThemedContext());
        SearchView sv = (SearchView) item.getActionView();
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(this);
        sv.setIconifiedByDefault(false);
        sv.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        sv.setSubmitButtonEnabled(true);
        sv.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("CLICKED","CHECK");
            }
        });

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                Log.e("CLOSED","");
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                Log.e("OPENED", "");
                return true;  // Return true to expand action view
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e("SUBMITTED", "");
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e("CHANGED", "");
        return false;
    }

    /*private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }


    public void onNewIntent(Intent intent) {
        //setIntent()...
        handleIntent(intent);
    }

    private void doSearch(String queryStr) {
        Log.e("Your search: ",queryStr);
    }*/
}