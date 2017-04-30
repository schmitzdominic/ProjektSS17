package de.projektss17.bonpix.daten;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import de.projektss17.bonpix.R;

/**
 * Created by Johanns on 28.04.2017.
 */

public class C_Budget_CardView_Adapter extends RecyclerView.Adapter<C_Budget_CardView_Adapter.ViewHolder> {

    private List<C_Budget> budgetList;

    public C_Budget_CardView_Adapter(List<C_Budget> budgetList) {
        this.budgetList = budgetList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView budgetCurrently, turnus, title, progressPercentage;
        public ProgressBar progressBar;


        public ViewHolder(View view) {
            super(view);

            this.title = (TextView) view.findViewById(R.id.budget_title);
            this.budgetCurrently = (TextView) view.findViewById(R.id.budget_content);
            this.turnus = (TextView) view.findViewById(R.id.budget_turnus);
            this.progressBar = (ProgressBar)view.findViewById(R.id.budget_progress_bar);
            this.progressPercentage = (TextView) view.findViewById(R.id.budget_progress_percentage);

        }
    }

    @Override
    public C_Budget_CardView_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_budget_content, parent, false);
        return new C_Budget_CardView_Adapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        C_Budget budget = budgetList.get(position);

        holder.title.setText(budget.getTitle());
        holder.budgetCurrently.setText(budget.getBudgetCurrently()+ " €");
        holder.turnus.setText(budget.getTurnus());
        holder.progressBar.setProgress(budget.getProcessbar());
        holder.progressPercentage.setText(budget.getProcessPercentage()+" %");
    }

    @Override
    public int getItemCount() {
        return budgetList.size();

    }

}
