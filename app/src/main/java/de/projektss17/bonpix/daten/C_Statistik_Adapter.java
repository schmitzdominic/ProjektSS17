package de.projektss17.bonpix.daten;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;


/**
 * Created by Marcus on 02.05.2017.
 */

public class C_Statistik_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int count = 8;
    private int counter = 0;


    public class ViewHolderTopProducts extends RecyclerView.ViewHolder {

        public TextView produkt1, produkt2, produkt3, percentage1, percentage2, percentage3;
        public ProgressBar progress1, progress2, progress3;

        public ViewHolderTopProducts(View view) {
            super(view);

            // Implementierung des Layouts der einzelnen Objekte für die CardView
            this.produkt1 = (TextView) view.findViewById(R.id.statistik_card_topproducts_article_one_title);
            this.produkt2 = (TextView) view.findViewById(R.id.statistik_card_topproducts_article_two_title);
            this.produkt3 = (TextView) view.findViewById(R.id.statistik_card_topproducts_article_three_title);
            this.percentage1 = (TextView) view.findViewById(R.id.statistik_card_topproducts_article_one_percentage);
            this.percentage2 = (TextView) view.findViewById(R.id.statistik_card_topproducts_article_two_percentage);
            this.percentage3 = (TextView) view.findViewById(R.id.statistik_card_topproducts_article_three_percentage);

            this.progress1 = (ProgressBar)view.findViewById(R.id.statistik_card_topproducts_article_one_progressbar);
            this.progress2 = (ProgressBar)view.findViewById(R.id.statistik_card_topproducts_article_two_progressbar);
            this.progress3 = (ProgressBar)view.findViewById(R.id.statistik_card_topproducts_article_three_progressbar);
        }
    }


    public class ViewHolderGeneral extends RecyclerView.ViewHolder {

        public TextView anzahlScans, ausgabenGesamt, anzahlLaeden, anzahlArtikel;

        public ViewHolderGeneral(View view) {
            super(view);

            // Implementierung des Layouts der einzelnen Objekte für die CardView
            this.anzahlScans = (TextView) view.findViewById(R.id.statistik_card_general_scans_content);
            this.ausgabenGesamt = (TextView) view.findViewById(R.id.statistik_card_general_costs_content);
            this.anzahlLaeden = (TextView) view.findViewById(R.id.statistik_card_marketamount_content);
            this.anzahlArtikel = (TextView) view.findViewById(R.id.statistik_card_productamount_content);

        }
    }

    public class ViewHolderBar extends RecyclerView.ViewHolder {
        BarChart chart;

        public ViewHolderBar(View view) {
            super(view);
            chart = (BarChart) view.findViewById(R.id.chart);
        }
    }

    public class ViewHolderLine extends RecyclerView.ViewHolder {
        LineChart chart1;

        public ViewHolderLine(View view) {
            super(view);
            chart1 = (LineChart) view.findViewById(R.id.chart1);
        }
    }


    public class ViewHolderPie extends RecyclerView.ViewHolder {
        PieChart chart2;

        public ViewHolderPie(View view) {
            super(view);
            chart2 = (PieChart) view.findViewById(R.id.chart2);
        }
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
            case 3:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_statistik_card_general_layout, parent, false);
                return new ViewHolderGeneral(itemView);
            case 4:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_statistik_card_top_products_layout, parent, false);
                return new ViewHolderTopProducts(itemView);

        }
        return null;
    }


    @Override
    public int getItemViewType(int position) {
        if (counter == 5){
            counter = 0;
            return counter;
        }
        else {
            return counter;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch(getItemViewType(position)){
            case 0:
                Log.e("### DATABASEHANDLER","## onBind 0 BAR");
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
                Log.e("### DATABASEHANDLER","## onBind 1 LINE");
                ViewHolderLine holderLine = (ViewHolderLine)holder;
                LineDataSet setComp1 = new LineDataSet(S.dbHandler.getLineData(1).get("lineOne"), "Company 1");
                setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
                LineDataSet setComp2 = new LineDataSet(S.dbHandler.getLineData(1).get("lineTwo"), "Company 2");
                setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
                List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(setComp1);
                dataSets.add(setComp2);
                LineData data = new LineData(dataSets);
                holderLine.chart1.setData(data);
                holderLine.chart1.invalidate();
                counter++;
                break;
            case 2:
                Log.e("### DATABASEHANDLER","## onBind 2 PIE");
                ViewHolderPie holderPie = (ViewHolderPie)holder;
                PieDataSet set = new PieDataSet(S.dbHandler.getPieData(1), "Election Results");
                PieData pieData = new PieData(set);
                holderPie.chart2.setData(pieData);
                holderPie.chart2.invalidate();
                counter++;
                break;
            case 3:
                ViewHolderGeneral holderGeneral = (ViewHolderGeneral)holder;
                holderGeneral.anzahlScans.setText("20");
                holderGeneral.ausgabenGesamt.setText("10303");
                holderGeneral.anzahlArtikel.setText("304040");
                holderGeneral.anzahlLaeden.setText("45");
                counter++;
                break;
            case 4:
                ViewHolderTopProducts holderTopProducts = (ViewHolderTopProducts)holder;
                holderTopProducts.produkt1.setText("Videospiele");
                holderTopProducts.produkt2.setText("Getränke");
                holderTopProducts.produkt3.setText("Steaks");
                holderTopProducts.progress1.setProgress(45);
                holderTopProducts.progress2.setProgress(35);
                holderTopProducts.progress3.setProgress(20);
                holderTopProducts.percentage1.setText("45 %");
                holderTopProducts.percentage2.setText("35 %");
                holderTopProducts.percentage3.setText("20 %");
                counter++;
                break;
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }



/*
    private int count = 8;
    private int counter = 0;

    public class ViewHolderBar extends RecyclerView.ViewHolder {
        BarChart chart;

        public ViewHolderBar(View view) {
            super(view);
            chart = (BarChart) view.findViewById(R.id.chart);
        }
    }

    public class ViewHolderLine extends RecyclerView.ViewHolder {
        LineChart chart1;

        public ViewHolderLine(View view) {
            super(view);
            chart1 = (LineChart) view.findViewById(R.id.chart1);
        }
    }

    public class ViewHolderPie extends RecyclerView.ViewHolder {
        PieChart chart2;

        public ViewHolderPie(View view) {
            super(view);
            chart2 = (PieChart) view.findViewById(R.id.chart2);
        }
    }

    public class ViewHolderGeneral extends RecyclerView.ViewHolder{
        View chart3;

        ViewHolderGeneral(View view){
            super(view);
            chart3 = (View)view.findViewById(R.layout.box_statistik_card_general_layout);
        }
    }

    public C_Statistik_Adapter(){
    }

    @Override
    public int getItemViewType(int position) {
        if (counter == 3){
            counter = 0;
            return counter;
        }
        else {
            return counter;
        }
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
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolderBar holderBar = (ViewHolderBar)holder;
        BarDataSet dataSetBar = new BarDataSet(S.dbHandler.getBarData(1), "test");
        BarData dataBar = new BarData(dataSetBar);
        dataBar.setBarWidth(0.9f); // set custom bar width
        holderBar.chart.setData(dataBar);
        holderBar.chart.setFitBars(true); // make the x-axis fit exactly all bars
        holderBar.chart.invalidate();


        /*
        switch(getItemViewType(position)){
            case 0:
                Log.e("### DATABASEHANDLER","## onBind 0 BAR");
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
                Log.e("### DATABASEHANDLER","## onBind 1 LINE");
                ViewHolderLine holderLine = (ViewHolderLine)holder;
                LineDataSet setComp1 = new LineDataSet(S.dbHandler.getLineData(1).get("lineOne"), "Company 1");
                setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
                LineDataSet setComp2 = new LineDataSet(S.dbHandler.getLineData(1).get("lineTwo"), "Company 2");
                setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
                List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(setComp1);
                dataSets.add(setComp2);
                LineData data = new LineData(dataSets);
                holderLine.chart1.setData(data);
                holderLine.chart1.invalidate();
                counter++;
                break;
            case 2:
                Log.e("### DATABASEHANDLER","## onBind 2 PIE");
                ViewHolderPie holderPie = (ViewHolderPie)holder;
                PieDataSet set = new PieDataSet(S.dbHandler.getPieData(1), "Election Results");
                PieData pieData = new PieData(set);
                holderPie.chart2.setData(pieData);
                holderPie.chart2.invalidate();
                counter++;
                break;
        }
    }

    @Override
    public int getItemCount() {
        return count;
    } */
}