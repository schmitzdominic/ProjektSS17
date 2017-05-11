package de.projektss17.bonpix;



import android.content.DialogInterface;
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

        //Floating Button instanziieren
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.laeden_fap);
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
                        .setTitle("Laden hinzufügen")
                        .setView(alertLayoutLaeden)
                        .setCancelable(false)
                        .setNegativeButton("Abbruch", null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(shopTitle.getText()== null || shopTitle.getText().toString().isEmpty())
                                    dialog.dismiss();
                                else
                                    Log.e("testausgabe", shopTitle.getText().toString());
                                    //Todo Datenbankanbindung => setLaden


                            }

                        }).create().show();
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