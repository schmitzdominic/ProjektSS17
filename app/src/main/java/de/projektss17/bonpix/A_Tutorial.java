package de.projektss17.bonpix;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import de.projektss17.bonpix.adapter.C_Adapter_Tutorial;

public class A_Tutorial extends Activity {

    ViewPager viewPager;
    C_Adapter_Tutorial customSwip;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_tutorial_content);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        customSwip = new C_Adapter_Tutorial(this);
        viewPager.setAdapter(customSwip);

        btn = (Button) findViewById(R.id.tutorial_skip_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int state) {}

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if(position == 7){
                    finish();
                }
            }
        });
    }
}