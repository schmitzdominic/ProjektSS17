package de.projektss17.bonpix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.daten.C_Bon_Anzeigen_Adapter;

import static de.projektss17.bonpix.S.db;

public class A_Bon_Anzeigen extends AppCompatActivity {


    private C_Bon_Anzeigen_Adapter mAdapter;
    private int pos;
    private ImageButton edit, info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_bon_anzeigen_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent mIntent = getIntent();
        this.pos = mIntent.getIntExtra("BonPos", pos);
        final C_Bon bon = S.dbHandler.getBon(db, pos);

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

        this.edit = (ImageButton) findViewById(R.id.bon_anzeigen_edit);
        this.info = (ImageButton) findViewById(R.id.bon_anzeigen_info);

        this.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.showManuell(A_Bon_Anzeigen.this, pos, "edit");
            }
        });

        this.info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                new AlertDialog.Builder(A_Bon_Anzeigen.this)
                .setTitle("Sonstige Informationen")
                .setMessage(bon.getOtherInformations())
                .setNeutralButton("Ok", null)
                .create()
                .show();
            }
        });

    }
}