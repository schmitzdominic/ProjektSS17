package de.projektss17.bonpix.daten;

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

import java.util.List;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;

/**
 * Created by Fabian on 25.04.2017.
 */

public class C_Adapter_Garantie extends RecyclerView.Adapter<C_Adapter_Garantie.MyViewHolder> {

    private List<C_Bon> bonListe;
    private C_Bon bon;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView warrantyShop, warrantyEnd, warrantyPrice;
        public ImageView icon, deleteBtn;
        public Resources res;


        public MyViewHolder(View view){
            super(view);
            icon = (ImageView) view.findViewById(R.id.garantie_view_laden_bild);
            warrantyShop = (TextView) view.findViewById(R.id.garantie_view_garantiebeginn);
            warrantyEnd = (TextView) view.findViewById(R.id.garantie_view_zusatz_garantieende);
            warrantyPrice = (TextView) view.findViewById(R.id.garantie_view_zusatz_favorite_price);
            deleteBtn = (ImageView) view.findViewById(R.id.garantie_view_garantie_delete_button);
            res = view.getResources();

        }
    }

    /**
     * returned Liste
     * @param bonListe
     */
    public C_Adapter_Garantie(List<C_Bon> bonListe){

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
        Bitmap imageBitmap = S.getShopIcon(holder.res, bon.getShopName());
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(holder.res, imageBitmap);
        roundedBitmapDrawable.setAntiAlias(true);
        holder.icon.setImageDrawable(roundedBitmapDrawable);
        holder.warrantyShop.setText(bon.getShopName());
        holder.warrantyEnd.setText(holder.res.getString(R.string.a_garantie_garantie_bis) + " " + bon.getGuaranteeEnd());
        Log.e("ADAPTER GARANTIE","" + bon.getGuaranteeEnd());

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
}