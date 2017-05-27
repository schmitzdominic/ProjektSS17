package de.projektss17.bonpix.daten;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private int count = 3;                      // Anzahlt der Items in der RecyclerView - derzeit 3 feste Cards!
    private Context context;                    // Context der Hauptactivity (Tab_Home) zur weiteren Verarbeitung
    private ArrayList<C_Bon> bons;              // Sammlung der jeweiligen ausgewählten Bons aus der DB zur weiteren Verarbeitung
    private ArrayList<C_Budget> budgets;        // Sammlung der jeweiligen ausgewählten Budgets aus der DB zur weitren Verarbeitung
    private String curreny, percentage;         // Feste String aus der String-XML (Für '€'-Zeichen und '%'-Zeichen


    public C_Home_Adapter(Context context) {
        this.context = context;
        this.curreny = context.getString(R.string.currency_europe);
        this.percentage = context.getString(R.string.percentage);
    }

    // LAYOUT BonCard
    // (Hinweis: Drei feste Inhalte! Dynamisch würde kein Sinn machen,
    //  da hierfür die Tab Bons bereit steht und die implementierung möglicherweise nicht funktioniert! )
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
            this.firstContentAbove = (TextView) view.findViewById(R.id.tab_home_boncard_first_bon_above_content);
            this.firstContentBelow = (TextView) view.findViewById(R.id.tab_home_boncard_first_bon_below_content);
            this.firstBonPrice = (TextView) view.findViewById(R.id.tab_home_boncard_first_bon_betrag);
            this.secondContentAbove = (TextView) view.findViewById(R.id.tab_home_boncard_second_bon_above_content);
            this.secondContentBelow = (TextView) view.findViewById(R.id.tab_home_boncard_second_bon_below_content);
            this.secondBonPrice = (TextView) view.findViewById(R.id.tab_home_boncard_second_bon_betrag);
            this.thirdContentAbove = (TextView) view.findViewById(R.id.tab_home_boncard_third_bon_above_content);
            this.thirdContentBelow = (TextView) view.findViewById(R.id.tab_home_boncard_third_bon_below_content);
            this.thirdBonPrice = (TextView) view.findViewById(R.id.tab_home_boncard_third_bon_betrag);


            //ToDo - BonImages Rund machen (sind derzeit viereckig)
            this.firstBonImage = (ImageView) view.findViewById(R.id.tab_home_boncard_first_bon_small_image);
            this.firstFavoriteImage = proofFavorite(bons.get(0), (ImageView) view.findViewById(R.id.tab_home_boncard_first_bon_big_image));
            this.secondBonImage = (ImageView) view.findViewById(R.id.tab_home_boncard_second_bon_small_image);
            this.secondFavoriteImage = proofFavorite(bons.get(1), (ImageView) view.findViewById(R.id.tab_home_boncard_second_bon_big_image));
            this.thirdBonImage = (ImageView) view.findViewById(R.id.tab_home_boncard_third_bon_small_image);
            this.thirdFavoriteImage = proofFavorite(bons.get(2), (ImageView) view.findViewById(R.id.tab_home_boncard_third_bon_big_image));

            //Implementierung der Invisible-Buttons zum Auswählen der zuletzt eingescannten Bons
            this.bon1 = (Button) view.findViewById(R.id.tab_home_boncard_first_bon);
            this.bon2 = (Button) view.findViewById(R.id.tab_home_boncard_second_bon);
            this.bon3 = (Button) view.findViewById(R.id.tab_home_boncard_third_bon);


            //ToDo - Hier muss ein onClick-Listener für die oberen drei Buttons (bon1, ...) implementiert werden
            // ---->> onClick bewirkt eine startActivity (Intent) an die Activity Bon_Anzeigen
            // HINWEIS: je nachdem welchen Bon man anklickt, sollen diese Daten an die Bon_Anzeigen weitergeleitet und autom. befüllt werden

            // INFO: Implementierung im nächsten Sprint, da parallel zu diesem Item Bon_Anzeigen überarbeitet wurde!!!!

        }
    }


    // LAYOUT BudgetCard
    // INFO: 1:1 Layout von der Budgetierungs-Activity
    public class ViewHolderBudgetCard extends RecyclerView.ViewHolder {

        public TextView budgetCurrently, yearBefore, monthBefore, yearAfter, monthAfter, progressPercentage, tagVon, tagBis;
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
            this.progressBar = (ProgressBar) view.findViewById(R.id.budget_progress_bar_circle);
            this.progressPercentage = (TextView) view.findViewById(R.id.budget_progress_percentage);

        }
    }


    // LAYOUT LineChartCard
    //Diese Logik war bereits gegeben vom vorherigen Sprint! (Bei Fragen an den jeweiligen wenden!)
    public class ViewHolderLinechartCard extends RecyclerView.ViewHolder {

        public LineChart lineChart;

        public ViewHolderLinechartCard(View view) {
            super(view);

            //ToDo - Es muss noch entschieden weren, welche Daten ausgelesen und ausgewertet werden
            LineDataSet dataSet;
            ArrayList<ILineDataSet> lineDataSet;
            LineData lineData;

            this.lineChart = (LineChart) view.findViewById(R.id.tab_home_chartcard_linechart);
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

        switch (viewType) {
            case 0:
                bons = S.dbHandler.getNumberOfNewestBons(S.db, 3);   // Holt die letzten drei Bons aus der DB
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_home_boncard_layout, parent, false);
                return new ViewHolderBonCard(itemView);
            case 1:
                budgets = S.dbHandler.getAllBudgets(S.db); // Holt sich alle Budgets (HINWEIS: wir entnehmen hier erstmal nur das erste!)
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_home_topbudget_layout, parent, false);
                return new ViewHolderBudgetCard(itemView);
            case 2:
                //ToDo - Hier muss überlegt werden, was ausgewertet wird, um dementsprechend die Daten aus der DB zu ziehen!
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_home_linechart_layout, parent, false);
                return new ViewHolderLinechartCard(itemView);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //HINWEIS: Derzeit sind alle drei Cards statisch. Die Inhalte sind jedoch DYNAMISCH!
        switch (getItemViewType(position)) {
            case 0:
                final ViewHolderBonCard holderBonCard = (ViewHolderBonCard) holder;
                holderBonCard.firstContentAbove.setText(bons.get(0).getShopName());
                holderBonCard.firstContentBelow.setText(bons.get(0).getDate());
                holderBonCard.firstBonPrice.setText(bons.get(0).getTotalPrice() + curreny);
                holderBonCard.secondContentAbove.setText(bons.get(1).getShopName());
                holderBonCard.secondContentBelow.setText(bons.get(1).getDate());
                holderBonCard.secondBonPrice.setText(bons.get(1).getTotalPrice() + curreny);
                holderBonCard.thirdContentAbove.setText(bons.get(2).getShopName());
                holderBonCard.thirdContentBelow.setText(bons.get(2).getDate());
                holderBonCard.thirdBonPrice.setText(bons.get(2).getTotalPrice() + curreny);
                holderBonCard.firstBonImage.setImageResource(R.mipmap.ic_edekalogo);

                holderBonCard.secondBonImage.setImageResource(R.mipmap.ic_edekalogo);
                holderBonCard.thirdBonImage.setImageResource(R.mipmap.ic_edekalogo);


                // Erster Favoriten-Button - Beim Drücken wird Funktion setAndChangeFavorite aufgerufen
                holderBonCard.firstFavoriteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holderBonCard.firstFavoriteImage = setAndChangeFavorite(bons.get(0),holderBonCard.firstFavoriteImage);
                    }
                });


                // Zweiter Favoriten-Button - Beim Drücken wird Funktion setAndChangeFavorite aufgerufen
                holderBonCard.secondFavoriteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holderBonCard.secondFavoriteImage = setAndChangeFavorite(bons.get(1),holderBonCard.secondFavoriteImage);
                    }
                });


                // Dritter Favoriten-Button - Beim Drücken wird Funktion setAndChangeFavorite aufgerufen
                holderBonCard.thirdFavoriteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holderBonCard.thirdFavoriteImage = setAndChangeFavorite(bons.get(2),holderBonCard.thirdFavoriteImage);
                    }
                });

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
                //ToDo - Hier muss noch überlegt werden, was genau ausgewertet werden soll!
                ViewHolderLinechartCard holderLinechartCard = (ViewHolderLinechartCard) holder;
                holderLinechartCard.lineChart.setBackgroundColor(555885);
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
    private String getRestBudget(C_Budget budget) {
        return "" + ((double) budget.getBudgetMax() - budget.getBudgetLost());
    }


    /**
     * Errechnet den restlichen Budgetbetrag als Prozent aus und gibt diesen als String zurück
     * @param budget Ein ausgewähltes Budget aus der DB ( Derzeit wird immer das erste genommen! )
     * @return Rückgabe des Restbetrags als Prozent
     */
    private String getRestPercentage(C_Budget budget) {
        return "" + (Math.round(((Double.parseDouble(this.getRestBudget(budget)) / budget.getBudgetMax()) * 100) * 100) / 100.00);
    }


    /**
     * Prüft beim erstellen des Layouts, komm der Bon ein Favorit ist oder nicht
     * INFO: Wenn Favorit true dann wird die ImageView dunkel befüllt, anderfalls bleibt der Inhalt unausgefüllt
     * @param bon Übergabe eines ausgewählten Bons, welches überprüft wird
     * @param favImage Übergabe der ImageView, die geändert wird ( In diesem Fall Favoriten-Image)
     * @return Rückgabe des übergebenen, bereits veränderten, Images
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
     * Wird beim onClick-Listener aufgerufen - Ändert die Farbe des ImageViews und setzt den Favoriten-Boolean entsprechend des Kriteriums
     * INFO: Prüft ob true - wenn ja wird auf false gesetzt und die DB mit dem Bon geupdatet - Das selbe umgekehrt mit Anfangswert false !
     * @param bon Übergabe eines ausgewählten Bons, welches überprüft wird
     * @param favImage Übergabe der ImageView, die geändert wird ( In diesem Fall Favoriten-Image)
     * @return Rückgabe des übergebenen, bereits veränderten, Images
     */
    private ImageView setAndChangeFavorite(final C_Bon bon, final ImageView favImage) {

        if(bon.getFavourite()){
            favImage.setImageDrawable(favImage.getContext().getResources().getDrawable(R.drawable.star_outline));
            favImage.setColorFilter(favImage.getContext().getResources().getColor(R.color.colorPrimary));
            bon.setFavourite(false);
            S.dbHandler.updateBon(S.db, bon);
            return favImage;
        } else {
            favImage.setImageDrawable(favImage.getContext().getResources().getDrawable(R.drawable.star));
            favImage.setColorFilter(favImage.getContext().getResources().getColor(R.color.colorPrimary));
            bon.setFavourite(true);
            S.dbHandler.updateBon(S.db, bon);
            return favImage;
        }


    }
}