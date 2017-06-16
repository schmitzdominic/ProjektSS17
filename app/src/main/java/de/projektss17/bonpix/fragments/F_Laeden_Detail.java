package de.projektss17.bonpix.fragments;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import de.projektss17.bonpix.adapter.C_Adapter_Laeden_Detail;

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
        C_Adapter_Laeden_Detail mAdapter = new C_Adapter_Laeden_Detail(bonsList);

        this.name = getArguments().getString("ShopName");
        prepareBonData(name);

        shopName = (EditText)rootView.findViewById(R.id.laeden_detail_shop_name);
        deleteButton = (Button)rootView.findViewById(R.id.laeden_detail_delete);
        saveButton = (Button)rootView.findViewById(R.id.laeden_detail_speichern);

        /**
         * Triggers Dialog (if user really wants to delete)
         */
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                        alert.setTitle(R.string.f_laeden_detail_delete_titel);
                        alert.setMessage(name + " " + v.getContext().getResources().getString(R.string.f_laeden_detail_delete_message));
                        alert.setNegativeButton(v.getContext().getResources().getString(R.string.f_laeden_detail_cancel), null);
                        alert.setPositiveButton(v.getContext().getResources().getString(R.string.f_laeden_detail_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                S.dbHandler.removeLaden(S.db, name);
                                Toast.makeText(getDialog().getContext(), getDialog().getContext().getResources().getString(R.string.f_laeden_detail_delete_toast1) + " " + name + " " + getDialog().getContext().getResources().getString(R.string.f_laeden_detail_delete_toast2), Toast.LENGTH_LONG).show();
                                ((A_Laeden) getActivity()).prepareShopData();
                                getDialog().dismiss();
                            };
                        });alert.show();
            }
        });

        /**
         * Triggers the AlertDialog
         */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle(R.string.f_laeden_detail_save_titel);
                alert.setMessage(R.string.f_laeden_detail_save_message);
                alert.setNegativeButton(v.getContext().getResources().getString(R.string.f_laeden_detail_cancel), null);
                alert.setPositiveButton(v.getContext().getResources().getString(R.string.f_laeden_detail_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!S.dbHandler.checkIfLadenExist(S.db, shopName.getText().toString())){
                            C_Laden ladenInst = S.dbHandler.getLaden(S.db, name);
                            ladenInst.setName(shopName.getText().toString());
                            S.dbHandler.updateLaden(S.db, ladenInst);
                            Toast.makeText(getDialog().getContext(), getDialog().getContext().getResources().getString(R.string.f_laeden_detail_save_save_toast), Toast.LENGTH_LONG).show();
                            ((A_Laeden) getActivity()).prepareShopData();
                            getDialog().dismiss();
                        } else if(shopName.getText().toString().equals(name)){
                            getDialog().dismiss();
                        } else {
                            Toast.makeText(getDialog().getContext(), getDialog().getContext().getResources().getString(R.string.f_laeden_detail_save_toast), Toast.LENGTH_LONG).show();
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
        shopName.setSingleLine(true);
        shopName.setSelection(shopName.getText().length());
        super.onResume();
    }

    /**
     * Set Data for RecyclerView Bons
     */
    private void prepareBonData(String name){
        bonsList.clear();
        for(C_Bon bon : S.dbHandler.getBonsOfStore(db, name)){
            this.bonsList.add(bon);
        }
    }
}