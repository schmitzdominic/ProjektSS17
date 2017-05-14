package de.projektss17.bonpix.daten;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;

/**
 * Created by Sascha on 14.05.2017.
 */

public class C_Home_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private int count = 2;
    private int counter = 0;

    public class ViewHolderBar extends RecyclerView.ViewHolder {
        BarChart chart;

        public ViewHolderBar(View view) {
            super(view);
            Log.e("### Home Adapter", "ViewHolderBar Scoping Chart");
            chart = (BarChart) view.findViewById(R.id.chart);
        }
    }

    public class ViewHolderBudget extends  RecyclerView.ViewHolder {
        TextView title;
        TextView budgetCurrently;
        TextView month;
        TextView year;
        TextView progressPercentage;
        ProgressBar progressBar;

        public ViewHolderBudget(View view){
            super(view);

            title = (TextView) view.findViewById(R.id.budget_title);
            budgetCurrently = (TextView) view.findViewById(R.id.budget_content);
            month = (TextView) view.findViewById(R.id.budget_turnus);
            year = (TextView) view.findViewById(R.id.budget_year);
            progressBar = (ProgressBar)view.findViewById(R.id.budget_progress_bar_circle);
            progressPercentage = (TextView) view.findViewById(R.id.budget_progress_percentage);

            //TODO Getter und Setter schreiben?!?


        }

    }

    public C_Home_Adapter(){
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch(viewType){
            case 0:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_statistik_card_bar_layout, parent, false);
                return new ViewHolderBar(itemView);
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
                ViewHolderBar holderBar = (ViewHolderBar)holder;
                BarDataSet dataSetBar = new BarDataSet(S.dbHandler.getBarData(1), "test");
                BarData dataBar = new BarData(dataSetBar);
                dataBar.setBarWidth(0.9f); // set custom bar width
                holderBar.chart.setData(dataBar);
                holderBar.chart.setFitBars(true); // make the x-axis fit exactly all bars
                holderBar.chart.invalidate(); // refresh
                counter++;
                break;
            case 1:
                ViewHolderBudget holderBudget = (ViewHolderBudget) holder;
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
