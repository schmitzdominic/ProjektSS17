package de.projektss17.bonpix.daten;

import android.support.v7.widget.RecyclerView;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;


/**
 * ReCreated by Johanns on 18.05.2017 - 20.05.2017.
 */

public class C_Statistik_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int count = 6;              // Gibt an wie viele Cards die RecyclerView beinhalten soll (derzeit 6 feste CARDS!)
    private ArrayList<C_Bon> bons;      // Sammlung aller aus der DB ausgelesenen Bons
    private ArrayList<C_Laden> laeden;  // Sammlung aller auser DB ausgelesenen Läden
    public String filter = "ALLE";      // Filter-Inforamation für die Methode createFilteredData (WICHTIG: siehe unten im Code)


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
        BarChart chart;

        public ViewHolderBar(View view) {
            super(view);
            chart = (BarChart) view.findViewById(R.id.statistik_card_bar);
        }
    }


    // LAYOUT - LineChart
    public class ViewHolderLine extends RecyclerView.ViewHolder {
        LineChart chart1;

        public ViewHolderLine(View view) {
            super(view);
            chart1 = (LineChart) view.findViewById(R.id.statistik_card_line);
        }
    }


    // LAYOUT - PieChart
    public class ViewHolderPie extends RecyclerView.ViewHolder {
        PieChart chart2;

        public ViewHolderPie(View view) {
            super(view);
            chart2 = (PieChart) view.findViewById(R.id.statistik_card_pie);
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
        createFilteredData();       //WICHTIG: Befüllt die ArrayLists (Läden & Bons) entsprechend der Filterung zur späteren Verarbeitung

        switch(getItemViewType(position)){
            case 0:
                ViewHolderGeneral holderGeneral = (ViewHolderGeneral)holder;
                holderGeneral.anzahlScans.setText(Integer.toString(bons.size()));

                for(int i = 0; i < bons.size(); i++)
                    for(int j = 0; j < bons.get(i).getArticles().size();j++)
                        ausgabenGesamt+=bons.get(i).getArticles().get(j).getPrice();

                holderGeneral.ausgabenGesamt.setText(formatDouble(ausgabenGesamt)+" €");

                for(int i = 0; i < bons.size();i++)
                    anzahlArtikel+=bons.get(i).getArticles().size();

                holderGeneral.anzahlArtikel.setText(Integer.toString(anzahlArtikel));
                holderGeneral.anzahlLaeden.setText(Integer.toString(laeden.size()));
                break;
            case 1:
                ViewHolderBar holderBar = (ViewHolderBar)holder;
                BarDataSet dataSetBar = new BarDataSet(S.dbHandler.getBarData(1), "test");
                BarData dataBar = new BarData(dataSetBar);
                dataBar.setBarWidth(0.9f); // set custom bar width
                holderBar.chart.setData(dataBar);
                holderBar.chart.setFitBars(true); // make the x-axis fit exactly all bars
                holderBar.chart.invalidate(); // refresh
                break;
            case 2:
                ViewHolderTopProducts holderTopProducts = (ViewHolderTopProducts)holder;
                holderTopProducts.produkt1.setText("Videospiele");  // DUMMYDATEN - später Inhalte aus der DB mit einer Funkktion
                holderTopProducts.produkt2.setText("Getränke");     // DUMMYDATEN - später Inhalte aus der DB mit einer Funkktion
                holderTopProducts.produkt3.setText("Steaks");       // DUMMYDATEN - später Inhalte aus der DB mit einer Funkktion
                holderTopProducts.progress1.setProgress(45);        // DUMMYDATEN - später Inhalte aus der DB mit einer Funkktion
                holderTopProducts.progress2.setProgress(35);        // DUMMYDATEN - später Inhalte aus der DB mit einer Funkktion
                holderTopProducts.progress3.setProgress(20);        // DUMMYDATEN - später Inhalte aus der DB mit einer Funkktion
                holderTopProducts.percentage1.setText("45 %");      // DUMMYDATEN - später Inhalte aus der DB mit einer Funkktion
                holderTopProducts.percentage2.setText("35 %");      // DUMMYDATEN - später Inhalte aus der DB mit einer Funkktion
                holderTopProducts.percentage3.setText("20 %");      // DUMMYDATEN - später Inhalte aus der DB mit einer Funkktion
                break;
            case 3:
                ViewHolderPie holderPie = (ViewHolderPie)holder;
                PieDataSet set = new PieDataSet(S.dbHandler.getPieData(1), "Election Results");
                PieData pieData = new PieData(set);
                holderPie.chart2.setData(pieData);
                holderPie.chart2.invalidate();
                break;
            case 4:
                ViewHolderTopFacts holderTopFacts = (ViewHolderTopFacts) holder;
                holderTopFacts.fact1.setText("LIDL");                                   // DUMMYDATEN - später Inhalte aus der DB mit einer Funkktion
                holderTopFacts.fact2.setText("Max-Mustermann-Str. 4\n86161 Augsburg");  // DUMMYDATEN - später Inhalte aus der DB mit einer Funkktion
                holderTopFacts.fact3.setText("2585 €");                                 // DUMMYDATEN - später Inhalte aus der DB mit einer Funkktion
                break;
            case 5:
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
    public void createFilteredData(){

        laeden = S.dbHandler.getAllLaeden(S.db);

       if(filter=="ALLE"){
           //bons = S.dbHandler.getAllBons(S.db);   derzeit dient die createBonData zum befüllen
           bons = createBonData(190);
       }

       if(filter =="TAG"){
           //bons = S.dbHandler.getAllBons(S.db);   derzeit dient die createBonData zum befüllen
           bons = createBonData(7);
       }

        if(filter =="MONAT"){
            //bons = S.dbHandler.getAllBons(S.db);   derzeit dient die createBonData zum befüllen
            bons = createBonData(36);
        }

        if(filter =="JAHR"){
            //bons = S.dbHandler.getAllBons(S.db);   derzeit dient die createBonData zum befüllen
            bons = createBonData(105);
        }
    }


    /**
     * ALLE 'DUMMYDATEN'-Funktionen werden gelöscht sofern DB-Anbdinung besteht!
     * formatDoulbe nach Anbindung bitte im onBindViewHolder löschen/ersetzen!
     */

    // DUMMYDATEN - Formatierung der RandomMath Doubles auf zwei Nachkommastellen
    public static String formatDouble(double i)
    {
        DecimalFormat f = new DecimalFormat("#0.00");
        double toFormat = ((double)Math.round(i*100))/100;
        return f.format(toFormat);
    }

    //DUMMYDATEN - BONS
    public ArrayList<C_Bon> createBonData(int anzahl){

        ArrayList<C_Bon> bons = new ArrayList<>();

        for(int i = 0; i < anzahl; i++)
            bons.add(new C_Bon(i, "Path/Bons/Test","Supermarkt "+i, "Max-Mustermann Str. XB"+i+" \n86161 Augsburg",
                    "Das sind DUMMYBONS", "21.05.2017", "30.05.2017", false, true, createArticleData(anzahl)));

        return bons;
    }

    //DUMMYDATEN - ARTIKEL
    public ArrayList<C_Artikel> createArticleData(int anzahl){

        ArrayList<C_Artikel> articles = new ArrayList<>();

        for(int i = 0; i < anzahl; i++)
            articles.add(new C_Artikel("Artikel " + i, (Math.random()*100.0)/100.0));

        return articles;
    }
}