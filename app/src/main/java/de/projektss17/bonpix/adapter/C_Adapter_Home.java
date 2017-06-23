package de.projektss17.bonpix.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.projektss17.bonpix.A_Bon_Anzeigen;
import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;
import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.daten.C_Budget;

public class C_Adapter_Home extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int count = 3;              // Anzahlt der Items in der RecyclerView - derzeit 3 feste Cards!
    public int bonsCount = 3;           // Anzahl der Bons die in der BonCard angezeigt werden sollen (ist für die Zukunft somit dynamisch)
    private Context context;
    private String curreny, percentage; // Feste String aus der String-XML (Für '€'-Zeichen und '%'-Zeichen)
    private ArrayList<C_Bon> bons;
    private C_Budget budget;
    private LineDataSet dataSet;
    private ArrayList<ILineDataSet> lineDataSet;
    private LineData lineData;
    private Description descLine;

    /**
     * Standard constructor
     * @param context
     */
    public C_Adapter_Home(Context context) {
        this.context = context;
        this.curreny = context.getString(R.string.currency_europe);
        this.percentage = context.getString(R.string.percentage);
    }

    public class ViewHolderDefault extends RecyclerView.ViewHolder {

        TextView titleDefault, contentDefault;

        public ViewHolderDefault(View view) {
            super(view);
            titleDefault = (TextView) view.findViewById(R.id.default_cardview_title);
            contentDefault = (TextView)view.findViewById(R.id.default_cardview_content);
        }
    }

    public class ViewHolderBonCard extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;

        public ViewHolderBonCard(View view) {
            super(view);
            linearLayout = (LinearLayout)view.findViewById(R.id.tab_home_boncard_linearlayout);
        }
    }

    /**
     * INFO: 1:1 Layout von der Budgetierungs-Activity
     */
    public class ViewHolderBudgetCard extends RecyclerView.ViewHolder {

        public TextView budgetCurrently, yearBefore, monthBefore, yearAfter, monthAfter, progressPercentage, tagVon, tagBis;
        public ProgressBar progressBar;

        public ViewHolderBudgetCard(View view) {
            super(view);
            this.budgetCurrently = (TextView) view.findViewById(R.id.budget_content);
            this.monthBefore = (TextView) view.findViewById(R.id.budget_monat_von);
            this.tagVon = (TextView) view.findViewById(R.id.budget_jahr_von);
            this.monthAfter = (TextView) view.findViewById(R.id.budget_monat_bis);
            this.tagBis = (TextView) view.findViewById(R.id.budget_jahr_bis);
            this.yearBefore = (TextView) view.findViewById(R.id.budget_tag_von);
            this.yearAfter = (TextView) view.findViewById(R.id.budget_tag_bis);
            this.progressBar = (ProgressBar) view.findViewById(R.id.budget_progress_bar_circle);
            this.progressPercentage = (TextView) view.findViewById(R.id.budget_progress_percentage);
        }
    }

    public class ViewHolderLinechartCard extends RecyclerView.ViewHolder {

        public LineChart lineChart;

        public ViewHolderLinechartCard(View view) {
            super(view);
            this.lineChart = (LineChart) view.findViewById(R.id.tab_home_chartcard_linechart);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        switch (viewType) {     // Wenn Bons bestehen, dann soll die geplante Card angezeigt werden, ansonsten nur die Default-Card
            case 0:
                if(bons.size() != 0){
                    itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_home_boncard_layout, parent, false);
                    return new ViewHolderBonCard(itemView);
                } else {
                    itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_default_cardview_layout, parent, false);
                    return new ViewHolderDefault(itemView);
                }
            case 1:
                if(budget != null){
                    itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_home_topbudget_layout, parent, false);
                    return new ViewHolderBudgetCard(itemView);
                } else {
                    itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_default_cardview_layout, parent, false);
                    return new ViewHolderDefault(itemView);
                }
            case 2:
                if(bons.size() != 0){
                    itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_home_linechart_layout, parent, false);
                    return new ViewHolderLinechartCard(itemView);
                } else {
                    itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_default_cardview_layout, parent, false);
                    return new ViewHolderDefault(itemView);
                }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {    // Wenn Bons bestehen, dann soll die geplante Card angezeigt werden, ansonsten nur die Default-Card
            case 0:
                if(bons.size() != 0){
                    ViewHolderBonCard holderBonCard = (ViewHolderBonCard) holder;
                    for(int i = 0; i < bons.size(); i++){
                        holderBonCard.linearLayout.addView(inflateBonsRow(bons.get(i)), holderBonCard.linearLayout.getChildCount());
                    }
                } else {
                    ViewHolderDefault holderDefaultCard = (ViewHolderDefault) holder;
                    holderDefaultCard.titleDefault.setText(R.string.tab_home_boncard_title_content);
                    holderDefaultCard.contentDefault.setText(R.string.tab_home_chartcard_boncard_default_content);
                }
                break;
            case 1:
                if(budget != null){
                    ViewHolderBudgetCard holderBudgetCard = (ViewHolderBudgetCard) holder;
                    holderBudgetCard.budgetCurrently.setText(S.roundPrice(getRestBudget(budget)) + curreny);
                    holderBudgetCard.yearBefore.setText(budget.getYearVon());
                    holderBudgetCard.monthBefore.setText(budget.getMonthVon());
                    holderBudgetCard.yearAfter.setText(budget.getYearBis());
                    holderBudgetCard.monthAfter.setText(budget.getMonthBis());
                    holderBudgetCard.progressPercentage.setText(getRestPercentage(budget) + percentage);
                    holderBudgetCard.tagVon.setText(budget.getZeitraumVon().split("\\.")[0]);
                    holderBudgetCard.tagBis.setText(budget.getZeitraumBis().split("\\.")[0]);
                    holderBudgetCard.progressBar.setProgress((int) (100 - Double.parseDouble(getRestPercentage(budget))));
                } else {
                    ViewHolderDefault holderDefaultCard = (ViewHolderDefault) holder;
                    holderDefaultCard.titleDefault.setText(R.string.tab_home_budgetcard_title_content);
                    holderDefaultCard.contentDefault.setText(R.string.tab_home_chartcard_budget_default_content);
                }
                break;
            case 2:
                if (bons.size() != 0){
                    ViewHolderLinechartCard holderLine = (ViewHolderLinechartCard) holder;
                    holderLine.lineChart.setData(lineData);
                    holderLine.lineChart.invalidate();
                    holderLine.lineChart.setTouchEnabled(false);
                    holderLine.lineChart.setDescription(descLine);
                    holderLine.lineChart.getXAxis().setEnabled(false);
                    holderLine.lineChart.getXAxis().setAxisMaximum(bons.size()+1);
                    holderLine.lineChart.getAxisLeft().setAxisLineColor(ContextCompat.getColor(context, R.color.cardview_light_background));
                    holderLine.lineChart.animateY(1000);
                    holderLine.lineChart.getLegend().setEnabled(false);
                    holderLine.lineChart.getAxisLeft().setEnabled(true);
                    holderLine.lineChart.getAxisRight().setEnabled(false);
                    holderLine.lineChart.setViewPortOffsets(145f, 18f, 15f, 30f);
                    holderLine.lineChart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            double doubleValue = Math.round(value * 100) / 100.00;
                            DecimalFormat df = new DecimalFormat("#0.00");
                            return df.format(doubleValue) + " " + context.getResources().getString(R.string.waehrung) + "  ";
                        }
                    });

                    this.dataSet.setCircleColor(ContextCompat.getColor(context, R.color.colorAccent));
                    this.dataSet.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    this.dataSet.setLineWidth(3);
                    this.dataSet.setCircleRadius(8);

                    XAxis xAxis = holderLine.lineChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawAxisLine(true);
                    xAxis.setAxisMaximum(bonsCount+1);
                    xAxis.setAxisMinimum(0);
                    xAxis.setLabelCount(bonsCount+1);
                    YAxis rightYAxis = holderLine.lineChart.getAxisLeft();
                    rightYAxis.setAxisMaxValue((float)(dataSet.getYMax()*1.25));
                    rightYAxis.setAxisMinValue(0);
                    rightYAxis.setLabelCount(bonsCount+2);

                    this.lineData.setValueTextSize(10);
                    this.lineData.setValueTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    this.lineData.setHighlightEnabled(true);
                    this.lineData.setValueFormatter(new IValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                            return bons.get((int)entry.getX() - 1).getShopName();
                        }
                    });
                } else {
                    ViewHolderDefault holderDefaultCard = (ViewHolderDefault) holder;
                    holderDefaultCard.titleDefault.setText(R.string.tab_home_chartcard_line_title_content);
                    holderDefaultCard.contentDefault.setText(R.string.tab_home_chartcard_boncard_default_content);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    /**
     * Errechnet den restlichen Budgetbetrag und gibt diesen als String zurück
     * @param budget Ein ausgewähltes Budget aus der DB ( Derzeit wird immer das erste genommen! )
     * @return Rückgabe des Restbetrags
     */
    private double getRestBudget(C_Budget budget) {
        return ((double) budget.getBudgetMax() - budget.getBudgetLost());
    }

    /**
     * Errechnet den restlichen Budgetbetrag als Prozent aus und gibt diesen als String zurück
     * @param budget Ein ausgewähltes Budget aus der DB ( Derzeit wird immer das erste genommen! )
     * @return Rückgabe des Restbetrags als Prozent
     */
    private String getRestPercentage(C_Budget budget) {
        return "" + (Math.round((((this.getRestBudget(budget)) / budget.getBudgetMax()) * 100) * 100) / 100.00);
    }

    /**
     * Prüft beim erstellen des Layouts, ob der Bon ein Favorit ist oder nicht
     * INFO: Wenn Favorit true dann wird die ImageView dunkel befüllt, anderfalls bleibt der Inhalt unausgefüllt
     * @param bon Übergabe eines ausgewählten Bons, welches überprüft wird
     * @param favImage Übergabe der ImageView, die geändert wird ( In diesem Fall Favoriten-Image)
     * @return Rückgabe des übergebenen und veränderten Images
     */
    private ImageView proofFavorite(final C_Bon bon, final ImageView favImage) {

        if (bon.getFavourite()) {
            favImage.setImageDrawable(favImage.getContext().getResources().getDrawable(R.drawable.star));
            favImage.setColorFilter(favImage.getContext().getResources().getColor(R.color.colorPrimary));
            return favImage;
        } else {
            favImage.setImageDrawable(favImage.getContext().getResources().getDrawable(R.drawable.star_outline));
            favImage.setColorFilter(favImage.getContext().getResources().getColor(R.color.colorPrimary));
            return favImage;
        }
    }

    /**
     * Erzeugt eine Dynamische Liste mit den aktuellsten Bons aus der DB
     * @param bon Übergabe der einzelnen Bons
     * @return Rückgabe der fertiggebauten View
     */
    private View inflateBonsRow(final C_Bon bon){

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.box_home_boncard_content, null);
        final TextView shopName = (TextView)rowView.findViewById(R.id.bons_shop_name);
        final TextView date = (TextView)rowView.findViewById(R.id.bons_buying_date);
        final TextView price = (TextView)rowView.findViewById(R.id.bons_price);
        final ImageView shopIcon = (ImageView)rowView.findViewById(R.id.bons_shop_image);
        final ImageView favIcon = proofFavorite(bon,(ImageView)rowView.findViewById(R.id.bons_favorite_icon));

        shopName.setText(bon.getShopName());
        date.setText(bon.getDate());
        price.setText(bon.getTotalPrice() + " " + curreny);
        shopIcon.setImageBitmap(S.getShopIcon(context.getResources(), bon.getShopName()));

        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bon.getFavourite()) {
                    favIcon.setImageDrawable(favIcon.getContext().getResources().getDrawable(R.drawable.star_outline));
                    favIcon.setColorFilter(favIcon.getContext().getResources().getColor(R.color.colorPrimary));
                    bon.setFavourite(false);
                    S.dbHandler.updateBon(S.db, bon);
                } else {
                    favIcon.setImageDrawable(favIcon.getContext().getResources().getDrawable(R.drawable.star));
                    favIcon.setColorFilter(favIcon.getContext().getResources().getColor(R.color.colorPrimary));
                    bon.setFavourite(true);
                    S.dbHandler.updateBon(S.db, bon);
                }
            }
        });

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, A_Bon_Anzeigen.class);
                intent.putExtra("BonPos", bon.getId());
                context.startActivity(intent);
            }
        });
        return rowView;
    }

    /**
     * Holt die Daten aus der DB zur weiteren Verarbeitung und bereitet diese vor (Aufruf in der onResume())
     * HINWEIS: Falls die jeweiligen Daten existieren, werden die Attribute bons & budget befüllt bzw. initialisiert.
     *          Wenn NICHT werden die Attribute LEER initialisiert und dementsprechend verarbeitet
     */
    public void prepareBonData(){
        this.bons = S.dbHandler.getNumberOfNewestBons(S.db,bonsCount);
        if(S.dbHandler.getFavoriteBudget(S.db) != null){
            budget = S.dbHandler.getFavoriteBudget(S.db);
            S.dbHandler.refreshBudget(S.db, budget);
        } else {
            budget = null;
        }
        prepareLineChartData(bonsCount);
    }

    /**
     * Bereitet die Daten für das Linien-Diagramm vor - Hierbei werden Daten aus der DB geholt und weiter verarbeitet
     * @param anzahl Übergabe der Bons die angezeigt werden sollen - Standardmäßig soviele Bons wie in der BonCard angezeigt werden (bonsCount)
     */
    public void prepareLineChartData(int anzahl){
        this.dataSet = new LineDataSet(S.dbHandler.getLineData(S.db, anzahl+1), "");
        this.lineDataSet = new ArrayList<>();
        this.lineDataSet.add(this.dataSet);
        this.lineData = new LineData(lineDataSet);
        this.descLine = new Description();
        this.descLine.setText("");  // Implementierung der Description für die Chart - soll jedoch KEINE Description zeigen, daher .setText("")
    }
}