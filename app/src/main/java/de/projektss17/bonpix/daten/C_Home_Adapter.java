package de.projektss17.bonpix.daten;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.util.ArrayList;

import de.projektss17.bonpix.A_Tab1Home;
import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;

public class C_Home_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int count = 3;
    private Context context;
    private ArrayList<C_Bon> bons;
    private ArrayList<C_Budget> budgets;
    private String curreny, percentage;


    public C_Home_Adapter(Context context){
        this.context=context;
        this.curreny = context.getString(R.string.currency_europe);
        this.percentage = context.getString(R.string.percentage);
    }

    // LAYOUT BonCard
    // (Hinweis: Drei feste Inhalte! Dynamisch würde kein Sinn machen, da hierfür die Tab Bons bereit steht! )
    public class ViewHolderBonCard extends RecyclerView.ViewHolder {

        public Button bon1, bon2, bon3;

        public TextView firstContentAbove, firstContentBelow, firstBonPrice,
                secondContentAbove, secondContentBelow, secondBonPrice,
                thirdContentAbove, thirdContentBelow, thirdBonPrice;
        public ImageView firstBonImage, firstFavoriteImage, secondBonImage, secondFavoriteImage,
                thirdBonImage, thirdFavoriteImage;

        public ViewHolderBonCard(View view) {
            super(view);

            // Implementierung des Layouts der einzelnen Objekte für die CardView
            this.firstContentAbove = (TextView)view.findViewById(R.id.tab_home_boncard_first_bon_above_content);
            this.firstContentBelow = (TextView)view.findViewById(R.id.tab_home_boncard_first_bon_below_content);
            this.firstBonPrice = (TextView)view.findViewById(R.id.tab_home_boncard_first_bon_betrag);
            this.secondContentAbove = (TextView)view.findViewById(R.id.tab_home_boncard_second_bon_above_content);
            this.secondContentBelow = (TextView)view.findViewById(R.id.tab_home_boncard_second_bon_below_content);
            this.secondBonPrice = (TextView)view.findViewById(R.id.tab_home_boncard_second_bon_betrag);
            this.thirdContentAbove = (TextView)view.findViewById(R.id.tab_home_boncard_third_bon_above_content);
            this.thirdContentBelow = (TextView)view.findViewById(R.id.tab_home_boncard_third_bon_below_content);
            this.thirdBonPrice = (TextView)view.findViewById(R.id.tab_home_boncard_third_bon_betrag);

            this.firstBonImage = (ImageView)view.findViewById(R.id.tab_home_boncard_first_bon_small_image);
            this.firstFavoriteImage = (ImageView)view.findViewById(R.id.tab_home_boncard_first_bon_big_image);
            this.secondBonImage = (ImageView)view.findViewById(R.id.tab_home_boncard_second_bon_small_image);
            this.secondFavoriteImage = (ImageView)view.findViewById(R.id.tab_home_boncard_second_bon_big_image);
            this.thirdBonImage = (ImageView)view.findViewById(R.id.tab_home_boncard_third_bon_small_image);
            this.thirdFavoriteImage = (ImageView)view.findViewById(R.id.tab_home_boncard_third_bon_big_image);

            //Implementierung der Invisible-Buttons zum Auswählen der zuletzt eingescannten Bons
            this.bon1 = (Button) view.findViewById(R.id.tab_home_boncard_first_bon);
            this.bon2 = (Button) view.findViewById(R.id.tab_home_boncard_second_bon);
            this.bon3 = (Button) view.findViewById(R.id.tab_home_boncard_third_bon);

        }
    }


    // LAYOUT BudgetCard
    public class ViewHolderBudgetCard extends RecyclerView.ViewHolder {

        public TextView budgetCurrently, yearBefore,monthBefore, yearAfter, monthAfter, progressPercentage, tagVon, tagBis;
        public ProgressBar progressBar;

        public ViewHolderBudgetCard(View view) {
            super(view);

            // Implementierung des Layouts der einzelnen Objekte in der View
            this.budgetCurrently = (TextView) view.findViewById(R.id.budget_content);
            this.monthBefore = (TextView) view.findViewById(R.id.budget_monat_von);
            this.yearBefore = (TextView) view.findViewById(R.id.budget_jahr_von);
            this.monthAfter = (TextView) view.findViewById(R.id.budget_monat_bis);
            this.yearAfter = (TextView) view.findViewById(R.id.budget_jahr_bis);
            this.tagVon = (TextView) view.findViewById(R.id.budget_tag_von);
            this.tagBis = (TextView) view.findViewById(R.id.budget_tag_bis);
            this.progressBar = (ProgressBar)view.findViewById(R.id.budget_progress_bar_circle);
            this.progressPercentage = (TextView) view.findViewById(R.id.budget_progress_percentage);


        }
    }


    // LAYOUT LineChartCard
    public class ViewHolderLinechartCard extends RecyclerView.ViewHolder{

        public LineChart lineChart;

        public ViewHolderLinechartCard(View view){
            super(view);

            //Diese Logik war bereits gegeben vom vorherigen Sprint! (Bei Fragen an den jeweiligen wenden!)
            //ToDo - Es muss noch entschieden weren, welche Daten ausgelesen und ausgewertet werden
            LineDataSet dataSet;
            ArrayList<ILineDataSet> lineDataSet;
            LineData lineData;

            this.lineChart = (LineChart)view.findViewById(R.id.tab_home_chartcard_linechart);
            lineChart.animateXY(2000, 4000);
            lineChart.setPadding(30, 30, 30, 30);

            lineDataSet = new ArrayList<>();

            dataSet = new LineDataSet(S.dbHandler.getLineData(S.db, 4), "Bon");
            dataSet.setColor(Color.BLACK); // Linienfarbe
            dataSet.setCircleColor(Color.BLACK); // Punktfarbe
            dataSet.setCircleSize(5); // Punktgröße
            dataSet.setLineWidth(3f); // Dicke der Linien
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawAxisLine(true);
            xAxis.setDrawGridLines(false);
            xAxis.setAxisMaximum(3);
            xAxis.setAxisMinimum(0);
            xAxis.setLabelCount(3);
            lineChart.getAxisRight().setEnabled(false); // no right axis
            lineDataSet.add(dataSet);
            lineData = new LineData(lineDataSet);

            lineData.setValueTextSize(10f);

            if (lineChart != null) {
                this.lineChart.setTouchEnabled(false);
                this.lineChart.setData(lineData);
                this.lineChart.invalidate();

            }
        }

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        switch(viewType){
            case 0:
                bons = S.dbHandler.getNumberOfNewestBons(S.db,3);   // Holt die letzten drei Bons aus der DB
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_home_boncard_layout, parent, false);
                return new ViewHolderBonCard(itemView);
            case 1:
                budgets = S.dbHandler.getAllBudgets(S.db); // Holt sich alle Budgets (HINWEIS: wir entnehmen hier erstmal nur das erste!)
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_home_topbudget_layout, parent, false);
                return new ViewHolderBudgetCard(itemView);
            case 2:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_home_linechart_layout, parent, false);
                return new ViewHolderLinechartCard(itemView);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)){
            case 0:
                ViewHolderBonCard holderBonCard = (ViewHolderBonCard) holder;
                holderBonCard.firstContentAbove.setText(bons.get(0).getShopName());
                holderBonCard.firstContentBelow.setText(bons.get(0).getDate());
                holderBonCard.firstBonPrice.setText(bons.get(0).getTotalPrice() + curreny);
                holderBonCard.secondContentAbove.setText(bons.get(1).getShopName());
                holderBonCard.secondContentBelow.setText(bons.get(1).getDate());
                holderBonCard.secondBonPrice.setText(bons.get(1).getTotalPrice() + curreny);
                holderBonCard.thirdContentAbove.setText(bons.get(2).getShopName());
                holderBonCard.thirdContentBelow.setText(bons.get(2).getDate());
                holderBonCard.thirdBonPrice.setText(bons.get(2).getTotalPrice()+ curreny);
                holderBonCard.firstBonImage.setImageResource(R.mipmap.ic_edekalogo);
                //holderBonCard.firstFavoriteImage
                holderBonCard.secondBonImage.setImageResource(R.mipmap.ic_edekalogo);
                //holderBonCard.secondFavoriteImage
                holderBonCard.thirdBonImage.setImageResource(R.mipmap.ic_edekalogo);
                //holderBonCard.thirdFavoriteImage
                break;
            case 1:
                ViewHolderBudgetCard holderBudgetCard = (ViewHolderBudgetCard) holder;
                holderBudgetCard.budgetCurrently.setText(getRestBudget(budgets.get(0)) + curreny);
                holderBudgetCard.yearBefore.setText(budgets.get(0).getYearVon());
                holderBudgetCard.monthBefore.setText(budgets.get(0).getMonthVon());
                holderBudgetCard.yearAfter.setText(budgets.get(0).getYearBis());
                holderBudgetCard.monthAfter.setText(budgets.get(0).getMonthBis());
                holderBudgetCard.progressPercentage.setText(getRestPercentage(budgets.get(0)) + percentage);
                holderBudgetCard.tagVon.setText(budgets.get(0).getYearVon().split("\\.")[0]);
                holderBudgetCard.tagBis.setText(budgets.get(0).getYearBis().split("\\.")[0]);
                holderBudgetCard.progressBar.setProgress((int) (100 - Double.parseDouble(getRestPercentage(budgets.get(0)))));

                break;
            case 2:
                ViewHolderLinechartCard holderLinechartCard = (ViewHolderLinechartCard) holder;
                holderLinechartCard.lineChart.setBackgroundColor(555885);
                break;
        }

    }


    @Override
    public int getItemCount() {
        return count;
    }

    private String getRestBudget(C_Budget budget){
        return ""+((double) budget.getBudgetMax() - budget.getBudgetLost());
    }

    private String getRestPercentage(C_Budget budget){
        return ""+(Math.round(((Double.parseDouble(this.getRestBudget(budget)) / budget.getBudgetMax())*100) * 100) / 100.00);
    }

}