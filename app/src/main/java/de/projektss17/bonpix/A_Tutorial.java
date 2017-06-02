package de.projektss17.bonpix;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import de.projektss17.bonpix.daten.C_Adapter_Tutorial;

public class A_Tutorial extends AppCompatActivity {

    ViewPager viewPager;
    C_Adapter_Tutorial customSwip;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_tutorial_content);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        customSwip = new C_Adapter_Tutorial(this);
        viewPager.setAdapter(customSwip);

        btn =(Button) findViewById(R.id.tutorial_skip_button);

        this.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                S.startActivitiy(A_Tutorial.this, A_Favoriten.class);
            }
        });
    }
}