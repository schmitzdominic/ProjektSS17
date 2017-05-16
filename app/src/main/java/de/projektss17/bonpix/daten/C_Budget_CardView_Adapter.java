package de.projektss17.bonpix.daten;

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


public class C_Budget_CardView_Adapter extends RecyclerView.Adapter<C_Budget_CardView_Adapter.ViewHolder> {


    private List<C_Budget> budgetList;      // Gespeicherte Objekte für die View (CardView)

    public C_Budget_CardView_Adapter(List<C_Budget> budgetList) {
        this.budgetList = budgetList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView budgetCurrently, yearBefore,monthBefore, yearAfter, monthAfter, title, progressPercentage;
        public ProgressBar progressBar;
        public CardView cardView;


        public ViewHolder(View view) {
            super(view);
            this.cardView = (CardView) view.findViewById(R.id.budget_card_view);

            // Implementierung des Layouts der einzelnen Objekte in der View
            this.title = (TextView) view.findViewById(R.id.budget_title);
            this.budgetCurrently = (TextView) view.findViewById(R.id.budget_content);
            this.monthBefore = (TextView) view.findViewById(R.id.budget_monat_von);
            this.yearBefore = (TextView) view.findViewById(R.id.budget_jahr_von);
            this.monthAfter = (TextView) view.findViewById(R.id.budget_monat_bis);
            this.yearAfter = (TextView) view.findViewById(R.id.budget_jahr_bis);
            this.progressBar = (ProgressBar)view.findViewById(R.id.budget_progress_bar_circle);
            this.progressPercentage = (TextView) view.findViewById(R.id.budget_progress_percentage);

        }
    }


    @Override
    public C_Budget_CardView_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // VIEW - Implementierung einer View und Rückgabe des fertig gebaueten Objekts
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_budget_content, parent, false);
        return new C_Budget_CardView_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        // VIEW - Inhalte werden auf die jeweilige Position des Layouts gesetzt
        C_Budget budget = budgetList.get(position);
        holder.title.setText(budget.getTitle());
        holder.budgetCurrently.setText(this.getRestBudget(budget)+ " €");
        holder.monthBefore.setText(budget.getMonthVon());
        holder.yearBefore.setText(budget.getYearVon());
        holder.monthAfter.setText(budget.getMonthBis());
        holder.yearAfter.setText(budget.getYearBis());
        holder.progressBar.setProgress((int) (100 - Double.parseDouble(this.getRestPercentage(budget))));
        holder.progressPercentage.setText(this.getRestPercentage(budget)+" %");

        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), A_Budget_Edit.class);
                i.putExtra("state", "edit");
                i.putExtra("budget", budgetList.get(position).getId()+"");
                v.getContext().startActivity(i);
            }
        });

    }

    private String getRestBudget(C_Budget budget){
        return ""+((double) budget.getBudgetMax() - budget.getBudgetLost());
    }

    private String getRestPercentage(C_Budget budget){
        return ""+((Double.parseDouble(this.getRestBudget(budget)) / budget.getBudgetMax())*100);
    }

    @Override
    public int getItemCount() {
        return budgetList.size();

    }
}