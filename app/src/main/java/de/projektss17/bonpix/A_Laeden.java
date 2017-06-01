package de.projektss17.bonpix;



import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;

import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.daten.C_Laden;
import de.projektss17.bonpix.daten.C_Laeden_Adapter;


public class A_Laeden extends AppCompatActivity {

    private ArrayList<C_Bon> bonsList = new ArrayList<>();
    private ArrayList<C_Laden> shopList = new ArrayList<>();
    private RecyclerView recyclerViewLaeden;
    private C_Laeden_Adapter mAdapter;
    private FloatingActionButton fab;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.box_laeden_screen);
        res = getResources();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Floating Button instanziieren
        fab = (FloatingActionButton) findViewById(R.id.laeden_fap);
        fab.setOnClickListener(new View.OnClickListener() {

            /**
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayoutLaeden = inflater.inflate(R.layout.box_laeden_alert_dialog, null);
                final EditText shopTitle = (EditText) alertLayoutLaeden.findViewById(R.id.laeden_alert_dialog_title);


                // DIALOG Fenster
                new AlertDialog.Builder(A_Laeden.this)
                        .setTitle(R.string.a_laeden_alert_dialog_title)
                        .setView(alertLayoutLaeden)
                        .setCancelable(false)
                        .setNegativeButton(R.string.a_laeden_alert_dialog_cancel, null)
                        .setPositiveButton(R.string.a_laeden_alert_dialog_ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                    if(shopTitle.getText() != null && !shopTitle.getText().toString().isEmpty() ){
                                        if(!S.dbHandler.checkIfLadenExist(S.db, shopTitle.getText().toString())){
                                            //Hinzufügen Laden zur Datenbank
                                            S.dbHandler.addLaden(S.db, new C_Laden(shopTitle.getText().toString()));
                                            prepareShopData();
                                        } else {
                                            S.outLong(A_Laeden.this, res.getString(R.string.a_laeden_alert_dialog_toast1));
                                        }
                                    } else {
                                        S.outLong(A_Laeden.this, res.getString(R.string.a_laeden_alert_dialog_toast2));
                                    }

                            }

                        }).create().show();
            }

        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Im Folgenden wird die RecyclerView angelegt und die dazugehörigen Einstellungen verwaltet
        //XML instaniziieren
        this.recyclerViewLaeden = (RecyclerView) findViewById(R.id.view_laeden);

        //RecyclerView in C_Laeden_Adapter
        mAdapter = new C_Laeden_Adapter(this, shopList);
        prepareShopData();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewLaeden.setLayoutManager(mLayoutManager);
        recyclerViewLaeden.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerViewLaeden.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLaeden.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        //Damit der Floating Button beim Scrollen verschwindet
        recyclerViewLaeden.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    /**
     * Set Data for RecyclerView Shops
     */
    public void prepareShopData(){
        shopList.clear();
        for(C_Laden shop : S.dbHandler.getAllLaeden(S.db)){
            this.shopList.add(shop);
        }
        mAdapter.notifyDataSetChanged();
        //Sortierung der Recycler View
        Collections.sort(shopList);
    }
}