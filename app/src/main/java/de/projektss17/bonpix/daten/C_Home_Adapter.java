package de.projektss17.bonpix.daten;

import android.icu.text.SimpleDateFormat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;

/**
 * Created by Sascha on 14.05.2017.
 */

public class C_Home_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int count = 2;
    private int counter = 0;

    public class ViewHolderLine extends RecyclerView.ViewHolder {
        LineChart chart;
        LineDataSet dataSet;
        ArrayList<ILineDataSet> lineDataSet;
        LineData lineData;
        View v;

        public ViewHolderLine(View view) {
            super(view);
            Log.e("ADAPTER","VIEWHOLDERLINE START");
            v = view;
            chart = (LineChart) v.findViewById(R.id.chart);

            this.setLineData();
            Log.e("ADAPTER","VIEWHOLDERLINE END");

        }

        public void setLineData(){
            this.lineDataSet = new ArrayList<>();

            // Liste xAchse wird mit Monaten befüllt
            this.dataSet = new LineDataSet(S.dbHandler.getLineData(S.db, 3), "test");
            this.lineDataSet.add(this.dataSet);
            this.lineData = new LineData(this.lineDataSet);

            this.lineData.setValueTextSize(10f);

            if(chart != null){
                this.chart.setTouchEnabled(false);
                this.chart.setData(this.lineData);
                this.chart.invalidate();
            }
        }
    }

    public class ViewHolderBudget extends RecyclerView.ViewHolder {
        TextView title;
        TextView budgetCurrently;
        TextView month;
        TextView year;
        TextView progressPercentage;
        ProgressBar progressBar;
        View v;

        public ViewHolderBudget(View view) {
            super(view);
            Log.e("ADAPTER","VIEWHOLDERBUDGET START");
            v = view;

            title = (TextView) view.findViewById(R.id.budget_title);
            budgetCurrently = (TextView) view.findViewById(R.id.budget_content);
            month = (TextView) view.findViewById(R.id.budget_monat_bis);
            year = (TextView) view.findViewById(R.id.budget_jahr_bis);
            progressBar = (ProgressBar) view.findViewById(R.id.budget_progress_bar_circle);
            progressPercentage = (TextView) view.findViewById(R.id.budget_progress_percentage);

            //TODO Getter und Setter schreiben?!?
            Log.e("ADAPTER","VIEWHOLDERLINE END");


        }

    }

    public C_Home_Adapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        Log.e("ADAPTER","ONCREATEVIEWHOLDER: ViewType:" + viewType);

        switch(viewType){
            case 0:
                Log.e("ADAPTER","ONCREATEVIEWHOLDER: CASE 0 ");
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_home_card_linechart_layout, parent, false);
                return new ViewHolderLine(itemView);
            case 1:
                Log.e("ADAPTER","ONCREATEVIEWHOLDER: CASE 1 ");
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_budget_content, parent, false);
                return new ViewHolderBudget(itemView);

            }
            return null;
        // muss bearbeitet werden
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Log.e("ADAPTER","ONBINDVIEWHOLDER: position:" + position);

        switch (position) {
            case 0:
                counter++;
                Log.e("ADAPTER","ONBINDVIEWHOLDER: CASE0 Counter++: = " + counter);
                break;

            case 1:
                counter++;
                Log.e("ADAPTER","ONBINDVIEWHOLDER: CASE1 Counter++: = " + counter);
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {

        Log.e("ADAPTER","GETITEMVIEWTYPE: position: " + position);

        if (counter == 2) {
            counter = 0;
            Log.e("ADAPTER","GETITEMVIEWTYPE: counter == 2: - RETURN: " + counter);
            return counter;
        }
        else {
            Log.e("ADAPTER","GETITEMVIEWTYPE: counter != 2: - RETURN: " + counter);
            return counter;
        }
    }

    @Override
    public int getItemCount() {
        Log.e("ADAPTER","GETITEMCOUNT: " + count);
        return count;
    }

}
