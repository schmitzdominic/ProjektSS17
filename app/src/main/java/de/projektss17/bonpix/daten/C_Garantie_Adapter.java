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

import java.util.List;

import de.projektss17.bonpix.R;

/**
 * Created by Fabian on 25.04.2017.
 */

public class C_Garantie_Adapter extends RecyclerView.Adapter<C_Garantie_Adapter.MyViewHolder> {

    private List<C_Bons> bonListe;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView garantieBegin, garantieEnde;
        public ImageView icon;

        public MyViewHolder(View view){
            super(view);
            icon = (ImageView) view.findViewById(R.id.garantie_view_laden_bild);
            garantieBegin = (TextView) view.findViewById(R.id.garantie_view_garantiebeginn);
            garantieEnde = (TextView) view.findViewById(R.id.garantie_view_zusatz_garantieende);

            // TODO: Derzeit ist das "REWE" Icon fest eingebunden in die RecyclerViewList. Dies muss geändert werden, sobald die RecyclerViewList dynamisch befüllt wird. (derzeit feste test werte, später Aldi, Lidl etc Logo je nach Bon)
            Bitmap imageBitmap = BitmapFactory.decodeResource(view.getResources(),  R.mipmap.icon_laden_rewe_24dp);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(view.getResources(), imageBitmap);
            roundedBitmapDrawable.setCircular(true);
            roundedBitmapDrawable.setAntiAlias(true);
            icon.setImageDrawable(roundedBitmapDrawable);
            // --- END TO DO ---
        }
    }

    /**
     * returned Liste
     * @param bonListe
     */
    public C_Garantie_Adapter(List<C_Bons> bonListe){

        this.bonListe = bonListe;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.box_garantie_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(C_Garantie_Adapter.MyViewHolder holder, int position) {
        C_Bons bon = bonListe.get(position);

        //ToDo später Icon dynamisch zuweisbar
        //holder.icon.setImageDrawable(rounderBitmapDrawable);
        holder.garantieBegin.setText("Garantie Anfang: "+ bon.getDatum());
        holder.garantieEnde.setText("Garantie Ende: 12.03.3079"); // TODO DB IMPLEMENTIEREN!
    }




    @Override
    public int getItemCount() {
        return bonListe.size();
    }
}
