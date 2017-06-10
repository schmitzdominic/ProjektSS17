package de.projektss17.bonpix.daten;

/**
 * Created by Daniel on 02.06.2017.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.projektss17.bonpix.A_Tutorial;
import de.projektss17.bonpix.R;


public class C_Adapter_Tutorial extends PagerAdapter {
    private int [] imageResources ={R.drawable.picture1,R.drawable.picture2,R.drawable.picture3,R.drawable.picture4,R.drawable.picture5,R.drawable.picture6,R.drawable.picture7,R.drawable.picture7};
    private Context ctx;
    private LayoutInflater layoutInflater;

    public C_Adapter_Tutorial(Context c) {
        ctx=c;

    }

    @Override
    public int getCount() {
        return imageResources.length;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater= (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=layoutInflater.inflate(R.layout.box_tutorial_adapter,container,false);

        if(position < imageResources.length - 1){
            ImageView imageView=(ImageView) itemView.findViewById(R.id.swip_image_view);
            TextView textView=(TextView) itemView.findViewById(R.id.imageCount);
            imageView.setImageResource(imageResources[position]);
            textView.setText(ctx.getResources().getString(R.string.tutorial_tipp)+(position+1));
            container.addView(itemView);
        }

        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return  (view==object);
    }
}

