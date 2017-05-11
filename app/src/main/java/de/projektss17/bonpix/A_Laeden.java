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
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.daten.C_Laden;
import de.projektss17.bonpix.daten.C_Laeden_Adapter;


public class A_Laeden extends AppCompatActivity {

    private List<C_Bon> bonsList = new ArrayList<>();
    private List<C_Laden> shopList = new ArrayList<>();
    private RecyclerView recyclerViewLaeden;
    private C_Laeden_Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.box_laeden_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.laeden_fap);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Im Folgenden wird die RecyclerView angelegt und die dazugehörigen Einstellungen verwaltet
        //XML instaniziieren
        this.recyclerViewLaeden = (RecyclerView) findViewById(R.id.view_laeden);

        mAdapter = new C_Laeden_Adapter(shopList);
        prepareBonData();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewLaeden.setLayoutManager(mLayoutManager);
        recyclerViewLaeden.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerViewLaeden.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLaeden.setAdapter(mAdapter);

    }

    /**
     * Set Data for RecyclerView Shops
     */
    private void prepareBonData(){
        shopList.clear();
        for(C_Laden shop : S.dbHandler.getAllLaeden(S.db)){
            this.shopList.add(shop);
        }
        mAdapter.notifyDataSetChanged();
    }
}