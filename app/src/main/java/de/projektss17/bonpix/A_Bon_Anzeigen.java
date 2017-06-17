package de.projektss17.bonpix;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.daten.C_Adapter_Bon_Anzeigen;
import static de.projektss17.bonpix.S.db;



public class A_Bon_Anzeigen extends AppCompatActivity {

    private C_Adapter_Bon_Anzeigen mAdapter;
    private RecyclerView recyclerView;
    private int pos;
    private ImageButton delete, edit, info;
    private C_Bon bon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_bon_anzeigen_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent mIntent = getIntent();
        this.pos = mIntent.getIntExtra("BonPos", pos);
        this.delete = (ImageButton) findViewById(R.id.bon_anzeigen_delete);
        this.edit = (ImageButton) findViewById(R.id.bon_anzeigen_edit);
        this.info = (ImageButton) findViewById(R.id.bon_anzeigen_info);
        this.recyclerView = (RecyclerView) findViewById(R.id.bon_list_recyclerview);

        this.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(A_Bon_Anzeigen.this)
                        .setTitle(R.string.bon_anzeigen_delete_title)
                        .setMessage(R.string.bon_anzeigen_delete_message)
                        .setNegativeButton(R.string.a_laeden_alert_dialog_cancel, null)
                        .setPositiveButton(R.string.a_laeden_alert_dialog_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                S.dbHandler.removeBon(S.db, bon.getId());
                                finish();
                            }
                        }).create().show();
            }
        });

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
                .setPositiveButton("Ok", null)
                .create()
                .show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.bon = S.dbHandler.getBon(db, pos);
        mAdapter = new C_Adapter_Bon_Anzeigen(this.bon);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}