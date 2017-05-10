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

import de.projektss17.bonpix.R;

import java.util.List;

public class C_Laeden_Adapter extends RecyclerView.Adapter<C_Laeden_Adapter.MyViewHolder> {

    private List<C_Laden> shopList;
    private C_Laden shop;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView shopName;
        public ImageView iconShop, editShopBtn;


        public MyViewHolder(View view) {
            super(view);
            iconShop = (ImageView) view.findViewById(R.id.laeden_view_laden_bild);
            shopName = (TextView) view.findViewById(R.id.laeden_view_laden_name);
            editShopBtn = (ImageView) view.findViewById(R.id.laeden_view_edit_button);


            // TODO: Derzeit ist das "Aldi" Icon fest eingebunden in die RecyclerViewList. Dies muss geändert werden, sobald die RecyclerViewList dynamisch befüllt wird. (derzeit feste test werte, später Aldi, Lidl etc Logo je nach Bon)
            Bitmap imageBitmap = BitmapFactory.decodeResource(view.getResources(), R.mipmap.icon_laden_aldi_24dp);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(view.getResources(), imageBitmap);
            roundedBitmapDrawable.setCircular(true);
            roundedBitmapDrawable.setAntiAlias(true);
            iconShop.setImageDrawable(roundedBitmapDrawable);
            // --- END TO DO ---


        }
    }

    /**
     * @param shopList
     */
    public C_Laeden_Adapter(List<C_Laden> shopList) {

        this.shopList = shopList;
    }


    @Override
    public C_Laeden_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.box_laeden_view, parent, false);

        return new C_Laeden_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final C_Laeden_Adapter.MyViewHolder holder, final int position) {
        this.shop = shopList.get(position);

        //ToDo später Icon dynamisch zuweisbar
        //holder.icon.setImageDrawable(rounderBitmapDrawable);
        holder.shopName.setText(shop.getName());

        holder.editShopBtn.setOnClickListener(new View.OnClickListener() {


            /**
             * On Click Methode für onClickListener
             * @param v
             */
            public void onClick(View v) {
                Log.e("test", "test");
                //bonListe.get(position).setGuarantee(false);
                //S.dbHandler.updateBon(S.db, bonListe.get(position));
                //bonListe.remove(position);

                //notifyItemRemoved(position);
                //notifyItemRangeChanged(position, getItemCount());

            }
        });


    }

    @Override
    public int getItemCount() {

        return this.shopList.size();
    }
}