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
    private int count = 3;
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
            month = (TextView) view.findViewById(R.id.budget_turnus);
            year = (TextView) view.findViewById(R.id.budget_year);
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

        switch(getItemViewType(position)) {
            case 0:
                ViewHolderTime holderTime = (ViewHolderTime)holder;
                long date = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
                String dateString = sdf.format(date);
                holderTime.time.setText(dateString);
                counter++;
                break;
                /*ViewHolderBar holderBar = (ViewHolderBar)holder;
                BarDataSet dataSetBar = new BarDataSet(S.dbHandler.getBarData(1), "test");
                BarData dataBar = new BarData(dataSetBar);
                dataBar.setBarWidth(0.9f); // set custom bar width
                holderBar.chart.setData(dataBar);
                holderBar.chart.setFitBars(true); // make the x-axis fit exactly all bars
                holderBar.chart.invalidate(); // refresh
                counter++;
                break;*/
            case 1:
                ViewHolderBudget holderBudget = (ViewHolderBudget)holder;
                counter++;
                break;
            case 2:
                ViewHolderLine holderLine = (ViewHolderLine)holder;
                ArrayList <ILineDataSet> daten = new ArrayList<>();

                // Liste xAchse wird mit Monaten befüllt
                befuelleListe();

                LineDataSet dataSet = new LineDataSet( S.dbHandler.getLineData(1), "test");
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
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (counter == 3) {
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

    public void befuelleListe(){
        xAchse.add("Januar");
        xAchse.add("Februar");
        xAchse.add("März");
        xAchse.add("April");
        xAchse.add("Mai");
        xAchse.add("Juni");
        xAchse.add("Juli");
        xAchse.add("August");
        xAchse.add("September");
        xAchse.add("Oktober");
        xAchse.add("November");
        xAchse.add("Dezember");
    }
}
