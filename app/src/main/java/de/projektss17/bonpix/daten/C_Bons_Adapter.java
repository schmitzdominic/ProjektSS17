package de.projektss17.bonpix.daten;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.projektss17.bonpix.R;

/**
 * Created by Marcus on 15.04.2017.
 */

public class C_Bons_Adapter extends RecyclerView.Adapter<C_Bons_Adapter.ViewHolder> {


    private List<C_Bons> bonsList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content;

        public ViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.view_name);
            content = (TextView) view.findViewById(R.id.view_state);
        }
    }

    public C_Bons_Adapter(List<C_Bons> bonsList){
        this.bonsList = bonsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_bons_view_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        C_Bons bon = bonsList.get(position);
        holder.title.setText(bon.getId());
        holder.content.setText(bon.getName());
    }

    @Override
    public int getItemCount(){
        return bonsList.size();
    }
}
