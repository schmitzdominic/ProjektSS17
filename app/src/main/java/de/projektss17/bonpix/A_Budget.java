
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
import de.projektss17.bonpix.daten.C_Budget_CardView_Adapter;


public class A_Budget extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<C_Budget> budgetList = new ArrayList<>();
    private RecyclerView.Adapter bAdapter;
    private FloatingActionButton fab;
    private ItemTouchHelper swipper;
    private CardView card;


    /**
     * Content für diese Activity wird erstellt / gebaut / vorbereitet
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_budget_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // LAYOUT - Implementierung aller Layouts
        recyclerView = (RecyclerView) findViewById(R.id.view_budget); // Recycler Liste
        bAdapter = new C_Budget_CardView_Adapter(budgetList);          // CardView Adapter
        card = (CardView) findViewById(R.id.budget_card_view);


        // FAB - Drücken öffnet ein Dialog mit Eingabefeldern
        this.fab = (FloatingActionButton) findViewById(R.id.budget_fab);    //Floating Action Button
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


        // SWIPPER - Implementierung des Swipper-Funktion
        swipper = new ItemTouchHelper(createHelperCallBack());      // ItemTouch -> Swipper
        swipper.attachToRecyclerView(recyclerView);

        onStartProofAndCreate();

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


    @Override
    public void onClick(View v) {

    }


    /**
     * Methode soll die Daten aus der DB lesen und die RecyclerView damit befüllen
     * (sobald DB vorhanden ist wird hier weiter programmiert)
     */
    public void onStartProofAndCreate() {

        for (C_Budget budget : S.dbHandler.getAllBudgets(S.db)) {
            S.dbHandler.refreshBudget(S.db, budget);
        }

        for (C_Budget budget : S.dbHandler.getAllBudgets(S.db)) {
            this.addItem(budget);
        }
    }


    /**
     * Created by Johanns am 30.04.2017
     * <p>
     * CREATE HELPER CALLBACK - Funktion zum Swippen (bewegen od. löschen von Items durch swippen)
     *
     * @return ItemTouchHelper (Swipper-Funktion) wird zurückgegeben
     */
    private ItemTouchHelper.Callback createHelperCallBack() {

        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {

                // CARD VIEW - Items können bewegt werden (oben nach unten und umgekehrt)
                moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;    // true wenn geswipped wird, ansonsten false
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {

                // CARD VIEW - Items werden gelöscht (nach Rechts oder Links)
                deleteItem(viewHolder.getAdapterPosition());
                S.outShort(A_Budget.this, "Budget wurde gelöscht!");

            }


        };
    }

    /**
     * MOVE ITEM - Bewegen eines Items in der RecyclerView (Swip nach Oben oder Unten)
     *
     * @param oldPos Alte Postion der View wird übergeben
     * @param newPos Neue Postition der View wird übergeben
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
     *
     * @param position Position der jeweiligen View wird übergeben
     */
    private void deleteItem(int position) {

        S.dbHandler.removeBudget(S.db, budgetList.get(position).getId());
        budgetList.remove(position);
        bAdapter.notifyItemRemoved(position);
    }


    /**
     * ADD ITEM - Befüllung der RecyclerView mit einem Budget
     *
     * @param budget Ein Budget Objekt das hinzugefügt werden soll
     */
    public void addItem(C_Budget budget) {

        budgetList.add(budget);

        layoutManager = new LinearLayoutManager(A_Budget.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bAdapter);
        bAdapter.notifyDataSetChanged();

    }
}