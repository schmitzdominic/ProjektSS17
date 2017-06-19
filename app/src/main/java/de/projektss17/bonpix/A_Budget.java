package de.projektss17.bonpix;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

import de.projektss17.bonpix.daten.C_Budget;
import de.projektss17.bonpix.daten.C_Adapter_Budget;

public class A_Budget extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<C_Budget> budgetList = new ArrayList<>();
    private RecyclerView.Adapter bAdapter;
    private FloatingActionButton fab;
    private ItemTouchHelper swipper;
    private CardView card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_budget_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerView = (RecyclerView) findViewById(R.id.view_budget); // Recycler Liste
        bAdapter = new C_Adapter_Budget(budgetList);          // CardView Adapter
        card = (CardView) findViewById(R.id.budget_card_view);
        this.fab = (FloatingActionButton) findViewById(R.id.budget_fab);    //Floating Action Button

        swipper = new ItemTouchHelper(createHelperCallBack());      // ItemTouch -> Swipper
        swipper.attachToRecyclerView(recyclerView);

        onStartProofAndCreate();

        fab.setOnClickListener(new View.OnClickListener() {

            /**
             * Funktion die beim drücken des FAB ausgeführt wird
             * @param v Übergabe einer view - In diesem Fall die View für die Card View
             */
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(A_Budget.this, A_Budget_Edit.class);
                intent.putExtra("state","new");
                startActivity(intent);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) {
                    // Scroll Down
                    if (fab.isShown()) {
                        fab.hide();

                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!fab.isShown()) {
                        fab.show();
                    }
                }
            }
        });
    }

    /**
     * Methode soll die Daten aus der DB lesen und die RecyclerView damit befüllen
     */
    public void onStartProofAndCreate() {

        for (C_Budget budget : S.dbHandler.getAllBudgets(S.db)) {
            S.dbHandler.refreshBudget(S.db, budget);
        }

        budgetList.clear();
        for (C_Budget budget : S.dbHandler.getAllBudgets(S.db)) {
            this.addItem(budget);
        }
    }

    private ItemTouchHelper.Callback createHelperCallBack() {

        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {

                moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;    // true wenn geswipped wird, ansonsten false
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                deleteItem(viewHolder.getAdapterPosition());
                S.outShort(A_Budget.this, "Budget wurde gelöscht!");
            }
        };
    }

    /**
     * MOVE ITEM - Bewegen eines Items in der RecyclerView (Swip nach Oben oder Unten)
     */
    private void moveItem(int oldPos, int newPos) {

        C_Budget item = (C_Budget) budgetList.get(oldPos);
        budgetList.get(oldPos);
        budgetList.remove(oldPos);
        budgetList.add(newPos, item);
        bAdapter.notifyItemMoved(oldPos, newPos);

    }

    /**
     * DELETE ITEM - Löschen eines Items in der RecyclerView (Swip nach Links)
     */
    private void deleteItem(int position) {

        S.dbHandler.removeBudget(S.db, budgetList.get(position).getId());
        budgetList.remove(position);
        bAdapter.notifyItemRemoved(position);
    }

    /**
     * ADD ITEM - Befüllung der RecyclerView mit einem Budget
     */
    public void addItem(C_Budget budget) {

        budgetList.add(budget);

        layoutManager = new LinearLayoutManager(A_Budget.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bAdapter);
        bAdapter.notifyDataSetChanged();

    }
}