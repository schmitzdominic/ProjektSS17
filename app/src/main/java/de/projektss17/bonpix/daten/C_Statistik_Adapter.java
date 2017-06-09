package de.projektss17.bonpix.daten;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.Hashtable;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;


/**
 * ReCreated by Johanns on 18.05.2017 - 20.05.2017.
 */

public class C_Statistik_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int count = 6;              // Gibt an wie viele Cards die RecyclerView beinhalten soll (derzeit 6 feste CARDS!)

    private ViewHolderGeneral holderGeneral;
    private ViewHolderBar holderBar;
    private ViewHolderTopProducts holderTopProducts;
    private ViewHolderPie holderPie;
    private ViewHolderLine holderLine;
    private ViewHolderTopFacts holderTopFacts;
    private Context context;
    private C_Statistik statistik;


    // LAYOUT TopFacts
    public class ViewHolderTopFacts extends RecyclerView.ViewHolder {

        public TextView mostVisitedLaden, articleCount, bonsCount, averagePrice;

        public ViewHolderTopFacts(View view) {
            super(view);

            // Implementierung des Layouts der einzelnen Objekte für die CardView
            this.mostVisitedLaden = (TextView) view.findViewById(R.id.statistik_card_topfacts_fact_one_content);
            this.articleCount = (TextView) view.findViewById(R.id.statistik_card_topfacts_fact_artikel_anzahl);
            this.bonsCount = (TextView) view.findViewById(R.id.statistik_card_topfacts_bons_anzahl);
            this.averagePrice = (TextView) view.findViewById(R.id.statistik_card_topfacts_fact_three_content);

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

        switch(getItemViewType(position)){
            case 0:

                if(this.holderGeneral == null){
                    this.holderGeneral = (ViewHolderGeneral) holder;
                }

                this.holderGeneral.anzahlScans.setText(this.statistik.getCountBons());
                this.holderGeneral.anzahlLaeden.setText(this.statistik.getCountLaeden());
                this.holderGeneral.anzahlArtikel.setText(this.statistik.getCountArtikel());
                this.holderGeneral.ausgabenGesamt.setText(this.statistik.getGesBetrag() + holderGeneral.itemView.getResources().getString(R.string.waehrung));

                break;
            case 1:

                if(this.holderBar == null){
                    this.holderBar = (ViewHolderBar) holder;
                }

                if(S.dbHandler.getAllBonsCount(S.db) == 0){break;}

                this.statistik.getBarData().setBarWidth(0.7f); // set custom bar width
                this.statistik.getBarData().setValueTextColor(ContextCompat.getColor(context, R.color.cardview_light_background));
                this.statistik.getBarData().setValueTextSize(8f);

                this.holderBar.barChart.setData(this.statistik.getBarData());
                this.holderBar.barChart = this.statistik.setBarChartSettings(holderBar.barChart);
                this.holderBar.barChart.invalidate();

                break;
            case 2:

                if(this.holderTopProducts == null){
                    this.holderTopProducts = (ViewHolderTopProducts) holder;
                }

                this.setTopThreeArticleInvisible();

                int count = 0;
                int size = 0;

                for(Hashtable.Entry<String, Integer> entry : this.statistik.getSortedArticleList()){
                    size += entry.getValue();
                }

                for(Hashtable.Entry<String, Integer> entry : this.statistik.getSortedArticleList()){

                    int progress = (int)((double)entry.getValue()/size*100);
                    double procent = ((double)entry.getValue()/size)*100 > 0 ? ((double)entry.getValue()/size)*100 : 0;

                    switch(count){
                        case 0:
                            this.holderTopProducts.produkt1 = this.statistik.setTopThreeArticleText(this.holderTopProducts.produkt1, entry);
                            this.holderTopProducts.progress1 = this.statistik.setTopThreeArticleProgress(this.holderTopProducts.progress1, progress);
                            this.holderTopProducts.percentage1 = this.statistik.setTopThreeArticleProcent(this.holderTopProducts.percentage1, procent);
                            break;
                        case 1:
                            this.holderTopProducts.produkt2 = this.statistik.setTopThreeArticleText(this.holderTopProducts.produkt2, entry);
                            this.holderTopProducts.progress2 = this.statistik.setTopThreeArticleProgress(this.holderTopProducts.progress2, progress);
                            this.holderTopProducts.percentage2 = this.statistik.setTopThreeArticleProcent(this.holderTopProducts.percentage2, procent);
                            break;
                        case 2:
                            this.holderTopProducts.produkt3 = this.statistik.setTopThreeArticleText(this.holderTopProducts.produkt3, entry);
                            this.holderTopProducts.progress3 = this.statistik.setTopThreeArticleProgress(this.holderTopProducts.progress3, progress);
                            this.holderTopProducts.percentage3 = this.statistik.setTopThreeArticleProcent(this.holderTopProducts.percentage3, procent);
                            break;
                    }

                    if(++count == 3){break;}
                }

                break;
            case 3:

                if(this.holderPie == null){
                    this.holderPie = (ViewHolderPie) holder;
                }

                PieDataSet pieDataSet = new PieDataSet(this.statistik.getPieces() , "");

                pieDataSet.setValueFormatter(new PercentFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        return "" + (int)value;
                    }
                });

                pieDataSet.setColors(this.statistik.getColorsPie());
                PieData pieData = new PieData(pieDataSet);
                pieData.setValueTextSize(10f);
                pieData.setValueTextColor(ContextCompat.getColor(context, R.color.cardview_light_background));

                this.holderPie.pieChart.setData(pieData);
                this.holderPie.pieChart = this.statistik.setPieChartSettings(this.holderPie.pieChart);
                this.holderPie.pieChart.invalidate();

                break;
            case 4:

                if(this.holderTopFacts == null){
                    this.holderTopFacts = (ViewHolderTopFacts) holder;
                }

                this.holderTopFacts.mostVisitedLaden.setText(this.statistik.getMostVisitedLaden());
                this.holderTopFacts.averagePrice.setText(this.statistik.getAveragePrice() + " " + context.getResources().getString(R.string.waehrung));
                this.holderTopFacts.bonsCount.setText(this.statistik.getMostVisitedLadenBonsCount());
                this.holderTopFacts.articleCount.setText(this.statistik.getGetMostVisitedLadenArticleCount());

                break;
            case 5:

                if(this.holderLine == null){
                    this.holderLine = (ViewHolderLine) holder;
                }

                this.statistik.setLineDataSetSettings();
                this.statistik.setLineDataSettings(this.statistik.getLineDataList());

                this.holderLine.lineChart.setData(this.statistik.getLineData());
                this.holderLine.lineChart = this.statistik.setLineChartSettings(this.holderLine.lineChart);
                this.holderLine.lineChart.invalidate();

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
    public void createFilteredData(String filter, Context context){

        this.context = context;

        this.statistik = new C_Statistik(context);

        switch(filter){
            case "ALLE":
                this.statistik.fillData("ALLE");
                break;
            case "WOCHE":
                this.statistik.fillData(S.getWeek()[0], S.getWeek()[1], "WOCHE");
                break;
            case "MONAT":
                this.statistik.fillData(S.getMonth()[0], S.getMonth()[1], "MONAT");
                break;
            case "QUARTAL":
                this.statistik.fillData(S.getQuartal()[0], S.getQuartal()[1], "QUARTAL");
                break;
            default:
                break;
        }

    }

    /**
     * Macht alle TopThreeArticles unsichtbar
     */
    private void setTopThreeArticleInvisible(){
        this.holderTopProducts.produkt1.setVisibility(View.INVISIBLE);
        this.holderTopProducts.progress1.setVisibility(View.INVISIBLE);
        this.holderTopProducts.percentage1.setVisibility(View.INVISIBLE);
        this.holderTopProducts.produkt2.setVisibility(View.INVISIBLE);
        this.holderTopProducts.progress2.setVisibility(View.INVISIBLE);
        this.holderTopProducts.percentage2.setVisibility(View.INVISIBLE);
        this.holderTopProducts.produkt3.setVisibility(View.INVISIBLE);
        this.holderTopProducts.progress3.setVisibility(View.INVISIBLE);
        this.holderTopProducts.percentage3.setVisibility(View.INVISIBLE);
    }
}