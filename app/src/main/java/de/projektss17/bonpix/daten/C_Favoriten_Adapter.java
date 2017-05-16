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

import java.util.List;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;

/**
 * Created by Daniel on 09.05.2017.
 */

public class C_Favoriten_Adapter extends RecyclerView.Adapter<C_Favoriten_Adapter.MyViewHolder> {
    private List<C_Bon> bonListe;
    private C_Bon bon;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView favoriteShopName, favoriteDate;
        public ImageView icon, deleteBtn;
        public Resources res;


        public MyViewHolder(View view){
            super(view);
            icon = (ImageView) view.findViewById(R.id.favoriten_view_laden_bild);
            favoriteShopName = (TextView) view.findViewById(R.id.favoriten_view_favoriteShopName);
            favoriteDate = (TextView) view.findViewById(R.id.favoriten_view_zusatz_favoriteDate);
            deleteBtn = (ImageView) view.findViewById(R.id.favoriten_view_favoriten_delete_button);
            res = view.getResources();


        }
    }

    /**
     * returned Liste
     * @param bonListe
     */
    public C_Favoriten_Adapter(List<C_Bon> bonListe){

        this.bonListe = bonListe;
    }


    @Override
    public C_Favoriten_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.box_favoriten_view, parent, false);

        return new C_Favoriten_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final C_Favoriten_Adapter.MyViewHolder holder, final int position) {
        this.bon = bonListe.get(position);

        Bitmap imageBitmap = S.getShopIcon(holder.res, bon.getShopName());
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(holder.res, imageBitmap);
        roundedBitmapDrawable.setCircular(true);
        roundedBitmapDrawable.setAntiAlias(true);
        holder.icon.setImageDrawable(roundedBitmapDrawable);

        holder.favoriteShopName.setText(bon.getShopName());
        holder.favoriteDate.setText(bon.getDate());

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {


            /**
             * On Click Methode für onClickListener
             * @param v
             */
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
}
