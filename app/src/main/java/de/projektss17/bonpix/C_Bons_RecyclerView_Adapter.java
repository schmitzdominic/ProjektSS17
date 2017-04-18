package de.projektss17.bonpix;


import android.app.LauncherActivity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sascha on 18.04.2017.
 */

public class C_Bons_RecyclerView_Adapter extends RecyclerView.Adapter<C_Bons_RecyclerView_Adapter.C_Bons_RecyclerView_Holder>{

    private List<C_Recyclerview_List_Item> listData;
    private LayoutInflater inflater;

    public C_Bons_RecyclerView_Adapter (List<C_Recyclerview_List_Item> listData, Context c){

        this.inflater = LayoutInflater.from(c);
        this.listData = listData;
    }


    @Override
    public C_Bons_RecyclerView_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.box_recyclerview_list_item, parent, false);
        return new C_Bons_RecyclerView_Holder(view);
    }

    @Override
    public void onBindViewHolder(C_Bons_RecyclerView_Holder holder, int position) {
        C_Recyclerview_List_Item item = listData.get(position);
        holder.title.setText(item.getTitle());
        holder.icon.setImageResource(item.getImageResId());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class C_Bons_RecyclerView_Holder extends RecyclerView.ViewHolder{

        private TextView title;
        private ImageView icon;
        private ImageView icon2;
        private View container;

        public C_Bons_RecyclerView_Holder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.item_text);
            icon = (ImageView)itemView.findViewById(R.id.supermarkticon);
            icon2 = (ImageView)itemView.findViewById(R.id.favoritenstern);
            container = itemView.findViewById(R.id.cont_item_root);
        }

    }



}
