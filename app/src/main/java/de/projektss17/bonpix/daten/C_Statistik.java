package de.projektss17.bonpix.daten;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.SortedSet;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;

public class C_Statistik {

    private int MAXVALUE_BAR_CHART = 5;
    private int MAXVALUE_PIE_CHART = 5;
    private int MAX_LENGTH_ARTICLE = 11;
    private int ANIMATION_TIME = 1000;

    private Context context;
    private String countBons;
    private String countLaeden;
    private String countArtikel;
    private String gesBetrag;
    private String averagePrice;
    private String mostVisitedLaden;
    private String mostVisitedLadenBonsCount;
    private String getMostVisitedLadenArticleCount;
    private String date[];
    private String state;
    private SortedSet<Hashtable.Entry<String, Integer>> sortedArticleList;
    private SortedSet<Hashtable.Entry<String, Integer>> bonsCountPerLaden;
    private List<PieEntry> pieces;
    private LineDataSet dataSet;
    private ArrayList<ILineDataSet> lineDataSet;
    private ArrayList<Integer> colorsPie;
    private ArrayList<String> lineDataList;
    private LineData lineData;
    private BarData barData;

    public C_Statistik(Context context){
        this.context = context;
    }

    public void fillData(String state){
        this.fillData(null, null, state);
    }

    public void fillData(String date1, String date2, String state){

        this.state = state;
        this.date = new String[]{date1, date2};

        this.countLaeden = "" + S.dbHandler.getAllLaedenCount(S.db);
        this.countArtikel = this.getAllArticleCount();

        this.countBons = date1 != null && date2 != null ?
                "" + S.dbHandler.getAllBonsCount(S.db, date1, date2) :
                "" + S.dbHandler.getAllBonsCount(S.db);

        this.gesBetrag = date1 != null && date2 != null ?
                "" + S.dbHandler.getTotalExpenditure(S.db, date1, date2) :
                "" + S.dbHandler.getTotalExpenditure(S.db);

        this.prepareBarData();
        this.prepareTopThreeArticlesData();
        this.preparePieData();
        this.prepareTopFacts();
        this.prepareLineData();
    }

    /**
     * Gibt die Anzahl der Artikel innerhalb des Zeitraums
     * @return Anzahl
     */
    private String getAllArticleCount(){

        ArrayList<C_Bon> bonList;

        if(date[0] != null && date[1] != null){
            bonList = S.dbHandler.getBonsBetweenDate(S.db, date[0], date[1]);
        } else {
            bonList = S.dbHandler.getAllBons(S.db);
        }

        int counter = 0;

        for(C_Bon bon : bonList){
            counter += bon.getArticles().size();
        }

        return "" + counter;
    }

    /**
     * Bereitet das Bar Chart vor
     */
    private void prepareBarData(){

        ArrayList<IBarDataSet> bars = new ArrayList<>();

        if(this.date != null){
            for(int i = 0; i < MAXVALUE_BAR_CHART; i++){

                List<BarEntry> list = S.dbHandler.getBarDataLaedenExpenditure(S.db, this.date[0], this.date[1], i);

                if(S.dbHandler.getBarDataLaden() != null && list.get(0).getY() != 0.0){
                    BarDataSet set = (new BarDataSet(list, S.dbHandler.getBarDataLaden().getName()));
                    set.setColor(this.getStatisticColor(i));
                    bars.add(set);
                }
            }
        }

        this.barData = new BarData(bars);
    }

    /**
     * Bereitet die Top 3 Artikel vor
     */
    private void prepareTopThreeArticlesData(){

        this.sortedArticleList = S.dbHandler.getTopArticles(S.db, this.date[0], this.date[1]);
    }

    /**
     * Bereitet das PieChart vor
     */
    private void preparePieData(){

        this.bonsCountPerLaden = S.dbHandler.getBonsCountPerLaden(S.db, this.date[0], this.date[1]);
        this.pieces = new ArrayList<>();

        int sizeLaedenBon = 0, counter = 0;

        for(Hashtable.Entry<String, Integer> entry : this.bonsCountPerLaden){
            sizeLaedenBon += entry.getValue();
        }

        if(this.date != null){
            for(Hashtable.Entry<String, Integer> entry : this.bonsCountPerLaden){
                if(entry.getValue() != 0){

                    PieEntry pieEntry;

                    if(counter + 1 == MAXVALUE_PIE_CHART){
                        pieEntry = new PieEntry(sizeLaedenBon, context.getResources().getString(R.string.statistik_card_rest_value));
                    } else {
                        sizeLaedenBon -= entry.getValue();
                        pieEntry = new PieEntry(entry.getValue(), entry.getKey());
                    }

                    this.pieces.add(pieEntry);
                }
                if(++counter == MAXVALUE_PIE_CHART){break;}
            }
        }

        this.colorsPie = new ArrayList<>();

        for(int i = 0; i < MAXVALUE_PIE_CHART; i++){
            this.colorsPie.add(this.getStatisticColor(i));
        }
    }

    /**
     * Bereitet die TopFacts vor
     */
    private void prepareTopFacts(){

        C_Laden laden = S.dbHandler.getMostVisitedLaden(S.db, this.date[0], this.date[1]);

        this.mostVisitedLaden = laden.getName();
        this.averagePrice = S.dbHandler.averageExpenditureLaden(S.db, laden, this.date[0], this.date[1]);
        this.mostVisitedLadenBonsCount = "" + S.dbHandler.bonsCountLaden(S.db, laden, this.date[0], this.date[1]);
        this.getMostVisitedLadenArticleCount = "" + S.dbHandler.articleCountLaden(S.db, laden, this.date[0], this.date[1]);

    }

    /**
     * Bereitet die Line Data vor
     */
    private void prepareLineData(){


        int counter = 1;

        ArrayList<String> data = S.dbHandler.getExpenditureLastWeek(S.db);
        List<Entry> dataList = new ArrayList<>();
        this.lineDataSet = new ArrayList<>();

        for(String value : data){
            dataList.add(new Entry((float) counter++, Float.parseFloat(value.split("/")[1].replace(",","."))));
        }

        this.dataSet = new LineDataSet(dataList, this.context.getString(R.string.statistik_card_line_chart_title));

        /*if(this.state.equals("ALLE") || this.state.equals("WOCHE")){
            // TODO Have to be implemented! BITTE NICHT ANMERKEN DA DAS SPÄTER IMPLEMENTIERT WIRD!!
        } else if (this.state.equals("MONAT")){
            //TODO BITTE NICHT ANMERKEN DA DAS SPÄTER IMPLEMENTIERT WIRD!!
        } else {
            // TODO BITTE NICHT ANMERKEN DA DAS SPÄTER IMPLEMENTIERT WIRD!!
        }*/

        this.lineDataList = S.dbHandler.getExpenditureLastWeek(S.db);

    }

    /**
     * Setzt den Text der Top 3 Artikel
     * @param product TextView
     * @param entry Eintrag
     * @return TextView
     */
    public TextView setTopThreeArticleText(TextView product, Hashtable.Entry<String, Integer> entry){

        product.setText(entry.getKey().length() > MAX_LENGTH_ARTICLE ? entry.getKey().substring(0, MAX_LENGTH_ARTICLE-2) + ".." : entry.getKey());
        product.setVisibility(View.VISIBLE);

        return product;
    }

    /**
     * Setzt den Progress Stand der Leiste
     * @param progressBar Leiste (ProgressBar)
     * @param progress Progress (Fortschritt)
     * @return ProgressBAr
     */
    public ProgressBar setTopThreeArticleProgress(ProgressBar progressBar, int progress){

        ProgressBarAnimation anim = new C_Statistik.ProgressBarAnimation(progressBar, 0, progress);
        anim.setDuration(ANIMATION_TIME);
        progressBar.startAnimation(anim);

        if(progressBar.getProgress() == 0){progressBar.setProgress(progress);}
        progressBar.setVisibility(View.VISIBLE);

        return progressBar;
    }

    /**
     * Setzt die Prozentanzahl
     * @param percentage TextView
     * @param procent Prozentanzahl
     * @return TextView
     */
    public TextView setTopThreeArticleProcent(TextView percentage, double procent){

        percentage.setText(S.roundPrice(procent) + " " + this.context.getResources().getString(R.string.percentage));
        percentage.setVisibility(View.VISIBLE);

        return percentage;
    }

    /**
     * Setzt die Settings für das Bar Chart
     * @param barChart BarChart das gesetzt werden soll
     * @return gesetztes BarChart
     */
    public BarChart setBarChartSettings(BarChart barChart){

        barChart.setDescription(this.getEmptyDescription());
        barChart.setDrawValueAboveBar(false);
        barChart.getXAxis().setEnabled(false);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.setTouchEnabled(false);
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setClickable(false);
        barChart.setFocusableInTouchMode(false);
        barChart.setFitBars(true);
        barChart.animateY(ANIMATION_TIME);
        barChart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return S.roundPrice((double) value) + " " + context.getResources().getString(R.string.waehrung) + "  ";
            }
        });

        return barChart;
    }

    /**
     * Setzt die Settings für das Pie Chart
     * @param pieChart PieChart das gesetzt werden soll
     * @return gesetztes PieChart
     */
    public PieChart setPieChartSettings(PieChart pieChart){

        pieChart.setDescription(this.getEmptyDescription());
        pieChart.animateY(ANIMATION_TIME);
        pieChart.setFocusable(false);
        pieChart.setTouchEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setUsePercentValues(false);

        return pieChart;
    }

    /**
     * Setzt die LineDataSet Settings
     */
    public void setLineDataSetSettings(){

        this.dataSet.setCircleColor(ContextCompat.getColor(context, R.color.colorAccent));
        this.dataSet.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        this.dataSet.setLineWidth(3);
        this.dataSet.setCircleRadius(8);

        this.lineDataSet.add(dataSet);
    }

    /**
     * Setzt die LineData Settings
     * @param data Daten
     */
    public void setLineDataSettings(final ArrayList<String> data){

        this.lineData = new LineData(this.lineDataSet);
        this.lineData.setValueTextSize(10);
        this.lineData.setValueTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        this.lineData.setHighlightEnabled(true);
        this.lineData.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return data.get((int)entry.getX()-1).split("/")[0].substring(0,2);
            }
        });
    }

    /**
     * Setzt die Settings für das Line Chart
     * @param lineChart LineChart
     * @return LineChart
     */
    public LineChart setLineChartSettings(LineChart lineChart){

        lineChart.setDescription(this.getEmptyDescription());
        lineChart.setTouchEnabled(false);
        lineChart.getXAxis().setEnabled(false);
        lineChart.getXAxis().setAxisMinimum(0);
        lineChart.getXAxis().setAxisMaximum(this.lineDataList.size()+1);
        lineChart.getAxisLeft().setAxisLineColor(ContextCompat.getColor(context, R.color.cardview_light_background));
        lineChart.animateY(ANIMATION_TIME);
        lineChart.getLegend().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setViewPortOffsets(200f, 30f, 15f, 30f);
        lineChart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return S.roundPrice((double) value) + " " + context.getResources().getString(R.string.waehrung) + "  ";
            }
        });

        return lineChart;
    }

    /**
     * Gibt eine leere Description zurück
     * @return Leere Description
     */
    public Description getEmptyDescription(){

        Description desc = new Description();
        desc.setText("");

        return desc;
    }

    /**
     * Gibt die Anzahl der Bons zurück
     * @return Anzahl der Bons
     */
    public String getCountBons() {
        return countBons;
    }

    /**
     * Gibt die Anzahl der Laeden zurück
     * @return Anzahl der Laeden
     */
    public String getCountLaeden() {
        return countLaeden;
    }

    /**
     * Gibt die Anzahl der Artikel zurück
     * @return Artikel Anzahl
     */
    public String getCountArtikel() {
        return countArtikel;
    }

    /**
     * Gibt den Gesamtbetrag wieder
     * @return Gesamtbetrag
     */
    public String getGesBetrag() {
        return gesBetrag;
    }

    /**
     * Gibt die Daten für das Bar Chart zurück
     * @return BarChart Daten
     */
    public BarData getBarData() {
        return this.barData;
    }

    /**
     * Gibt Sortiert die Daten der Top3 Artikel zurück
     * @return Top3 Artikel Daten
     */
    public SortedSet<Hashtable.Entry<String, Integer>> getSortedArticleList() {
        return this.sortedArticleList;
    }

    /**
     * Gibt den Durchschnittspreis zurück
     * @return Durchschnitspeis
     */
    public String getAveragePrice() {
        return averagePrice;
    }

    /**
     * Gibt den meistbesuchten Laden zurück
     * @return meistbesuchter Laden
     */
    public String getMostVisitedLaden() {
        return mostVisitedLaden;
    }

    /**
     * Gibt die Bonsanzahl des meistbesuchten Ladens zurück
     * @return Bons anzahl
     */
    public String getMostVisitedLadenBonsCount() {
        return mostVisitedLadenBonsCount;
    }

    /**
     * Gibt die Artikelanzahl des meistbesuchten Ladens zurück
     * @return
     */
    public String getGetMostVisitedLadenArticleCount() {
        return getMostVisitedLadenArticleCount;
    }

    /**
     * Gibt die Farben für den Pie Chart zurück
     * @return Farben Array
     */
    public ArrayList<Integer> getColorsPie() {
        return colorsPie;
    }

    /**
     * Gibt die Stücke des Kuchens zurück
     * @return Kuchenstücke
     */
    public List<PieEntry> getPieces() {
        return pieces;
    }

    /**
     * Gibt die LineData zurück
     * @return LineData
     */
    public LineData getLineData() {
        return lineData;
    }

    /**
     * Gibt die DatenListe zurück
     * @return lineDataList
     */
    public ArrayList<String> getLineDataList() {
        return lineDataList;
    }

    /**
     * Gibt die Farben für die Statistiken zurück
     * @param number Welche nummer?
     * @return Farbe
     */
    private int getStatisticColor(int number){

        switch(number){
            case 0:
                return ContextCompat.getColor(this.context,R.color.colorPrimaryDark);
            case 1:
                return ContextCompat.getColor(this.context,R.color.colorAccent);
            case 2:
                return ContextCompat.getColor(this.context,R.color.colorPrimary);
            case 3:
                return ContextCompat.getColor(this.context,R.color.color4Bar);
            case 4:
                return ContextCompat.getColor(this.context,R.color.color5Bar);
            default:
                return ContextCompat.getColor(this.context,R.color.colorPrimary);
        }
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
