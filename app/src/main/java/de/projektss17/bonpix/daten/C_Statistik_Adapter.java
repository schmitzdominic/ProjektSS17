package de.projektss17.bonpix.daten;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.SortedSet;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;


/**
 * ReCreated by Johanns on 18.05.2017 - 20.05.2017.
 */

public class C_Statistik_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int count = 6;              // Gibt an wie viele Cards die RecyclerView beinhalten soll (derzeit 6 feste CARDS!)
    public String countBons;
    private String countLaeden;
    private String countArtikel;
    private String gesBetrag;
    private String date[];
    private ViewHolderGeneral holderGeneral;
    private ViewHolderBar holderBar;
    ViewHolderTopProducts holderTopProducts;

    // LAYOUT TopFacts
    public class ViewHolderTopFacts extends RecyclerView.ViewHolder {

        public TextView fact1, fact2, fact3;

        public ViewHolderTopFacts(View view) {
            super(view);

            // Implementierung des Layouts der einzelnen Objekte für die CardView
            this.fact1 = (TextView) view.findViewById(R.id.statistik_card_topfacts_fact_one_content);
            this.fact2 = (TextView) view.findViewById(R.id.statistik_card_topfacts_fact_two_content);
            this.fact3 = (TextView) view.findViewById(R.id.statistik_card_topfacts_fact_three_content);

        }
    }


    // LAYOUT TopProducts
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


    // LAYOUT - Allgemeine Infos
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


    // LAYOUT - Allgemeine Infos
    public class ViewHolderBar extends RecyclerView.ViewHolder {
        BarChart barChart;

        public ViewHolderBar(View view) {
            super(view);
            barChart = (BarChart) view.findViewById(R.id.statistik_card_bar);
        }
    }


    // LAYOUT - LineChart
    public class ViewHolderLine extends RecyclerView.ViewHolder {
        LineChart lineChart;

        public ViewHolderLine(View view) {
            super(view);
            lineChart = (LineChart) view.findViewById(R.id.statistik_card_line);
        }
    }


    // LAYOUT - PieChart
    public class ViewHolderPie extends RecyclerView.ViewHolder {
        PieChart pieChart;

        public ViewHolderPie(View view) {
            super(view);
            pieChart = (PieChart) view.findViewById(R.id.statistik_card_pie);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        switch(viewType){
            case 0:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_statistik_card_general_layout, parent, false);
                return new ViewHolderGeneral(itemView);
            case 1:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_statistik_card_bar_layout, parent, false);
                return new ViewHolderBar(itemView);
            case 2:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_statistik_card_top_products_layout, parent, false);
                return new ViewHolderTopProducts(itemView);
            case 3:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_statistik_card_pie_layout, parent, false);
                return new ViewHolderPie(itemView);
            case 4:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_statistik_card_topfacts_layout, parent, false);
                return new ViewHolderTopFacts(itemView);
            case 5:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_statistik_card_line_layout, parent, false);
                return new ViewHolderLine(itemView);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return position;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        double ausgabenGesamt=0.0;  //Gesamtausgaben des Array bons (Könnte theoretisch auch bei den oberen Attributen stehen)
        int anzahlArtikel = 0;      //Anzahl aller Artikel des Array bons (Könnte theoretisch auch bei den oberen Attributen stehen)
        //createFilteredData();       //WICHTIG: Befüllt die ArrayLists (Läden & Bons) entsprechend der Filterung zur späteren Verarbeitung

        switch(getItemViewType(position)){
            case 0:

                if(holderGeneral == null){
                    holderGeneral = (ViewHolderGeneral)holder;
                }

                holderGeneral.anzahlScans.setText(countBons);
                holderGeneral.anzahlLaeden.setText(countLaeden);
                holderGeneral.anzahlArtikel.setText(countArtikel);
                holderGeneral.ausgabenGesamt.setText(gesBetrag + holderGeneral.itemView.getResources().getString(R.string.waehrung));

                break;
            case 1:

                int MAXVALUE_BAR_CHART = 5;

                if(holderBar == null){
                    holderBar = (ViewHolderBar)holder;
                }

                if(S.dbHandler.getAllBonsCount(S.db) == 0){
                    break;
                }

                List<IBarDataSet> bars = new ArrayList<>();

                if(date != null){

                    for(int i = 0; i < MAXVALUE_BAR_CHART; i++){
                        List<BarEntry> list = S.dbHandler.getBarDataLaedenExpenditure(S.db, date[0],date[1], i);
                        if(S.dbHandler.getBarDataLaden() != null && list.get(0).getY() != 0.0){
                            BarDataSet set = (new BarDataSet(list, S.dbHandler.getBarDataLaden().getName()));

                            switch(i){
                                case 0:
                                    set.setColor(ContextCompat.getColor(holderBar.itemView.getContext(), R.color.colorPrimaryDark));
                                    break;
                                case 1:
                                    set.setColor(ContextCompat.getColor(holderBar.itemView.getContext(), R.color.colorAccent));
                                    break;
                                case 2:
                                    set.setColor(ContextCompat.getColor(holderBar.itemView.getContext(), R.color.colorPrimary));
                                    break;
                                case 3:
                                    set.setColor(ContextCompat.getColor(holderBar.itemView.getContext(), R.color.color4Bar));
                                    break;
                                case 4:
                                    set.setColor(ContextCompat.getColor(holderBar.itemView.getContext(), R.color.color5Bar));
                                    break;

                            }

                            bars.add(set);
                        }
                    }
                }

                BarData dataBar = new BarData(bars);
                Description desc = new Description();
                desc.setText("");
                dataBar.setBarWidth(0.7f); // set custom bar width
                holderBar.barChart.setData(dataBar);
                holderBar.barChart.setDescription(desc);
                holderBar.barChart.getXAxis().setEnabled(false);
                holderBar.barChart.getAxisLeft().setDrawAxisLine(false);
                holderBar.barChart.getAxisRight().setEnabled(false);
                holderBar.barChart.setTouchEnabled(false);
                holderBar.barChart.setDragEnabled(false);
                holderBar.barChart.setScaleEnabled(false);
                holderBar.barChart.setPinchZoom(false);
                holderBar.barChart.setClickable(false);
                holderBar.barChart.setFocusableInTouchMode(false);
                holderBar.barChart.setFitBars(true); // make the x-axis fit exactly all bars
                holderBar.barChart.animateY(1000);
                holderBar.barChart.invalidate(); // refresh*/
                break;
            case 2:

                int ANIMATION_TIME = 1000;

                if(holderTopProducts == null){
                    holderTopProducts = (ViewHolderTopProducts)holder;
                }

                holderTopProducts.progress1.animate();

                SortedSet<Hashtable.Entry<String, Integer>> sortedArticleList = S.dbHandler.getTopArticles(S.db, date[0], date[1]);

                int count = 0;
                int size = 0;

                for(Hashtable.Entry<String, Integer> entry : sortedArticleList){
                    size += entry.getValue();
                }

                for(Hashtable.Entry<String, Integer> entry : sortedArticleList){

                    int progress = (int)((double)entry.getValue()/size*100);
                    double procent = ((double)entry.getValue()/size)*100 > 0 ? ((double)entry.getValue()/size)*100 : 0;
                    ProgressBarAnimation anim;

                    procent = Math.round(procent * 100) / 100.00;
                    DecimalFormat df = new DecimalFormat("#0.00");


                    switch(count){
                        case 0:
                            holderTopProducts.produkt1.setText(entry.getKey());
                            anim = new ProgressBarAnimation(holderTopProducts.progress1, 0, progress);
                            anim.setDuration(ANIMATION_TIME);
                            holderTopProducts.progress1.startAnimation(anim);
                            holderTopProducts.percentage1.setText(df.format(procent) + " %");
                            break;
                        case 1:
                            holderTopProducts.produkt2.setText(entry.getKey());
                            anim = new ProgressBarAnimation(holderTopProducts.progress2, 0, progress);
                            anim.setDuration(ANIMATION_TIME);
                            holderTopProducts.progress2.startAnimation(anim);
                            holderTopProducts.percentage2.setText(df.format(procent) + " %");
                            break;
                        case 2:
                            holderTopProducts.produkt3.setText(entry.getKey());
                            anim = new ProgressBarAnimation(holderTopProducts.progress3, 0, progress);
                            anim.setDuration(ANIMATION_TIME);
                            holderTopProducts.progress3.startAnimation(anim);
                            holderTopProducts.percentage3.setText(df.format(procent) + " %");
                            break;
                    }

                    if(++count == 3){
                        break;
                    }
                }

                break;
            case 3:
                /*ViewHolderPie holderPie = (ViewHolderPie)holder;
                PieDataSet set = new PieDataSet(S.dbHandler.getPieData(1), "Election Results");
                PieData pieData = new PieData(set);
                holderPie.pieChart.setData(pieData);
                holderPie.pieChart.invalidate();*/
                break;
            case 4:
                /*ViewHolderTopFacts holderTopFacts = (ViewHolderTopFacts) holder;
                holderTopFacts.fact1.setText("LIDL");                                   // DUMMYDATEN - später Inhalte aus der DB mit einer Funkktion
                holderTopFacts.fact2.setText("Max-Mustermann-Str. 4\n86161 Augsburg");  // DUMMYDATEN - später Inhalte aus der DB mit einer Funkktion
                holderTopFacts.fact3.setText("2585 €"); */                                // DUMMYDATEN - später Inhalte aus der DB mit einer Funkktion
                break;
            case 5:

                /*ViewHolderLine holderLine = (ViewHolderLine)holder;
                LineDataSet setComp1 = new LineDataSet(S.dbHandler.getLineData(1).get("lineOne"), "Company 1");
                setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
                LineDataSet setComp2 = new LineDataSet(S.dbHandler.getLineData(1).get("lineTwo"), "Company 2");
                setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
                List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(setComp1);
                dataSets.add(setComp2);
                LineData data = new LineData(dataSets);
                holderLine.lineChart.setData(data);
                holderLine.lineChart.invalidate();*/
                break;
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }


    /**
     * WICHTIG:
     * Klasse zur Vorbereitung der ArrayLists für die spätere Verarbeitung in den Statistiken
     * Je Nachdem wie gefiltert werden soll, holt sich die Methode die Daten aus der DB eines bestimmten Zeitraums
     *
     * Derzeit werden die ArrayLists mit DummyDaten befüllt - Bitte darauf achten!
     */
    public void createFilteredData(String filter){

       switch(filter){
           case "ALLE":
               fillData();
               break;
           case "WOCHE":
               fillData(S.getWeek()[0], S.getWeek()[1]);
               break;
           case "MONAT":
               fillData(S.getMonth()[0], S.getMonth()[1]);
               break;
           case "QUARTAL":
               fillData(S.getQuartal()[0], S.getQuartal()[1]);
               break;
           default:
               break;
       }
    }

    private void fillData(){
        this.fillGeneralData();
        this.date = new String[]{null, null};
        this.countBons = "" + S.dbHandler.getAllBonsCount(S.db);
        this.gesBetrag = "" + S.dbHandler.getTotalExpenditure(S.db);
    }

    private void fillData(String date1, String date2){
        this.fillGeneralData();
        this.date = new String[]{date1, date2};
        this.countBons = "" + S.dbHandler.getAllBonsCount(S.db, date1, date2);
        this.gesBetrag = "" + S.dbHandler.getTotalExpenditure(S.db, date1, date2);
    }

    private void fillGeneralData(){
        this.countLaeden = "" + S.dbHandler.getAllLaedenCount(S.db);
        this.countArtikel = "" + S.dbHandler.getAllArticleCount(S.db);
    }

    /**
     * ProgressBar Animation
     */
    public class ProgressBarAnimation extends Animation {
        private ProgressBar progressBar;
        private float from;
        private float  to;

        public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }

    }
}