package de.projektss17.bonpix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.daten.C_Bon_Anzeigen_Adapter;

import static de.projektss17.bonpix.S.db;

public class A_Bon_Anzeigen extends AppCompatActivity {

    private C_Bon_Anzeigen_Adapter mAdapter;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_bon_anzeigen_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent mIntent = getIntent();
        this.pos = mIntent.getIntExtra("BonPos", pos);
        C_Bon bon = S.dbHandler.getBon(db, pos);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.bon_list_recyclerview);
        mAdapter = new C_Bon_Anzeigen_Adapter(bon);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        Log.e("### BonAnzeigen","BonPosition: " + pos);
    }
}