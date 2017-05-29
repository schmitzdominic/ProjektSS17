package de.projektss17.bonpix.daten;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;

public class C_Adapter_Bons extends RecyclerView.Adapter<C_Adapter_Bons.ViewHolder> {


    private List<C_Bon> bonsList;
    private List<C_Bon> filteredList;
    private int row_index = -1;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content, price;
        public ImageView icon;
        public ImageView button;
        public Resources res;

        public ViewHolder(View view){
            super(view);
            icon = (ImageView) view.findViewById(R.id.imageview_picture);
            title = (TextView) view.findViewById(R.id.view_name);
            content = (TextView) view.findViewById(R.id.view_state);
            price = (TextView) view.findViewById(R.id.view_total_price);
            button = (ImageView) view.findViewById(R.id.imageview_button);
            res = view.getResources();

        }
    }

    /**
     * Constructor
     * @param bonsList
     */
    public C_Adapter_Bons(List<C_Bon> bonsList){
        this.bonsList = bonsList;
        this.filteredList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_bons_view_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        final C_Bon bon = bonsList.get(position);

        Bitmap imageBitmap = S.getShopIcon(holder.res, bon.getShopName());
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(holder.res, imageBitmap);
        roundedBitmapDrawable.setAntiAlias(true);
        holder.icon.setImageDrawable(roundedBitmapDrawable);

        holder.title.setText(bon.getShopName());
        holder.content.setText(S.getWeekday(holder.res, S.getWeekdayNumber(bon.getDate())) + "\n" + bon.getDate());
        holder.price.setText(String.format("%s €", bon.getTotalPrice().replace(".", ",")));
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

    public void setFilter(List<C_Bon> passedList) {
        bonsList = new ArrayList<>();
        bonsList.addAll(passedList);
        notifyDataSetChanged();
    }
}