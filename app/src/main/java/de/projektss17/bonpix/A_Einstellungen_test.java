package de.projektss17.bonpix;

import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


public class A_Einstellungen_test extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main1_toolbar);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new F_Einstellungen())
                .commit();
    }
}