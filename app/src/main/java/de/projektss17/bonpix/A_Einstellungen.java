package de.projektss17.bonpix;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

public class A_Einstellungen extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_einstellungen_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Scopes
        Switch onOffSwitch = (Switch)  findViewById(R.id.switch1);
        ImageButton versionButton = (ImageButton) findViewById(R.id.einstellungen_version_button);

        // Listener
        versionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                S.showVersion(A_Einstellungen.this);
            }
        });
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("Switch State: ", " ### " + isChecked);

            }
        });

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean switchStat = settings.getBoolean("switch", false);
        onOffSwitch.setChecked(switchStat);
    }

    @Override
    protected void onStop(){
        super.onStop();

        // Save preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        Switch onOffSwitch = (Switch)  findViewById(R.id.switch1);
        editor.putBoolean("switch", onOffSwitch.isChecked());
        editor.apply();
    }
}