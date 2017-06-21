package de.projektss17.bonpix.adapter;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
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
import de.projektss17.bonpix.daten.C_Bon;

public class C_Adapter_Favoriten extends RecyclerView.Adapter<C_Adapter_Favoriten.MyViewHolder> {

    private List<C_Bon> bonListe;
    private C_Bon bon;

    /**
     * Standard Constructor
     * @param bonListe
     */
    public C_Adapter_Favoriten(List<C_Bon> bonListe){
        this.bonListe = bonListe;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView favoriteShopName, favoriteDate, favouritePrice;
        public ImageView icon, deleteBtn;
        public Resources res;

        public MyViewHolder(View view){
            super(view);
            icon = (ImageView) view.findViewById(R.id.favoriten_view_laden_bild);
            favoriteShopName = (TextView) view.findViewById(R.id.favoriten_view_favorite_shopname);
            favoriteDate = (TextView) view.findViewById(R.id.favoriten_view_zusatz_favorite_date);
            favouritePrice = (TextView) view.findViewById(R.id.favoriten_view_zusatz_favorite_price);
            deleteBtn = (ImageView) view.findViewById(R.id.favoriten_view_favoriten_delete_button);
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

    @Override
    public C_Adapter_Favoriten.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.box_favoriten_view, parent, false);
        return new C_Adapter_Favoriten.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final C_Adapter_Favoriten.MyViewHolder holder, final int position) {

        this.bon = bonListe.get(position);
        holder.icon.setImageBitmap(S.getShopIcon(holder.res, bon.getShopName()));
        holder.favoriteShopName.setText(bon.getShopName());
        holder.favouritePrice.setText(bon.getTotalPrice() + " €");
        holder.favoriteDate.setText(S.getWeekday(holder.res, S.getWeekdayNumber(bon.getDate())) + "\n" + bon.getDate());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bonListe.get(position).setFavourite(false);
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

    /**
     * Set the Adapter List to the passed List
     * Passed List contains the search objects
     * @param passedList
     */
    public void setFilter(List<C_Bon> passedList) {
        bonListe = new ArrayList<>();
        bonListe.addAll(passedList);
        notifyDataSetChanged();
    }
}