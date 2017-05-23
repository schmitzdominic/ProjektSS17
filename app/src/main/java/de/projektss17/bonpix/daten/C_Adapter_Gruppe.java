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
import de.projektss17.bonpix.S;

/**
 * Created by SemperFi on 18.05.2017.
 */

public class C_Adapter_Gruppe extends RecyclerView.Adapter<C_Adapter_Gruppe.MyViewHolder> {

    private List<C_Gruppe> gruppenListe;
    private C_Gruppe gruppe;

    public C_Adapter_Gruppe(List<C_Gruppe> gruppenListe){

        this.gruppenListe = gruppenListe;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.box_gruppen_view_layout, parent, false);

        return new C_Adapter_Gruppe.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        this.gruppe = gruppenListe.get(position);

        //ToDo später Icon dynamisch zuweisbar
        //holder.icon.setImageDrawable(rounderBitmapDrawable);
        holder.groupName.setText(gruppe.getName());

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {


            /**
             * On Click Methode für onClickListener
             * @param v
             */
            public void onClick(View v) {

                gruppenListe.get(position).setName("");
                gruppenListe.remove(position);

                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());

            }
        });

    }

    @Override
    public int getItemCount() {
        return this.gruppenListe.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName, groupState;
        public ImageView icon, deleteBtn;


        public MyViewHolder(View view){
            super(view);
            icon = (ImageView) view.findViewById(R.id.recyclergruppen_picture);
            groupName = (TextView) view.findViewById(R.id.recyclergruppen_name);
            groupState = (TextView) view.findViewById(R.id.recyclergruppen_state);
            deleteBtn = (ImageView) view.findViewById(R.id.recyclergruppen_button_del);


            // TODO: Derzeit ist das "group_black" Icon fest eingebunden in die RecyclerViewList. Später soll der User selbst entscheiden welches Logo angegeben wird.
            Bitmap imageBitmap = BitmapFactory.decodeResource(view.getResources(),  R.mipmap.ic_group_black_24dp);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(view.getResources(), imageBitmap);
            roundedBitmapDrawable.setAntiAlias(true);
            icon.setImageDrawable(roundedBitmapDrawable);
            // --- END TO DO ---


        }
    }
}