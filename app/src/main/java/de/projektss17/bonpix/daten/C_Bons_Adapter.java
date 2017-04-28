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
 * Created by Marcus on 15.04.2017.
 */

public class C_Bons_Adapter extends RecyclerView.Adapter<C_Bons_Adapter.ViewHolder> {


    private List<C_Bon> bonsList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content;
        public ImageView icon;

        public ViewHolder(View view){
            super(view);
            icon = (ImageView) view.findViewById(R.id.imageview_picture);
            title = (TextView) view.findViewById(R.id.view_name);
            content = (TextView) view.findViewById(R.id.view_state);

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
     * Constructor
     * @param bonsList
     */
    public C_Bons_Adapter(List<C_Bon> bonsList){
        this.bonsList = bonsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_bons_view_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        C_Bon bon = bonsList.get(position);

        //holder.icon.setImageDrawable(rounderBitmapDrawable);
        holder.title.setText(bon.getLadenname());
        holder.content.setText(bon.getDatum());
    }

    @Override
    public int getItemCount(){
        return bonsList.size();
    }
}