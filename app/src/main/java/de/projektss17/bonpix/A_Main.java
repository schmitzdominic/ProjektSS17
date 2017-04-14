package de.projektss17.bonpix;

import android.Manifest;
import android.animation.Animator;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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

import de.projektss17.bonpix.daten.C_DatabaseHandler;

public class A_Main extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private boolean isFABOpen = false;
    private FloatingActionButton kameraButton, fotoButton, manuellButton;
    private LinearLayout fotoLayout, manuellLayout;
    private View fabBGLayout;
    private String fileNameTakenPhoto;
    private Uri mCapturedImageURI;
    public ArrayList<String> picturePathList;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    /** Selbsterklärend. Wer noch nicht weiß was diese Methode bewirkt,
     * Bitte 2_layout.pdf lesen!
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_main_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        requestPermissions();

        // Set Default Preferences (Important if app was started the first time)
        PreferenceManager.setDefaultValues(this, R.xml.box_einstellungen_preferences, false);

        // Database Section
        // Values
        final String KEY_TEST = "bons_name";
        String testInsert = "testContent";

        // DataBase Connection
        S.dbHandler = new C_DatabaseHandler(this);
        S.db = S.dbHandler.getWritableDatabase();

        // Insert Examples
        ContentValues values = new ContentValues();
        values.put(KEY_TEST, testInsert);
        S.db.insert("bons", null, values);

        // Test Functions
        S.dbHandler.checkTables(S.db);
        S.dbHandler.setLaeden(S.db, "Aldi");
        S.dbHandler.setLaeden(S.db, "Lidl");
        S.dbHandler.deleteLaden(S.db, 1);
        // END --- Database Section

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.main_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        picturePathList = new ArrayList<>();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // NavigationView (wird benötigt um Menü Items zu klicken)
        NavigationView mV = (NavigationView) findViewById(R.id.main_nav_menu);

        // Layout
        this.fotoLayout = (LinearLayout) findViewById(R.id.main_foto_button_layout);
        this.manuellLayout = (LinearLayout) findViewById(R.id.main_manuell_button_layout);

        // Buttons
        this.kameraButton = (FloatingActionButton) findViewById(R.id.main_kamera_button);
        this.fotoButton = (FloatingActionButton) findViewById(R.id.main_foto_button);
        this.manuellButton = (FloatingActionButton) findViewById(R.id.main_manuell_button);
        fabBGLayout = findViewById(R.id.main_fabBGLayout);

        // Klick auf den Foto Button
        fotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeTakePhoto();
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
                    case R.id.menu_nav_budget:
                        S.showBudget(A_Main.this);
                        return true;
                    case R.id.menu_nav_gruppen:
                        S.showGruppen(A_Main.this);
                        return true;
                    case R.id.menu_nav_favoriten:
                        S.showFavoriten(A_Main.this);
                        return true;
                    case R.id.menu_nav_garantie:
                        S.showGarantie(A_Main.this);
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
     * Fügt alle optionen die in menu/menu.menu_mainl angegeben wurden
     * hinzu
     *
     * @param menu
     * @return true wenn alles hinzugefügt wurde
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.menu_main_DUMMY:
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
    public void requestPermissions(){
        ActivityCompat.requestPermissions(A_Main.this,
                new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
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
                } else {
                    Toast.makeText(A_Main.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    /**
     * Ruft die Standard Android Kamera Anwendung auf
     */
    public void activeTakePhoto() {

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
            S.showRecognition(A_Main.this,picturePathList);
        }
    }
}

