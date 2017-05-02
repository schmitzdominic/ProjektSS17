package de.projektss17.bonpix.daten;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.projektss17.bonpix.R;


/**
 * Created by Marcus on 02.05.2017.
 */

public class C_Statistik_Adapter extends RecyclerView.Adapter<C_Statistik_Adapter.ViewHolder> {

    private List data;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            //textView = (TextView) view.findViewById(R.id.testTextview);
        }
    }

    public C_Statistik_Adapter(List pass){
        this.data = pass;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_statistik_card_layout, parent, false);
        ViewHolder holder = new ViewHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText("hallo 123 TEST");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
