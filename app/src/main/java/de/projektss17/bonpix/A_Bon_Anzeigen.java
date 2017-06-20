package de.projektss17.bonpix;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.adapter.C_Adapter_Bon_Anzeigen;
import static de.projektss17.bonpix.S.db;

public class A_Bon_Anzeigen extends AppCompatActivity {

    private C_Adapter_Bon_Anzeigen mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private int pos;
    private ImageButton delete, edit, info;
    private C_Bon bon;
    public TextView ladenName, adresse, adresseTitle, datum, artikel, garantie, garantieTitle, gesbetrag;
    public ImageView kassenzettel;
    public String image, waehrung;

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

        // Header Content
        this.kassenzettel  = (ImageView) findViewById(R.id.bon_anzeigen_picture);
        this.ladenName = (TextView) findViewById(R.id.bon_anzeigen_ladenname);
        this.adresse = (TextView) findViewById(R.id.bon_anzeigen_adresse);
        this.adresseTitle = (TextView) findViewById(R.id.bon_anzeigen_adresse_title);
        this.datum = (TextView) findViewById(R.id.bon_anzeigen_datum);
        this.artikel = (TextView) findViewById(R.id.bon_anzeigen_artikel);
        this.gesbetrag = (TextView) findViewById(R.id.bon_anzeigen_gesbetrag);
        this.garantie = (TextView) findViewById(R.id.bon_anzeigen_garantie);
        this.garantieTitle = (TextView) findViewById(R.id.bon_anzeigen_garantie_title);

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

        this.kassenzettel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image != null){
                    Intent intent = new Intent(v.getContext(), A_Max_Bon_Pic.class);
                    intent.putExtra("imageUri", image);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.bon = S.dbHandler.getBon(db, pos);

        fillBonHeader();

        this.mAdapter = new C_Adapter_Bon_Anzeigen(this,bon.getArticles());
        this.mLayoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(mLayoutManager);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setAdapter(mAdapter);
        this.mAdapter.notifyDataSetChanged();
    }

    public void fillBonHeader(){

        if(bon.getPath() != null && bon.getPath().contains(".")){
            if (bon.getPath().split("\\.")[1].equals("jpg")) {
                File image = new File(bon.getPath());
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
                this.image = bon.getPath();
                this.kassenzettel.setImageBitmap(bitmap);
            }
        }

        this.ladenName.setText(bon.getShopName());
        this.datum.setText(bon.getDate());

        if(bon.getAdress() != null && !bon.getAdress().isEmpty()){
            this.adresseTitle.setVisibility(View.VISIBLE);
            this.adresse.setText(bon.getAdress());
        } else {
            this.adresseTitle.setVisibility(View.INVISIBLE);
            this.adresse.setText("");
        }

        if(bon.getGuaranteeEnd().contains(".")){
            this.garantieTitle.setVisibility(View.VISIBLE);
            this.garantie.setText(bon.getGuaranteeEnd());
        } else {
            this.garantieTitle.setVisibility(View.INVISIBLE);
            this.garantie.setText("");
        }

        this.gesbetrag.setText(bon.getTotalPrice() + " " + this.getResources().getString(R.string.waehrung));

    }
}