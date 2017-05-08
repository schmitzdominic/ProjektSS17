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

import de.projektss17.bonpix.daten.C_Bons;
import de.projektss17.bonpix.daten.C_Garantie_Adapter;

public class A_Garantie extends AppCompatActivity {

    private List<C_Bons> bonListe = new ArrayList<>();
    private RecyclerView recyclerViewGarantie;
    private C_Garantie_Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_garantie_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Im Folgenden wird die RecyclerView angelegt und die dazugehörigen Einstellungen verwaltet
        //XML instaniziieren
        this.recyclerViewGarantie = (RecyclerView) findViewById(R.id.garantie_view);

        mAdapter = new C_Garantie_Adapter(bonListe);
        prepareBonData();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewGarantie.setLayoutManager(mLayoutManager);
        recyclerViewGarantie.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerViewGarantie.setItemAnimator(new DefaultItemAnimator());
        recyclerViewGarantie.setAdapter(mAdapter);

    }

    /**
     * Daten in Array-List BonListe füllen, welche später angezeigt werden
     */
    private void prepareBonData(){
        for(int i = 0; i < 20; i++) {
            C_Bons bons = new C_Bons("TEST"+i, "TEST"+(char)(i+65), "Test", "Test", "Test");
            bonListe.add(bons);
        }
        //ToDo Auskommentieren nachdem Datenbank_verbessern gemerged wurde und obiges löschen
        /*
        for(C_Bon bon : S.dbHandler.getAllBons(S.db)){
            if(bon.getGarantie()){
                this.bonlist.add(bon);
            }
        }*/

        mAdapter.notifyDataSetChanged();
    }

}
