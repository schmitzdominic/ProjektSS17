package de.projektss17.bonpix;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.projektss17.bonpix.daten.C_Gruppe;
import de.projektss17.bonpix.daten.C_Adapter_Gruppe;

public class A_Gruppen extends AppCompatActivity {

    private List<C_Gruppe> gruppenListe = new ArrayList<>();
    private RecyclerView recyclerViewGruppen;
    private C_Adapter_Gruppe mAdapter;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_gruppen_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        this.fab = (FloatingActionButton) findViewById(R.id.gruppen_fab);
        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.recyclerViewGruppen = (RecyclerView) findViewById(R.id.recyclerview_gruppen);

        mAdapter = new C_Adapter_Gruppe(gruppenListe);
        prepareGruppenData();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewGruppen.setLayoutManager(mLayoutManager);
        recyclerViewGruppen.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerViewGruppen.setItemAnimator(new DefaultItemAnimator());
        recyclerViewGruppen.setAdapter(mAdapter);

        recyclerViewGruppen.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) {
                    // Scroll Down
                    if (fab.isShown()) {
                        fab.hide();

                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!fab.isShown()) {
                        fab.show();
                    }
                }
            }
        });

    }

    private void prepareGruppenData(){
// ToDo: Wenn die Benötigten Funktionen Bestehen werden hier die Daten aus der Datenbank ausgelesen und zurückgegeben (Ggf. noch anpassen da die Namen noch nicht bekannt sind)

        gruppenListe.clear();

        for(int i = 0; i < 10; i++){
            gruppenListe.add(new C_Gruppe("TESTGRUPPE"+(i+1)));
            Log.e("### GRUPPE", "TESTGRUPPE" + (i+1) + " wurde erstellt");
        }

        mAdapter.notifyDataSetChanged();
    }

}