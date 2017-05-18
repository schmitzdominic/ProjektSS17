package de.projektss17.bonpix.fragments;


import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import de.projektss17.bonpix.A_Laeden;
import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;
import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.daten.C_Laden;
import de.projektss17.bonpix.daten.C_Laeden_Detail_Adapter;

import static de.projektss17.bonpix.S.db;

public class F_Laeden_Detail extends DialogFragment {
    private ArrayList<C_Bon> bonsList = new ArrayList<>();
    private EditText shopName;
    private String name;
    private Button deleteButton;
    private Button saveButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.box_laeden_detail_content, container, false);
        RecyclerView recyclerViewDetailLaeden = (RecyclerView) rootView.findViewById(R.id.laeden_detail_view);
        C_Laeden_Detail_Adapter mAdapter = new C_Laeden_Detail_Adapter(bonsList);

        //Erhalt des shopnamen von C_Laeden_Adapter
        this.name = getArguments().getString("ShopName");
        prepareBonData(name);

        shopName = (EditText)rootView.findViewById(R.id.laeden_detail_shop_name);
        deleteButton = (Button)rootView.findViewById(R.id.laeden_detail_delete);
        saveButton = (Button)rootView.findViewById(R.id.laeden_detail_speichern);

        //Delete Button Funktion
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DIALOG Fenster
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                        alert.setTitle("Achtung ");
                        alert.setMessage(name + " Wird GELÖSCHT! \n\nWenn Sie Bestätigen werden alle zugeordneten Bons entfernt.");
                        alert.setNegativeButton(v.getContext().getResources().getString(R.string.a_ocr_manuell_pop_up_cancel), null);
                        alert.setPositiveButton(v.getContext().getResources().getString(R.string.a_ocr_manuell_pop_up_confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                S.dbHandler.removeLaden(S.db, name);
                                Toast.makeText(getDialog().getContext(), "Laden " + name + " wurde gelöscht!", Toast.LENGTH_LONG).show();
                                ((A_Laeden) getActivity()).prepareShopData();
                                getDialog().dismiss();

                            };
                        });alert.show();
            }

        });

        //Save Button Funktion
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DIALOG Fenster
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("Ladenname ändern");
                alert.setMessage("Sollen Ihre Eingaben übernommen werden");
                alert.setNegativeButton(v.getContext().getResources().getString(R.string.a_ocr_manuell_pop_up_cancel), null);
                alert.setPositiveButton(v.getContext().getResources().getString(R.string.a_ocr_manuell_pop_up_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(!S.dbHandler.checkIfLadenExist(S.db, name)){
                            C_Laden ladenInst = S.dbHandler.getLaden(S.db, name);
                            ladenInst.setName(shopName.getText().toString());
                            S.dbHandler.updateLaden(S.db, ladenInst);
                            ((A_Laeden) getActivity()).prepareShopData();
                            getDialog().dismiss();
                        } else if(shopName.getText().toString().equals(name)){
                            getDialog().dismiss();
                        } else {
                            Toast.makeText(getDialog().getContext(), "Laden bereits vorhanden! Bitte geben Sie einen anderen Wert ein.", Toast.LENGTH_LONG).show();
                        }


                    };
                });alert.show();
            }
        });


        recyclerViewDetailLaeden.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewDetailLaeden.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerViewDetailLaeden.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDetailLaeden.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        return rootView;
    }

    @Override
    public void onResume() {
        shopName.setText(this.name);
        super.onResume();
    }

    /**
     * Set Data for RecyclerView Bons
     */
    private void prepareBonData(String name){
        bonsList.clear();
        for(C_Bon bon : S.dbHandler.getBonsOfStore(db, name)){
            this.bonsList.add(bon);
            Log.e("### prepareDATA","" + bon.getShopName());
        }

    }
}