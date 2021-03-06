package de.projektss17.bonpix;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.projektss17.bonpix.adapter.C_Adapter_Statistik;

public class A_Tab2Statistik extends Fragment{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private C_Adapter_Statistik mAdapter;
    public FloatingActionButton fabPlus;
    private TabLayout tabLayout;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.box_tab2_statistik_content, container, false);
        this.setHasOptionsMenu(false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.statistik_recyclerview);
        this.fabPlus = ((A_Main) getActivity()).getFloatingActionButtonPlus();
        this.tabLayout = (((A_Main) getActivity()).getTimeTabLayout());

        this.mAdapter = new C_Adapter_Statistik();
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setAdapter(mAdapter);
        this.getTabData(this.tabLayout.getSelectedTabPosition());

        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) {
                    if (fabPlus.isShown()) { // Scroll Down
                        if (((A_Main) getActivity()).getFabState()) {
                            ((A_Main) getActivity()).closeFABMenu();
                        }
                        fabPlus.hide();
                    }
                }
                else if (dy <0) {
                    if (!fabPlus.isShown()) { // Scroll Up
                        fabPlus.show();
                    }
                }
            }
        });

        // Click eines Tabs bewirkt eine Aktion (in diesem Fall sollen die Charts gefiltert werden)
        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getTabData(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {} // MÜSSEN LEIDER mit implementiert werden, machen jedoch nichts!

            @Override
            public void onTabReselected(TabLayout.Tab tab) {} // MÜSSEN LEIDER mit implementiert werden, machen jedoch nichts!
        });
    }

    /**
     * Triggert Statistken je nach Position
     * @param position Position
     */
    public void getTabData(int position){
        switch (position){
            case 0:
                mAdapter.createFilteredData("ALLE", this.getContext());
                mAdapter.notifyDataSetChanged();
                break;
            case 1:
                mAdapter.createFilteredData("WOCHE", this.getContext());
                mAdapter.notifyDataSetChanged();
                break;
            case 2:
                mAdapter.createFilteredData("MONAT", this.getContext());
                mAdapter.notifyDataSetChanged();
                break;
            case 3:
                mAdapter.createFilteredData("QUARTAL", this.getContext());
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
}