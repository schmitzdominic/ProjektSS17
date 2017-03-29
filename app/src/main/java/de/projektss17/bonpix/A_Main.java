package de.projektss17.bonpix;

import android.animation.Animator;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.support.design.widget.NavigationView;

public class A_Main extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private boolean isFABOpen = false;
    private FloatingActionButton kameraButton, fotoButton, manuellButton;
    private LinearLayout fotoLayout, manuellLayout;
    private View fabBGLayout;

    /** Selbsterklärend. Wer noch nicht weiß was diese Methode bewirkt,
     * Bitte 2_layout.pdf lesen!
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // NavigationView (wird benötigt um Menü Items zu klicken)
        NavigationView mV = (NavigationView) findViewById(R.id.nav_menu);

        // Layout
        this.fotoLayout = (LinearLayout) findViewById(R.id.foto_button_layout);
        this.manuellLayout = (LinearLayout) findViewById(R.id.manuell_button_layout);

        // Buttons
        this.kameraButton = (FloatingActionButton) findViewById(R.id.kamera_button);
        this.fotoButton = (FloatingActionButton) findViewById(R.id.foto_button);
        this.manuellButton = (FloatingActionButton) findViewById(R.id.manuell_button);
        fabBGLayout = findViewById(R.id.fabBGLayout);

        // Klick auf den Foto Button
        fotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                S.showFoto(A_Main.this);
            }
        });

        // Klick auf den Manuell Button
        manuellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                S.showManuell(A_Main.this);
            }
        });

        // Klick auf den Kamera Button
        kameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });

        // Wenn ein Menü item (NavigationDrawer) geklickt wird
        mV.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_budget:
                        S.showBudget(A_Main.this);
                        return true;
                    case R.id.nav_gruppen:
                        S.showGruppen(A_Main.this);
                        return true;
                    case R.id.nav_favoriten:
                        S.showFavoriten(A_Main.this);
                        return true;
                    case R.id.nav_garantie:
                        S.showGarantie(A_Main.this);
                        return true;
                    case R.id.nav_einstellungen:
                        S.showEinstellungen(A_Main.this);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    /**
     * Fügt alle optionen die in menu/menu.menu_a__main.xml angegeben wurden
     * hinzu
     *
     * @param menu
     * @return true wenn alles hinzugefügt wurde
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a__main, menu);
        return true;
    }

    /**
     * Ruft Items die vorher über die onCreateOptionsMenu inflatet wurden auf.
     * Zur Info: settings button sind die drei punkte oben rechts
     *
     * @param item
     * @return true wenn die Activity aufgerufen wurde
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // Wird ausgelöst wenn der NavigationDrawer aktiviert wird
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Wird ausgelöst wenn einer der (drei punkte) Optionen aufgerufen wird
        switch (id) {
            case R.id.DUMMY:
                // Rufe die Activity auf
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Überschreibt den onBackPressed Button
     * damit sich das Floating Button Menü auch schließt.
     */
    @Override
    public void onBackPressed() {
        if (isFABOpen) {
            closeFABMenu();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Zeigt das Floating Button Menü an
     */
    private void showFABMenu() {
        isFABOpen = true;
        this.fotoLayout.setVisibility(View.VISIBLE);
        this.manuellLayout.setVisibility(View.VISIBLE);

        this.kameraButton.animate().rotationBy(90);
        this.fotoLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        this.manuellLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
    }

    /**
     * Schließt das Floating Button Menü
     */
    private void closeFABMenu() {
        isFABOpen = false;
        fabBGLayout.setVisibility(View.GONE);
        this.kameraButton.animate().rotationBy(-90);
        this.fotoButton.animate().translationY(0);
        this.manuellButton.animate().translationY(0);
        this.fotoLayout.animate().translationY(0);
        this.manuellLayout.animate().translationY(0).setListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    fotoLayout.setVisibility(View.GONE);
                    manuellLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
    }

    /**
     * Diese Klasse Managed die Tabs der Main Seite.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Gibt das Fragment Item für die jeweilige Position der Tableiste zurück
         * Zur Info: Alle Tab Klassen erben von Fragment daher können die Instanzen
         * zurück gegeben werden
         *
         * @param position int Position des Fragment Items
         * @return Fragment Instanz
         */
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Tab1Home tab1 = new Tab1Home();
                    return tab1;
                case 1:
                    Tab2Statistik tab2 = new Tab2Statistik();
                    return tab2;
                case 2:
                    Tab3Bons tab3 = new Tab3Bons();
                    return tab3;
                default:
                    return null;
            }
        }

        /**
         * Gibt an wie viele Tabs vorhanden sein sollen
         */
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        /**
         * Gibt den Page Title zurück
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.title_tab1);
                case 1:
                    return getResources().getString(R.string.title_tab2);
                case 2:
                    return getResources().getString(R.string.title_tab3);
            }
            return null;
        }
    }
}
