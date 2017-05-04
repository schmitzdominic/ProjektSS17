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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import de.projektss17.bonpix.daten.C_Bons;
import de.projektss17.bonpix.daten.C_Bons_Adapter;

public class A_Tab1Home extends Fragment {

    // RECYCLERVIEW VARIABLEN

    private List<C_Bons> bonsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private C_Bons_Adapter mAdapter;

    // CHART VARIABLEN

    private PieChart pieChart;
    private ArrayList<PieEntry> data = new ArrayList<>();
    private ArrayList<String> label = new ArrayList<>();;
    private BarChart barChart;
    private ArrayList<BarEntry> BARENTRY;
    private ArrayList<IBarDataSet> BarEntryLabels;
    BarDataSet Bardataset;
    BarData BarDaten;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.box_tab1_home_content, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.view_home_bons);
        mAdapter = new C_Bons_Adapter(bonsList);
        prepareBonData();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        pieChart = (PieChart) rootView.findViewById(R.id.chart1);

        // Label initialisierung

        label.add("Algerie");
        label.add("Maroc");
        label.add("TUnisie");

        // Data initialisierung

        data.add(new PieEntry(0.2f, 0));
        data.add(new PieEntry(0.2f, 1));
        data.add(new PieEntry(0.50f, 2));
        final PieDataSet dataSet = new PieDataSet(data, "Test");
        final PieData pieData = new PieData(dataSet);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieChart.setCenterText("Pie Chart");
        pieChart.setData(pieData);
        pieChart.animateY(1000);
        pieChart.setBackgroundColor(5);
        pieData.setDrawValues(true);





        BarChart bchart = (BarChart) rootView.findViewById(R.id.chart2);


        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = (int) 0; i < 10 + 1; i++) {
            float val = (float) (Math.random());
            yVals1.add(new BarEntry(i, val));
        }

        BarDataSet set1;

        set1 = new BarDataSet(yVals1, "The year 2017");
        set1.setColors(ColorTemplate.MATERIAL_COLORS);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);

        bchart.setTouchEnabled(false);
        bchart.setData(data);
        return rootView;








/*
        barChart = (BarChart) rootView.findViewById(R.id.chart2);

        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<IBarDataSet>();

        AddValuesToBARENTRY();
        AddValuesToBarEntryLabels();

        Bardataset = new BarDataSet(BARENTRY, "Projects");


        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        barChart.setData(BarDaten);

        barChart.animateY(3000);



        return rootView;

    }

    public void AddValuesToBARENTRY(){

        BARENTRY.add(new BarEntry(2f, 0));
        BARENTRY.add(new BarEntry(4f, 1));
        BARENTRY.add(new BarEntry(6f, 2));
        BARENTRY.add(new BarEntry(8f, 3));
        BARENTRY.add(new BarEntry(7f, 4));
        BARENTRY.add(new BarEntry(3f, 5));

    }

    public void AddValuesToBarEntryLabels(){

        BarEntryLabels.add("Januar");
        BarEntryLabels.add("Februar");
        BarEntryLabels.add("Maerz");
        BarEntryLabels.add("April");
        BarEntryLabels.add("Mai");
        BarEntryLabels.add("Juni");

    }
    */
    }


    private void prepareBonData(){
        for(int i = 0; i < 3; i++) {
            C_Bons bons = new C_Bons("TEST"+i, "TEST"+(char)(i+65), "Test", "Test", "Test");
            bonsList.add(bons);
        }
    }
}
