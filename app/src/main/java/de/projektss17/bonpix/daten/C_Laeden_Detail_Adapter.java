package de.projektss17.bonpix.daten;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;

/**
 * Created by Fabian on 11.05.2017.
 */

public class C_Laeden_Detail_Adapter extends RecyclerView.Adapter<C_Laeden_Detail_Adapter.MyViewHolder>{

    private ArrayList<C_Bon> bonListe;
    private C_Bon bon;

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
        holder.bonPrice.setText(bon.getTotalPrice());
    }
    @Override
    public int getItemCount() {
        return this.bonListe.size();
    }
}