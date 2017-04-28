package de.projektss17.bonpix.daten;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.projektss17.bonpix.R;

/**
 * Created by Carnivorus on 28.04.2017.
 */

public class C_Budget_CardView_Adapter extends RecyclerView.Adapter<C_Budget_CardView_Adapter.ViewHolder> {

    private List<C_Budget> budgetList;

    public C_Budget_CardView_Adapter(List<C_Budget> budgetList) {
        this.budgetList = budgetList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView budget, turnus;

        public ViewHolder(View view) {
            super(view);

            budget = (TextView) view.findViewById(R.id.budget_content_gesamt);
            turnus = (TextView) view.findViewById(R.id.budget_card_view_content_desc);
        }
    }

    @Override
    public C_Budget_CardView_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_budget_card_view, parent, false);
        return new C_Budget_CardView_Adapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        C_Budget budget = budgetList.get(position);

        //holder.icon.setImageDrawable(rounderBitmapDrawable);
        holder.budget.setText(budget.getBudget());
        holder.turnus.setText(budget.getTurnus());
    }

    @Override
    public int getItemCount() {
        return budgetList.size();

    }
}
