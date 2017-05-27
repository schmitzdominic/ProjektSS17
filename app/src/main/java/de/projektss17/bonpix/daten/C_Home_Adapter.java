package de.projektss17.bonpix.daten;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.util.ArrayList;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;

public class C_Home_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int count = 3;
    private ArrayList<C_Bon> bons;
    private ArrayList<C_Budget> budgets;


    // LAYOUT BonCard
    // (Hinweis: Drei feste Inhalte! Dynamisch würde kein Sinn machen, da hierfür die Tab Bons bereit steht! )
    public class ViewHolderBonCard extends RecyclerView.ViewHolder {

        public TextView firstContentAbove, firstContentBelow, secondContentAbove, secondContentBelow,
                thirdContentAbove, thirdContentBelow;
        public ImageView firstBonImage, firstFavoriteImage, secondBonImage, secondFavoriteImage,
                thirdBonImage, thirdFavoriteImage;

        public ViewHolderBonCard(View view) {
            super(view);

            // Implementierung des Layouts der einzelnen Objekte für die CardView
            this.firstContentAbove = (TextView)view.findViewById(R.id.tab_home_boncard_first_bon_above_content);
            this.firstContentBelow = (TextView)view.findViewById(R.id.tab_home_boncard_first_bon_below_content);
            this.secondContentAbove = (TextView)view.findViewById(R.id.tab_home_boncard_second_bon_above_content);
            this.secondContentBelow = (TextView)view.findViewById(R.id.tab_home_boncard_second_bon_below_content);
            this.thirdContentAbove = (TextView)view.findViewById(R.id.tab_home_boncard_third_bon_above_content);
            this.thirdContentBelow = (TextView)view.findViewById(R.id.tab_home_boncard_third_bon_below_content);

            this.firstBonImage = (ImageView)view.findViewById(R.id.tab_home_boncard_first_bon_big_image);
            this.firstFavoriteImage = (ImageView)view.findViewById(R.id.tab_home_boncard_first_bon_small_image);
            this.secondBonImage = (ImageView)view.findViewById(R.id.tab_home_boncard_second_bon_big_image);
            this.secondFavoriteImage = (ImageView)view.findViewById(R.id.tab_home_boncard_second_bon_small_image);
            this.thirdBonImage = (ImageView)view.findViewById(R.id.tab_home_boncard_third_bon_big_image);
            this.thirdFavoriteImage = (ImageView)view.findViewById(R.id.tab_home_boncard_third_bon_small_image);

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

        bons = S.dbHandler.getAllBons(S.db);
        budgets = S.dbHandler.getAllBudgets(S.db);

        View itemView;

        switch(viewType){
            case 0:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_home_boncard_layout, parent, false);
                return new ViewHolderBonCard(itemView);

            case 1:
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
                holderBonCard.firstContentAbove.setText("Test");
                break;
            case 1:
                ViewHolderBudgetCard holderBudgetCard = (ViewHolderBudgetCard) holder;
                holderBudgetCard.budgetCurrently.setText("TEST");
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

}