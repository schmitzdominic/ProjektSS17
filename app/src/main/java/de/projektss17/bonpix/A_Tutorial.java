package de.projektss17.bonpix;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import de.projektss17.bonpix.daten.C_Adapter_Tutorial;

public class A_Tutorial extends Activity {

    private ViewPager viewPager;
    private C_Adapter_Tutorial customSwip;
    private Button skipButton;
    private int slideCount = 8;     // Anzahl der Slides

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_tutorial_content);

        this.viewPager=(ViewPager)findViewById(R.id.viewPager);
        this.customSwip = new C_Adapter_Tutorial(this, slideCount);
        this.viewPager.setAdapter(customSwip);

        this.skipButton =(Button) findViewById(R.id.tutorial_skip_button);
        this.skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int state) {}

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if(position == slideCount){
                    finish();
                }
            }
        });
    }
}