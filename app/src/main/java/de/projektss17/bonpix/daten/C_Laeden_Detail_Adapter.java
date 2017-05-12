package de.projektss17.bonpix.daten;

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

/**
 * Created by Fabian on 11.05.2017.
 */

public class C_Laeden_Detail_Adapter extends RecyclerView.Adapter<C_Laeden_Detail_Adapter.MyViewHolder>{

    private ArrayList<C_Bon> bonListe;
    private C_Bon bon;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView shopName, bonDate;
        public ImageView shopIcon;


        public MyViewHolder(View view){
            super(view);
            shopIcon = (ImageView) view.findViewById(R.id.laeden_detail_view_shop_icon);
            shopName = (TextView) view.findViewById(R.id.laeden_detail_view_shop_name);
            bonDate = (TextView) view.findViewById(R.id.laeden_detail_view_bon_date);

            // TODO: Derzeit ist das "Aldi" Icon fest eingebunden in die RecyclerViewList. Dies muss geändert werden, sobald die RecyclerViewList dynamisch befüllt wird. (derzeit feste test werte, später Aldi, Lidl etc Logo je nach Bon)
            Bitmap imageBitmap = BitmapFactory.decodeResource(view.getResources(),  R.mipmap.icon_laden_aldi_24dp);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(view.getResources(), imageBitmap);
            roundedBitmapDrawable.setCircular(true);
            roundedBitmapDrawable.setAntiAlias(true);
            shopIcon.setImageDrawable(roundedBitmapDrawable);
            // --- END TO DO ---


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
        holder.shopName.setText(bon.getShopName());
        holder.bonDate.setText(bon.getDate());
    }
    @Override
    public int getItemCount() {
        return this.bonListe.size();
    }
}