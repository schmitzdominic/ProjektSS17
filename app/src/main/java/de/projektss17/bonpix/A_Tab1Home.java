package de.projektss17.bonpix;

/**
 * Created by Domi on 28.03.2017.
 * Hier bitte die Logik des ersten Tabs
 */

import android.graphics.Color;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.daten.C_Home_Adapter;

public class A_Tab1Home extends Fragment {


    private List<C_Bon> bonsList = new ArrayList<>();
    private RecyclerView Recyclerview;
    private C_Home_Adapter mAdapter1;

    private View rootView;

    LineChart chart;
    LineDataSet dataSet;
    ArrayList<ILineDataSet> lineDataSet;
    LineData lineData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.box_tab1_home_content, container, false);
        Recyclerview = (RecyclerView) rootView.findViewById(R.id.tab_eins_recyclerview_bons);
        mAdapter1 = new C_Home_Adapter(bonsList);
        prepareHomeData();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        Recyclerview.setLayoutManager(mLayoutManager);
        Recyclerview.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        Recyclerview.setItemAnimator(new DefaultItemAnimator());
        Recyclerview.setAdapter(mAdapter1);
        mAdapter1.notifyDataSetChanged();
        chart = (LineChart) rootView.findViewById(R.id.chart);

        // Chart Einstellungen:

        chart.animateXY(2000, 4000);
        chart.setPadding(30, 30, 30, 30);

        if(bonsList.size() != 0){
            prepareLineData();
        }

        return rootView;
    }

    private void prepareLineData() {
        this.lineDataSet = new ArrayList<>();

        // Liste wird mit Daten aus der Datenbank befüllt

        this.dataSet = new LineDataSet(S.dbHandler.getLineData(S.db, 4), "Bon");
        dataSet.setColor(Color.BLACK); // Linienfarbe
        dataSet.setCircleColor(Color.BLACK); // Punktfarbe
        dataSet.setCircleSize(5); // Punktgröße
        dataSet.setLineWidth(3f); // Dicke der Linien
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(3);
        xAxis.setAxisMinimum(0);
        xAxis.setLabelCount(3);
        chart.getAxisRight().setEnabled(false); // no right axis
        this.lineDataSet.add(this.dataSet);
        this.lineData = new LineData(this.lineDataSet);


        this.lineData.setValueTextSize(10f);

        if (chart != null) {
            this.chart.setTouchEnabled(false);
            this.chart.setData(this.lineData);
            this.chart.invalidate();

        }
    }

    // TODO: 21.05.2017 aktuelles Budget aus der Datenbank holen
    private void prepareBudgetData(){

    }

    private void prepareHomeData() {
        if(S.dbHandler.getAllBons(S.db).size() != 0){
            bonsList.clear();
            int count = 0;
            for (C_Bon bon : S.dbHandler.getAllBons(S.db)) {
                if (count == 3) {
                    break;
                }
                bonsList.add(bon);
                count++;
            }
        }
    }

}