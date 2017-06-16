package de.projektss17.bonpix.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import de.projektss17.bonpix.A_Budget_Edit;
import de.projektss17.bonpix.R;
import de.projektss17.bonpix.daten.C_Budget;

public class C_Adapter_Budget extends RecyclerView.Adapter<C_Adapter_Budget.ViewHolder> {

    private List<C_Budget> budgetList;

    public C_Adapter_Budget(List<C_Budget> budgetList) {
        this.budgetList = budgetList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView budgetCurrently, yearBefore,monthBefore, yearAfter, monthAfter, title, progressPercentage, tagVon, tagBis;
        public ProgressBar progressBar;
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);
            this.cardView = (CardView) view.findViewById(R.id.budget_card_view);
            this.title = (TextView) view.findViewById(R.id.budget_title);
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

    @Override
    public C_Adapter_Budget.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_budget_content, parent, false);
        return new C_Adapter_Budget.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        C_Budget budget = budgetList.get(position);
        holder.title.setText(budget.getTitle() + " " + budget.getBudgetMax() + "€");
        holder.budgetCurrently.setText(this.getRestBudget(budget)+ " €");
        holder.monthBefore.setText(budget.getMonthVon());
        holder.yearBefore.setText(budget.getYearVon());
        holder.monthAfter.setText(budget.getMonthBis());
        holder.yearAfter.setText(budget.getYearBis());
        holder.tagVon.setText(budget.getZeitraumVon().split("\\.")[0]);
        holder.tagBis.setText(budget.getZeitraumBis().split("\\.")[0]);
        holder.progressBar.setProgress((int) (100 - Double.parseDouble(this.getRestPercentage(budget))));
        holder.progressPercentage.setText(this.getRestPercentage(budget)+" %");
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), A_Budget_Edit.class);
                i.putExtra("state", "edit");
                i.putExtra("budget", budgetList.get(position).getId() + "");
                v.getContext().startActivity(i);
            }
        });
    }

    private String getRestBudget(C_Budget budget){
        return "" + ((double) budget.getBudgetMax() - budget.getBudgetLost());
    }

    private String getRestPercentage(C_Budget budget){
        return "" + (Math.round(((Double.parseDouble(this.getRestBudget(budget)) / budget.getBudgetMax()) * 100) * 100) / 100.00);
    }

    @Override
    public int getItemCount() {
        return budgetList.size();
    }
}