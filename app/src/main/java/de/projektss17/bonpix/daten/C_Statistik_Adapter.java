package de.projektss17.bonpix.daten;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;
import java.util.List;

import de.projektss17.bonpix.R;


/**
 * Created by Marcus on 02.05.2017.
 */

public class C_Statistik_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List data = new ArrayList();

    public class ViewHolderBar extends RecyclerView.ViewHolder {
        BarChart chart;

        public ViewHolderBar(View view) {
            super(view);
            chart = (BarChart) view.findViewById(R.id.chart);
        }
    }

    public class ViewHolderLine extends RecyclerView.ViewHolder {
        LineChart chart;

        public ViewHolderLine(View view) {
            super(view);
            chart = (LineChart) view.findViewById(R.id.chart);
        }
    }

    public class ViewHolderPie extends RecyclerView.ViewHolder {
        PieChart chart;

        public ViewHolderPie(View view) {
            super(view);
            chart = (PieChart) view.findViewById(R.id.chart);
        }
    }

    public C_Statistik_Adapter(List pass){
        for (Object row : pass){
            data.add(row);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        return position % 2 * 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch(viewType){
            case 0:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_statistik_card_bar_layout, parent, false);
                return new ViewHolderBar(itemView);
            case 1:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_statistik_card_line_layout, parent, false);
                return new ViewHolderLine(itemView);
            case 2:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_statistik_card_pie_layout, parent, false);
                return new ViewHolderPie(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_statistik_card_bar_layout, parent, false);
                return new ViewHolderPie(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch(getItemViewType(position)){
            case 0:
                final ViewHolderBar holderBar = (ViewHolderBar)holder;
                BarDataSet dataSet = new BarDataSet(data, "test");
                BarData data = new BarData(dataSet);
                data.setBarWidth(0.9f); // set custom bar width
                holderBar.chart.setData(data);
                holderBar.chart.setFitBars(true); // make the x-axis fit exactly all bars
                holderBar.chart.invalidate(); // refresh
                break;
            case 1:
                final ViewHolderLine holderLine = (ViewHolderLine)holder;
                LineDataSet dataSet1 = new LineDataSet(this.data, "test1");
                //LineData data = new LineData(dataSet1);
                holderLine.chart.invalidate();
                break;
            case 2:
                final ViewHolderPie holderPie = (ViewHolderPie)holder;
                PieDataSet dataSet2 = new PieDataSet(this.data, "test2");
                holderPie.chart.invalidate();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}