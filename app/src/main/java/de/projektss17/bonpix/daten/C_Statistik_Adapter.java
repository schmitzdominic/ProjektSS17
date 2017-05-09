package de.projektss17.bonpix.daten;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import de.projektss17.bonpix.S;


/**
 * Created by Marcus on 02.05.2017.
 */

public class C_Statistik_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List data = new ArrayList();
    private int counter = 0;

    public class ViewHolderBar extends RecyclerView.ViewHolder {
        BarChart chart;

        public ViewHolderBar(View view) {
            super(view);
            Log.e("### STATISTIK ADAPTER","ViewHolderBar Scoping Chart");
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

    public C_Statistik_Adapter(List testList){
        this.data = testList;
        Log.e("### Stat_Adapter","Constructor reached");
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        if(counter == 1){
            return counter;
        }
        else{
            counter++;
            Log.e("### STATISTIK ADAPTER","getItemViewType" + position % 2 * 2);
            return position % 2 * 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch(viewType){
            case 0:
                Log.e("### STATISTIK ADAPTER","onCreateViewHolder 0");
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
                return new ViewHolderBar(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch(getItemViewType(position)){
            case 0:
                Log.e("### DATABASEHANDLER","onBindViewHolder Bar 0");
                ViewHolderBar holderBar = (ViewHolderBar)holder;
                BarDataSet dataSetBar = new BarDataSet(S.dbHandler.getBarData(1), "test");
                BarData dataBar = new BarData(dataSetBar);
                dataBar.setBarWidth(0.9f); // set custom bar width
                holderBar.chart.setData(dataBar);
                holderBar.chart.setFitBars(true); // make the x-axis fit exactly all bars
                holderBar.chart.invalidate(); // refresh
                notifyDataSetChanged();
                break;
            case 1:
                ViewHolderLine holderLine = (ViewHolderLine)holder;
                LineDataSet setComp1 = new LineDataSet(S.dbHandler.getLineData(1).get("lineOne"), "Company 1");
                setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
                LineDataSet setComp2 = new LineDataSet(S.dbHandler.getLineData(1).get("lineTwo"), "Company 2");
                setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
                List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(setComp1);
                dataSets.add(setComp2);
                LineData data = new LineData(dataSets);
                holderLine.chart.setData(data);
                holderLine.chart.invalidate();
                break;
            case 2:
                ViewHolderPie holderPie = (ViewHolderPie)holder;
                PieDataSet set = new PieDataSet(S.dbHandler.getPieData(1), "Election Results");
                PieData pieData = new PieData(set);
                holderPie.chart.setData(pieData);
                holderPie.chart.invalidate();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}