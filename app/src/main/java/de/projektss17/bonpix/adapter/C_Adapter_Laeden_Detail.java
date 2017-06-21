package de.projektss17.bonpix.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.daten.C_Bon;

public class C_Adapter_Laeden_Detail extends RecyclerView.Adapter<C_Adapter_Laeden_Detail.MyViewHolder>{

    private ArrayList<C_Bon> bonListe;
    private C_Bon bon;

    /**
     * Standard constructor
     * @param bonListe
     */
    public C_Adapter_Laeden_Detail(ArrayList<C_Bon> bonListe){
        this.bonListe = bonListe;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView bonDate, bonPrice;
        public ImageView shopIcon;
        public Resources res;

        public MyViewHolder(View view){
            super(view);
            res = view.getResources();
            bonDate = (TextView) view.findViewById(R.id.laeden_detail_view_bon_date);
            bonPrice = (TextView) view.findViewById(R.id.laeden_detail_view_bon_price);
            shopIcon = (ImageView) view.findViewById(R.id.laeden_detail_icon);
        }
    }

    @Override
    public C_Adapter_Laeden_Detail.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.box_laeden_detail_view, parent, false);
        return new C_Adapter_Laeden_Detail.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        this.bon = bonListe.get(position);
        holder.bonDate.setText(bon.getDate());
        holder.bonPrice.setText(bon.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return this.bonListe.size();
    }
}