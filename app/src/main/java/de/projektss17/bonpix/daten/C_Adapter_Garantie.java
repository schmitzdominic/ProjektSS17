package de.projektss17.bonpix.daten;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.projektss17.bonpix.A_Bon_Anzeigen;
import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;

/**
 * Created by Fabian on 25.04.2017.
 */

public class C_Adapter_Garantie extends RecyclerView.Adapter<C_Adapter_Garantie.MyViewHolder> {

    private List<C_Bon> bonListe;
    private List<C_Bon> filteredList;
    private C_Bon bon;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView warrantyShop, warrantyEnd, warrantyPrice;
        public ImageView icon, deleteBtn;
        public Resources res;

        public MyViewHolder(View view){
            super(view);
            icon = (ImageView) view.findViewById(R.id.garantie_view_laden_bild);
            warrantyShop = (TextView) view.findViewById(R.id.garantie_view_shopname);
            warrantyEnd = (TextView) view.findViewById(R.id.garantie_view_zusatz_garantieende);
            warrantyPrice = (TextView) view.findViewById(R.id.garantie_view_zusatz_favorite_price);
            deleteBtn = (ImageView) view.findViewById(R.id.garantie_view_garantie_delete_button);
            res = view.getResources();

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final C_Bon bon = bonListe.get(getAdapterPosition());
                    int pos = bon.getId();
                    Intent intent = new Intent(v.getContext(), A_Bon_Anzeigen.class);
                    intent.putExtra("BonPos", pos);
                    v.getContext().startActivity(intent);
                }
            });

        }
    }

    /**
     * returned Liste
     * @param bonListe
     */
    public C_Adapter_Garantie(List<C_Bon> bonListe){
        this.filteredList = new ArrayList<>();
        this.bonListe = bonListe;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.box_garantie_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final C_Adapter_Garantie.MyViewHolder holder, final int position) {

        this.bon = bonListe.get(position);
        
        holder.icon.setImageBitmap(S.getShopIcon(holder.res, bon.getShopName()));
        holder.warrantyShop.setText(bon.getShopName());
        holder.warrantyEnd.setText(holder.res.getString(R.string.a_garantie_garantie_bis) + " " + bon.getGuaranteeEnd());

        // TODO: € is hardcoded. Has to be implement as Value String in strings.xml or another solution
        holder.warrantyPrice.setText(bon.getTotalPrice() + " €");

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {


            /**
             * On Click Methode für onClickListener
             * @param v
             */
            public void onClick(View v) {
                bonListe.get(position).setGuarantee(false);
                S.dbHandler.updateBon(S.db, bonListe.get(position));
                bonListe.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.bonListe.size();
    }

    public void setFilter(List<C_Bon> passedList) {
        bonListe = new ArrayList<>();
        bonListe.addAll(passedList);
        notifyDataSetChanged();
    }
}