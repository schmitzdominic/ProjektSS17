package de.projektss17.bonpix.daten;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.projektss17.bonpix.R;

/**
 * Created by Fabian on 11.05.2017.
 */

public class C_Laeden_Detail_Adapter extends RecyclerView.Adapter<C_Laeden_Detail_Adapter.MyViewHolder>{

    private ArrayList<C_Bon> bonListe;
    private C_Bon bon;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView bonDate, bonPrice;
        public ImageView shopIcon;


        public MyViewHolder(View view){
            super(view);
            bonDate = (TextView) view.findViewById(R.id.laeden_detail_view_bon_date);
            bonPrice = (TextView) view.findViewById(R.id.laeden_detail_view_bon_price);

        }
    }

    /**
     * returned Liste
     * @param bonListe
     */
    public C_Laeden_Detail_Adapter(ArrayList<C_Bon> bonListe){
        this.bonListe = bonListe;
    }
    @Override
    public C_Laeden_Detail_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.box_laeden_detail_view, parent, false);
        return new C_Laeden_Detail_Adapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        this.bon = bonListe.get(position);
        holder.bonDate.setText(bon.getDate());
        //ToDo Hier momentan noch ein Dummy-Wert, muss später ausgetauscht werden
        holder.bonPrice.setText("300€");
    }
    @Override
    public int getItemCount() {
        return this.bonListe.size();
    }
}