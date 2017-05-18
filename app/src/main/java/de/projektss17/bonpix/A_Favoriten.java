package de.projektss17.bonpix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

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

        //Im Folgenden wird die RecyclerView angelegt und die dazugehörigen Einstellungen verwaltet
        //XML instaniziieren
        this.recyclerViewFavoriten = (RecyclerView) findViewById(R.id.favoriten_view);

        mAdapter = new C_Adapter_Favoriten(bonListe);
        prepareBonData();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewFavoriten.setLayoutManager(mLayoutManager);
        recyclerViewFavoriten.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerViewFavoriten.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFavoriten.setAdapter(mAdapter);
    }

    /**
     * Daten in Array-List BonListe füllen, welche später angezeigt werden
     */
    private void prepareBonData(){

        for(C_Bon bon : S.dbHandler.getAllBons(S.db)){
            if(bon.getFavourite()){
                this.bonListe.add(bon);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

}
