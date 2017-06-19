package de.projektss17.bonpix;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.daten.C_Adapter_Favoriten;

public class A_Favoriten extends AppCompatActivity {

    private List<C_Bon> bonListe = new ArrayList<>();
    private RecyclerView recyclerViewFavoriten;
    private C_Adapter_Favoriten mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_favoriten_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Daten in Array-List BonListe füllen, welche später angezeigt werden
     */
    private void prepareBonData(){

        this.bonListe.clear();

        for(C_Bon bon : S.dbHandler.getNumberOfNewestBons(S.db, S.dbHandler.getAllBonsCount(S.db))){
            if(bon.getFavourite()){
                this.bonListe.add(bon);
            }
        }
        this.mAdapter.notifyDataSetChanged();
    }

    /**
     * Fügt alle optionen die in menu/menu.menu_mainl angegeben wurden
     * hinzu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem item = menu.findItem(R.id.menu_main_search);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = (SearchView) MenuItemCompat.getActionView(item);
        sv.setIconifiedByDefault(false);
        sv.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        sv.setSubmitButtonEnabled(true);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String filterString){
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pass){
                final List<C_Bon> filteredBonsList = filter(bonListe, pass);
                mAdapter.setFilter(filteredBonsList);
                return true;
            }
        });
        MenuItemCompat.setActionView(item, sv);
        return true;
    }

    private List<C_Bon> filter(List<C_Bon> bons, String query) {
        query = query.toLowerCase();
        final List<C_Bon> filteredBonsList = new ArrayList<>();
        for (C_Bon bon : bons) {
            if(bon.getFavourite()) {
                if (bon.getShopName().toLowerCase().contains(query) || bon.getTotalPrice().contains(query) || bon.getDate().contains(query)) {
                    filteredBonsList.add(bon);
                }
            }
        }
        return filteredBonsList;
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.recyclerViewFavoriten = (RecyclerView) findViewById(R.id.favoriten_view);
        this.mAdapter = new C_Adapter_Favoriten(bonListe);
        this.prepareBonData();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewFavoriten.setLayoutManager(mLayoutManager);
        recyclerViewFavoriten.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerViewFavoriten.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFavoriten.setAdapter(mAdapter);
    }
}
