package de.projektss17.bonpix.daten;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;
import de.projektss17.bonpix.fragments.F_Laeden_Detail;

import java.util.ArrayList;
import java.util.List;


public class C_Laeden_Adapter extends RecyclerView.Adapter<C_Laeden_Adapter.MyViewHolder> {

    private ArrayList<C_Laden> shopList = new ArrayList<>();
    private C_Laden shop;
    private F_Laeden_Detail trigger;
    private Context context;
    private FragmentManager fm;
    private Bundle args;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView shopName;
        public ImageView iconShop, editShopBtn;
        public ArrayList<String> supShops;
        public Resources res;

        public MyViewHolder(View view) {
            super(view);
            iconShop = (ImageView) view.findViewById(R.id.laeden_view_laden_bild);
            shopName = (TextView) view.findViewById(R.id.laeden_view_laden_name);
            editShopBtn = (ImageView) view.findViewById(R.id.laeden_view_edit_button);
            String[] shops = view.getResources().getStringArray(R.array.defaultLaeden);
            res = view.getResources();

            supShops = new ArrayList<>();

            for(String x : shops){
                supShops.add(x);
            }
        }
    }
    /**
     * @param shopList
     */
    public C_Laeden_Adapter(Context context, ArrayList<C_Laden> shopList) {
        this.context = context;
        this.shopList = shopList;
    }
    @Override
    public C_Laeden_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.box_laeden_view, parent, false);

        //Instanz des Fragments
        trigger = new F_Laeden_Detail();
        fm = ((Activity) context).getFragmentManager();

        return new C_Laeden_Adapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final C_Laeden_Adapter.MyViewHolder holder, final int position) {
        this.shop = shopList.get(position);

        Bitmap imageBitmap = S.getShopIcon(holder.res, this.shop.getName());
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(holder.res, imageBitmap);
        roundedBitmapDrawable.setAntiAlias(true);
        holder.iconShop.setImageDrawable(roundedBitmapDrawable);

        holder.shopName.setText(shop.getName());

        //Bei Supported Shops darf der Editierbutton nicht erscheinen => Wird unsichtbar gemacht
        if(holder.supShops.contains(shop.getName())){
            holder.editShopBtn.setVisibility(View.INVISIBLE);
        } else {
            holder.editShopBtn.setVisibility(View.VISIBLE);
        }
        holder.editShopBtn.setOnClickListener(new View.OnClickListener() {

            /**
             * On Click Methode für onClickListener
             * @param v
             */
            public void onClick(View v) {

                C_Laden shopA = shopList.get(position);
                //Übergabe Name des angeklickten Shops => an F_Laeden_Detail
                args = new Bundle();
                args.clear();
                args.putString("ShopName", shopA.getName());
                trigger.setArguments(args);
                trigger.show(fm , "BlaBla");
            }
        });
    }
    @Override
    public int getItemCount() {
        return this.shopList.size();
    }

    public void setFilter(List<C_Laden> passedList) {
        shopList = new ArrayList<>();
        shopList.addAll(passedList);
        notifyDataSetChanged();
    }
}