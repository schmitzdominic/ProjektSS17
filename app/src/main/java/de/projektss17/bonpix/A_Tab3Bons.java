package de.projektss17.bonpix;

/**
 * Created by Domi on 28.03.2017.
 * Hier bitte die Logik des dritten Tabs
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class A_Tab3Bons extends Fragment {

    private RecyclerView recView;
    private C_Bons_RecyclerView_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.box_tab3_bons_content, container, false);
        recView = (RecyclerView)rootView.findViewById(R.id.rec_list);
        recView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        adapter = new C_Bons_RecyclerView_Adapter(C_Recyclerview_Data.getListData(), container.getContext());
        recView.setAdapter(adapter);
        return rootView;
    }
}
