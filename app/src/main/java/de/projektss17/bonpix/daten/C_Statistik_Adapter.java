package de.projektss17.bonpix.daten;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

import de.projektss17.bonpix.R;


/**
 * Created by Marcus on 02.05.2017.
 */

public class C_Statistik_Adapter extends RecyclerView.Adapter<C_Statistik_Adapter.ViewHolder> {

    private List data;

    public class ViewHolder extends RecyclerView.ViewHolder {
        BarChart chart;

        public ViewHolder(View view) {
            super(view);
            chart = (BarChart) view.findViewById(R.id.chart);
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
        BarDataSet dataSet = new BarDataSet(data, "test");

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f); // set custom bar width
        holder.chart.setData(data);
        holder.chart.setFitBars(true); // make the x-axis fit exactly all bars
        holder.chart.invalidate(); // refresh
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}