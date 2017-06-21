package de.projektss17.bonpix.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;
import de.projektss17.bonpix.daten.C_Artikel;

public class C_Adapter_Bon_Anzeigen extends RecyclerView.Adapter<C_Adapter_Bon_Anzeigen.ViewHolder> {

    private ArrayList<C_Artikel> artikel;
    private Context context;

    public C_Adapter_Bon_Anzeigen(Context context, ArrayList<C_Artikel> artikel){
        this.context = context;
        this.artikel = artikel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView bonArtikel, preis;

        public ViewHolder(final View view){
            super(view);

            this.bonArtikel = (TextView)view.findViewById(R.id.bon_anzeigen_artikel);
            this.preis = (TextView)view.findViewById(R.id.bon_anzeigen_artikel_preis);

        }
    }


    @Override
    public C_Adapter_Bon_Anzeigen.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_bon_anzeigen_bottom_content, parent, false);
        return new C_Adapter_Bon_Anzeigen.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(C_Adapter_Bon_Anzeigen.ViewHolder holder, int position) {
        holder.bonArtikel.setText(artikel.get(position).getName());
        holder.preis.setText(S.roundPrice(artikel.get(position).getPrice())+" "+ context.getResources().getString(R.string.waehrung));
    }

    @Override
    public int getItemCount() {
        return artikel.size();
    }

}