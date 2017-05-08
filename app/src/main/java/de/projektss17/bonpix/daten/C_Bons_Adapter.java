package de.projektss17.bonpix.daten;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

/**
 * Created by Marcus on 15.04.2017.
 */

public class C_Bons_Adapter extends RecyclerView.Adapter<C_Bons_Adapter.ViewHolder> {


    private List<C_Bon> bonsList;
    private int row_index = -1;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content;
        public ImageView icon;
        public ImageView button;

        public ViewHolder(View view){
            super(view);
            icon = (ImageView) view.findViewById(R.id.imageview_picture);
            title = (TextView) view.findViewById(R.id.view_name);
            content = (TextView) view.findViewById(R.id.view_state);
            button = (ImageView) view.findViewById(R.id.imageview_button);

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
    public void onBindViewHolder(final ViewHolder holder, final int position){
        final C_Bon bon = bonsList.get(position);
        holder.title.setText(bon.getBildpfad());
        holder.content.setText(bon.getLadenname());
        //Loading the FavoriteList
        if (bon.getFav()){
            holder.button.setImageDrawable(holder.button.getContext().getResources().getDrawable(R.drawable.star));
        } else {
            holder.button.setImageDrawable(holder.button.getContext().getResources().getDrawable(R.drawable.star_outline));
        }
        holder.button.setOnClickListener(new View.OnClickListener(){

            /**
             * OnClickListener for the RecyclerView
             * @param v
             */
            @Override
            public void onClick(View v) {
                    row_index = position;
                    // Put the onClick cases here
                    switch (v.getId()) {
                        case R.id.imageview_button: {
                            if (bon.getFav()){
                                holder.button.setImageDrawable(v.getContext().getResources().getDrawable(R.drawable.star_outline));
                                bon.deleteFav();
                            } else {
                                if (row_index == position) {
                                    holder.button.setImageDrawable(v.getContext().getResources().getDrawable(R.drawable.star));
                                    bon.setFav();
                                    Log.i("CLICKEVENT FAV ICON", "### SUCCESS");
                                }
                                else {
                                    Log.e("CLICKEVENT FAV ICON","### COULDNT MATCH POSITION");
                                }
                            }
                        }
                        break;
                    }
            }
        });
    }

    @Override
    public int getItemCount(){
        return bonsList.size();
    }
}