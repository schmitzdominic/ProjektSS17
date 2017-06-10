package de.projektss17.bonpix;

import android.Manifest;
import android.animation.Animator;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.support.design.widget.NavigationView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import de.projektss17.bonpix.daten.C_Artikel;
import de.projektss17.bonpix.daten.C_AssetHelper;
import de.projektss17.bonpix.daten.C_Bon;
import de.projektss17.bonpix.daten.C_Budget;
import de.projektss17.bonpix.daten.C_DatabaseHandler;
import de.projektss17.bonpix.daten.C_Laden;
import de.projektss17.bonpix.daten.C_Preferences;

public class A_Main extends AppCompatActivity {

    // Primitive Datentypen
    private boolean isFABOpen = false;
    private boolean isDrawOpen = false;
    private boolean cameraPermissions;
    private String fileNameTakenPhoto;
    public ArrayList<String> picturePathList;

    // Layout
    private ActionBarDrawerToggle toggle;
    private DrawerLayout navigationDrawerLayout;
    private FloatingActionButton plusButton;
    private FloatingActionButton fotoButton;
    private FloatingActionButton manuellButton;
    private LinearLayout fotoLayout;
    private LinearLayout manuellLayout;
    private NavigationView navigationView;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private TabLayout tabLayout;
    private TabLayout tabChooseTime;
    private AppBarLayout tabChooseTimeLayout;
    private Toolbar toolbar;
    private Uri mCapturedImageURI;
    private View fabBGLayout;
    private ViewPager viewPager;

    // Statischer Bereich
    private static final int REQUEST_IMAGE_CAPTURE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_main_screen);
        this.toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Navigation Drawer
        this.navigationDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        this.toggle = new ActionBarDrawerToggle(this, this.navigationDrawerLayout, R.string.open, R.string.close){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                isDrawOpen = false;
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isDrawOpen = true;
            }
        };

        // Layout
        this.viewPager = (ViewPager) findViewById(R.id.main_container);
        this.tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        this.tabChooseTime = (TabLayout) findViewById(R.id.statistik_tabs);
        this.tabChooseTimeLayout = (AppBarLayout) findViewById(R.id.main_time_bar_layout);
        this.fotoLayout = (LinearLayout) findViewById(R.id.main_foto_button_layout);
        this.manuellLayout = (LinearLayout) findViewById(R.id.main_manuell_button_layout);
        this.fotoButton = (FloatingActionButton) findViewById(R.id.main_foto_button);
        this.plusButton = (FloatingActionButton) findViewById(R.id.main_kamera_button);
        this.manuellButton = (FloatingActionButton) findViewById(R.id.main_manuell_button);
        this.navigationView = (NavigationView) findViewById(R.id.main_nav_menu);
        this.fabBGLayout = findViewById(R.id.main_fabBGLayout);

        // Berechtigungen
        this.requestPermissions(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE});

        // Instanziierungen und Konfigurationen
        this.toggle.syncState();
        this.tabChooseTimeLayout.setVisibility(View.INVISIBLE);
        this.picturePathList = new ArrayList<>();
        this.navigationDrawerLayout.addDrawerListener(toggle);
        this.sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        this.viewPager.setAdapter(sectionsPagerAdapter);
        this.tabLayout.setupWithViewPager(viewPager);

        this.initPersistence();
        this.initOnClickListener();
        this.onFirstStart();

    }

    /**
     * Initialisiert alle OnClickListener
     */
    private void initOnClickListener(){

        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 1){
                    tabChooseTimeLayout.setVisibility(View.VISIBLE);
                } else {
                    tabChooseTimeLayout.setVisibility(View.INVISIBLE);
                }

                if (!plusButton.isShown()) {
                    plusButton.show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {/* MÜSSEN LEIDER mit implementiert werden, machen jedoch nichts! */}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {/* MÜSSEN LEIDER mit implementiert werden, machen jedoch nichts! */}
        });

        this.manuellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
                S.showManuell(A_Main.this,"new");
            }
        });

        this.fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });

        this.fotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeTakePhoto();
            }
        });

        this.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        // Wenn ein Menüitem (NavigationDrawer) geklickt wird
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.menu_nav_budget:
                        S.showBudget(A_Main.this);
                        return true;
                    /*case R.id.menu_nav_gruppen:
                        S.showGruppen(A_Main.this);
                        return true;*/
                    case R.id.menu_nav_favoriten:
                        S.showFavoriten(A_Main.this);
                        return true;
                    case R.id.menu_nav_garantie:
                        S.showGarantie(A_Main.this);
                        return true;
                    case R.id.menu_nav_laeden:
                        S.showLaeden(A_Main.this);
                        return true;
                    case R.id.menu_nav_einstellungen:
                        S.showEinstellungen(A_Main.this);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    /**
     * Initialisiert die Datenpersisitenz
     * WICHTIG beim starten der App
     */
    private void initPersistence(){
        S.dbHandler = new C_DatabaseHandler(this);
        S.dbArtikelHandler = new C_AssetHelper(this);
        S.db = S.dbHandler.getWritableDatabase();
        S.dbArtikel = S.dbArtikelHandler.getWritableDatabase();
        S.dbHandler.checkTables(S.db);
        S.dbHandler.showLogAllDBEntries();
        S.prefs = new C_Preferences(A_Main.this);
    }

    /**
     * Gibt die Time Leiste zurück
     * @return Time Leiste
     */
    public TabLayout getTimeTabLayout(){
        return this.tabChooseTime;
    }

    /**
     * Gibt den atkuellen Status des FAB Menüs zurück
     * @return boolean
     */
    public boolean getFabState(){
        return this.isFABOpen;
    }

    /**
     * Gibt den FAB zurück
     * @return FloatingActionButton
     */
    public FloatingActionButton getFloatingActionButtonPlus(){
        return this.plusButton;
    }

    /**
     * Fügt alle optionen die in menu/menu.menu_mainl angegeben wurden
     * hinzu
     *
     * @param menu
     * @return true wenn alles hinzugefügt wurde
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

        // Wird ausgelöst wenn der NavigationDrawer aktiviert wird
        if (toggle.onOptionsItemSelected(item)) {
            this.isDrawOpen = true;
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

        if (isDrawOpen) {
            navigationDrawerLayout.closeDrawers();
        }else if (isFABOpen) {
            closeFABMenu();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Zeigt das Floating Button Menü an
     */
    public void showFABMenu() {
        isFABOpen = true;
        this.fotoLayout.setVisibility(View.VISIBLE);
        this.manuellLayout.setVisibility(View.VISIBLE);

        this.plusButton.animate().rotationBy(90);
        this.fotoLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        this.manuellLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
    }

    /**
     * Schließt das Floating Button Menü
     */
    public void closeFABMenu() {
        isFABOpen = false;
        fabBGLayout.setVisibility(View.GONE);
        this.plusButton.animate().rotationBy(-90);
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
                    A_Tab1Home tab1 = new A_Tab1Home();
                    return tab1;
                case 1:
                    A_Tab2Statistik tab2 = new A_Tab2Statistik();
                    return tab2;
                case 2:
                    A_Tab3Bons tab3 = new A_Tab3Bons();
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

    /**
     * Prüft ob die benötigten Permissions vorhanden sind
     */
    public void requestPermissions(String[] permissionRequest){
        ActivityCompat.requestPermissions(A_Main.this,
                permissionRequest,
                1);
    }

    /**
     * Frägt nach den noch benötigten Permissions
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraPermissions = true;
                } else {
                    Toast.makeText(A_Main.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                    cameraPermissions = false;
                }
                return;
            }
        }
    }

    /**
     * Ruft die Standard Android Kamera Anwendung auf
     */
    public void activeTakePhoto() {

        this.closeFABMenu();

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Date date = Calendar.getInstance().getTime();
            DateFormat formatter = new SimpleDateFormat("ddMMyyyyHH:mm");
            String today = formatter.format(date);
            fileNameTakenPhoto = today + ".jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileNameTakenPhoto);
            mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            takePictureIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT,5582912L);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Nachdem ein Bild geschossen wurde, wird die S.showRecognition aufgerufen und
     * der Pfad zu dem eben geschossenen Bild übergeben.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(mCapturedImageURI, projection, null, null, null);
            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String picturePath = cursor.getString(column_index_data);
            picturePathList.add(picturePath);
            S.showManuell(A_Main.this, picturePathList, "foto");
        }
    }

    /**
     * Wird nur beim ersten Start der App ausgeführt
     */
    private void onFirstStart(){
        if (S.prefs.getPrefBoolean("first_time")) {
            this.setDefaultSettings();
            this.setDefaultDBValues();
            //this.createDBDummyData(20);

            // Zurücksetzen um zu gewährleisten das es nicht mehr ausgeführt wird.
            S.prefs.savePrefBoolean("first_time", false);
        }
    }

    /**
     * Setzt alle standard DB Werte
     */
    private void setDefaultDBValues(){

        String [] defaultLaeden = this.getResources().getStringArray(R.array.defaultLaeden);

        for(String laden : defaultLaeden){
            S.dbHandler.addLaden(S.db, new C_Laden(laden));
        }
    }

    /**
     * Setzt alle Standardwerte
     */
    private void setDefaultSettings(){
        PreferenceManager.setDefaultValues(this, R.xml.box_einstellungen_preferences, true);
        PreferenceManager.setDefaultValues(this, R.xml.box_backup_preferences, false);
    }

    /**
     * Erstellt Dummy Daten
     * @param value Wie viele Daten generiert werden
     */
    private void createDBDummyData(int value){

        ArrayList<C_Artikel> artikelList = new ArrayList<>();
        ArrayList<C_Laden> ladenList = new ArrayList<>();
        Random ran = new Random();
        int x = 0;

        for(int laden = 0; laden < value; laden++){
            ladenList.add(new C_Laden("Laden_"+laden));
        }

        for(int i = 0; i < value; i++){
            x = 3 + ran.nextInt((10 - 3) + 1);

            for(int dummyArtikel = 0; dummyArtikel < x; dummyArtikel++){
                artikelList.add(new C_Artikel(""+i+dummyArtikel, i*dummyArtikel));
            }
            Log.e("### CEATE DUMMY DATA", " "+(i+1)+" OF "+value);
            if(i < 10){
                S.dbHandler.addBon(S.db, new C_Bon("PFAD", ladenList.get(i).getName(), "TestAnschrift", "SONSTIGES", "0"+i+".10.2016", "0"+i+".10.2018", "1337", true, true, artikelList));
            } else {
                S.dbHandler.addBon(S.db, new C_Bon("PFAD", ladenList.get(i).getName(), "TestAnschrift", "SONSTIGES", i+".10.2016", i+".10.2018", "1337", true, true, artikelList));
            }
            artikelList.clear();
        }

        boolean isFavorite = true;

        for(int i = 1; i < 3; i++){
            S.dbHandler.addBudget(S.db, new C_Budget(i*1000, i*100, "0"+i+".01.2020", "0"+i+".01.2020", "TESTBUDGET"+i, "LALALALA", isFavorite, S.dbHandler.getNumberOfNewestBons(S.db, i)));
            isFavorite = false;
        }
    }
}