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
import de.projektss17.bonpix.S;

public class C_Home_Adapter extends RecyclerView.Adapter<C_Home_Adapter.ViewHolder> {


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
            Bitmap imageBitmap = BitmapFactory.decodeResource(view.getResources(),  R.mipmap.ic_aldisuedlogo);
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
    public C_Home_Adapter(List<C_Bon> bonsList){
        this.bonsList = bonsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_home_bonsview_layout_small, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        final C_Bon bon = bonsList.get(position);
        holder.title.setText(bon.getShopName());
        holder.content.setText(bon.getDate());
        //Loading the FavoriteList
        if (bon.getFavourite()){
            holder.button.setImageDrawable(holder.button.getContext().getResources().getDrawable(R.drawable.star));
            holder.button.setColorFilter(holder.button.getContext().getResources().getColor(R.color.colorPrimary));
        } else {
            holder.button.setImageDrawable(holder.button.getContext().getResources().getDrawable(R.drawable.star_outline));
            holder.button.setColorFilter(holder.button.getContext().getResources().getColor(R.color.colorPrimary));
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
                        if (bon.getFavourite()){
                            holder.button.setImageDrawable(v.getContext().getResources().getDrawable(R.drawable.star_outline));
                            holder.button.setColorFilter(v.getContext().getResources().getColor(R.color.colorPrimary));
                            bon.setFavourite(false);
                            S.dbHandler.updateBon(S.db, bon);
                        } else {
                            if (row_index == position) {
                                holder.button.setImageDrawable(v.getContext().getResources().getDrawable(R.drawable.star));
                                holder.button.setColorFilter(v.getContext().getResources().getColor(R.color.colorPrimary));
                                bon.setFavourite(true);
                                S.dbHandler.updateBon(S.db, bon);
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