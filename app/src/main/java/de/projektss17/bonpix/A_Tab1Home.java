package de.projektss17.bonpix;

/**
 * Created by Domi on 28.03.2017.
 * Hier bitte die Logik des ersten Tabs
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.daten.C_Bons_Adapter;

public class A_Tab1Home extends Fragment {

    // RECYCLERVIEW VARIABLEN

    private List<C_Bon> bonsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private C_Bons_Adapter mAdapter;

    // CHART VARIABLEN

    private PieChart pieChart1, pieChart2;
    private BarChart bChart, bChart3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.box_tab1_home_content, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.view_home_bons);
        mAdapter = new C_Bons_Adapter(bonsList);
        prepareHomeData();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        // Unterstreichen der Textviews

        TextView textView = (TextView) rootView.findViewById(R.id.home_title1);
        SpannableString content = new SpannableString("Ihre letzten Bons:");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);

        TextView textView1 = (TextView) rootView.findViewById(R.id.home_title2);
        SpannableString content1 = new SpannableString("Ihre Statistik:");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        textView1.setText(content1);

        // Kuchendiagramm
        pieChart1 = (PieChart) rootView.findViewById(R.id.chart1);
        createPieChart(pieChart1, "Test");

        /*//Balkendiagramm 1
        bChart = (BarChart) rootView.findViewById(R.id.chart2);
        ArrayList<IBarDataSet> dataSets1 = new ArrayList<>();
        createBarChart(bChart, dataSets1, "The year 2017");

        //Balkendiagramm 2
        bChart3 = (BarChart) rootView.findViewById(R.id.chart3);
        ArrayList<IBarDataSet> dataSets3 = new ArrayList<>();
        createBarChart(bChart3, dataSets3, "The year 2018");*/

        return rootView;
    }


    private void prepareHomeData(){
        bonsList.clear();
        int count = 0;
            for (C_Bon bon : S.dbHandler.getAllBons(S.db)) {
                if(count == 3){
                    break;
                }
                bonsList.add(bon);
                count++;
            }
    }

    /**
     * Erstellt ein Balkendiagramm
     * Zuerst werden die Werte gefüllt und dann in das Diagramm eingelesen
     */
    private void createBarChart(BarChart bar, ArrayList<IBarDataSet> daten, String name){
        ArrayList<BarEntry> val = new ArrayList<BarEntry>();
        prepareBarData(val);   // Daten werden gefüllt

        BarDataSet set = new BarDataSet(val, name);
        set.setColors(ColorTemplate.MATERIAL_COLORS);

        daten.add(set);
        BarData data = new BarData(daten);

        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);

        bar.setTouchEnabled(false);
        bar.setData(data);
    }

    /**
     * Füllt die Values eines Balkendiagramms mit random Werten
     */
    private void prepareBarData(ArrayList<BarEntry> values){
        for (int i = 0; i < 10 + 1; i++) {
            float val = (float) (Math.random());
            values.add(new BarEntry(i, val));
        }
    }

    /**
     * Erstellt ein Kreisdiagramm
     * Zuerst werden die Werte gefüllt und dann in das Diagramm eingelesen
     */
    private void createPieChart(PieChart bar, String name){
        ArrayList<PieEntry> val = new ArrayList<>();
        preparePieData(val);    // Daten werden gefüllt

        final PieDataSet dataSet = new PieDataSet(val, name);
        final PieData pieData = new PieData(dataSet);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        bar.setCenterText("Pie Chart");
        bar.setData(pieData);
        bar.animateY(1000);
        bar.setBackgroundColor(5);
        pieData.setDrawValues(true);
    }

    /**
     * Füllt die Values eines Kreisdiagramms mit Werten
     */
    private void preparePieData(ArrayList<PieEntry> values){
        values.add(new PieEntry(0.2f, 0));
        values.add(new PieEntry(0.2f, 1));
        values.add(new PieEntry(0.50f, 2));
    }
}
