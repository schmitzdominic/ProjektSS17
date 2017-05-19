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
    private ArrayList xAchse = new ArrayList();

    public class ViewHolderLine extends RecyclerView.ViewHolder {
        LineChart chart;

        public ViewHolderLine(View view) {
            super(view);
            Log.e("### Home Adapter", "ViewHolderBar Scoping Chart");
            chart = (LineChart) view.findViewById(R.id.chart);
        }
    }

    public class ViewHolderBudget extends RecyclerView.ViewHolder {
        TextView title;
        TextView budgetCurrently;
        TextView month;
        TextView year;
        TextView progressPercentage;
        ProgressBar progressBar;

        public ViewHolderBudget(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.budget_title);
            budgetCurrently = (TextView) view.findViewById(R.id.budget_content);
            month = (TextView) view.findViewById(R.id.budget_monat_bis);
            year = (TextView) view.findViewById(R.id.budget_jahr_bis);
            progressBar = (ProgressBar) view.findViewById(R.id.budget_progress_bar_circle);
            progressPercentage = (TextView) view.findViewById(R.id.budget_progress_percentage);

            //TODO Getter und Setter schreiben?!?


        }

    }

    public class ViewHolderTime extends RecyclerView.ViewHolder {
        TextView time;





        public ViewHolderTime(View view) {
            super(view);
            Log.e("### Home Adapter", "ViewHolderTime Scoping time");
            time = (TextView) view.findViewById(R.id.time);


        }

    }

    public C_Home_Adapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch(viewType){
            case 0:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_home_card_linechart_layout, parent, false);
                return new ViewHolderLine(itemView);
            case 1:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_budget_content, parent, false);
                return new ViewHolderBudget(itemView);

        }
        return null;

        // muss bearbeitet werden
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case 0:
                ViewHolderLine holderLine = (ViewHolderLine) holder;
                ArrayList<ILineDataSet> daten = new ArrayList<>();

                // Liste xAchse wird mit Monaten befüllt
                LineDataSet dataSet = new LineDataSet(S.dbHandler.getLineData(S.db, 3), "test");
                daten.add(dataSet);
                LineData data = new LineData(daten);

                data.setValueTextSize(10f);

                holderLine.chart.setTouchEnabled(false);
                holderLine.chart.setData(data);

                holderLine.chart.invalidate();

                //LineData dataLine = new LineData(data);
                /*dataLine.setLineWidth(0.9f); // set custom bar width
                holderLine.chart.setData(dataLine);
                holderLine.chart.setFitBars(true); // make the x-axis fit exactly all bars
                holderLine.chart.invalidate(); // refresh*/
                counter++;
                break;

            case 1:
                ViewHolderBudget holderBudget = (ViewHolderBudget) holder;
                counter++;
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (counter == 2) {
            counter = 0;
            return counter;
        }
        else {
            return counter;
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

}
