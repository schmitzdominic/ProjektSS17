package de.projektss17.bonpix.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;
import de.projektss17.bonpix.daten.C_Laden;
import de.projektss17.bonpix.fragments.F_Laeden_Detail;

import java.util.ArrayList;
import java.util.List;


public class C_Adapter_Laeden extends RecyclerView.Adapter<C_Adapter_Laeden.MyViewHolder> {

    private ArrayList<C_Laden> shopList = new ArrayList<>();
    private C_Laden shop;
    private F_Laeden_Detail trigger;
    private Context context;
    private FragmentManager fm;
    private Bundle args;

    public C_Adapter_Laeden(Context context, ArrayList<C_Laden> shopList) {
        this.context = context;
        this.shopList = shopList;
    }

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

    @Override
    public C_Adapter_Laeden.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.box_laeden_view, parent, false);
        trigger = new F_Laeden_Detail();
        fm = ((Activity) context).getFragmentManager();
        return new C_Adapter_Laeden.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final C_Adapter_Laeden.MyViewHolder holder, final int position) {
        this.shop = shopList.get(position);

        holder.iconShop.setImageBitmap(S.getShopIcon(holder.res, shop.getName()));
        holder.shopName.setText(shop.getName());

        if(holder.supShops.contains(shop.getName())){           // Bei Supported Shops darf der Editierbutton nicht erscheinen => Wird unsichtbar gemacht
            holder.editShopBtn.setVisibility(View.INVISIBLE);
        } else {
            holder.editShopBtn.setVisibility(View.VISIBLE);
        }
        holder.editShopBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {                       // Passing the ShopName to F_Laeden_Detail
                C_Laden shopA = shopList.get(position);
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

    /**
     * Set the Adapter List to the passed List
     * Passed List contains the search objects
     * @param passedList
     */
    public void setFilter(List<C_Laden> passedList) {
        shopList = new ArrayList<>();
        shopList.addAll(passedList);
        notifyDataSetChanged();
    }
}