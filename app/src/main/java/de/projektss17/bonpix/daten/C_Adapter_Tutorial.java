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
import android.widget.ProgressBar;
import android.widget.TextView;

import de.projektss17.bonpix.A_Tutorial;
import de.projektss17.bonpix.R;

import static de.projektss17.bonpix.R.id.textView;


public class C_Adapter_Tutorial extends PagerAdapter {
    private int [] imageResources ={R.drawable.picture1,R.drawable.picture2,R.drawable.picture3,R.drawable.picture4,R.drawable.picture5,R.drawable.picture6,R.drawable.picture7,R.drawable.picture7};
    private Context ctx;
    private LayoutInflater layoutInflater;
    private TextView content;
    private ImageView imageView;
    private ProgressBar progressBar;

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
            this.imageView=(ImageView) itemView.findViewById(R.id.swip_image_view);
            this.content = (TextView) itemView.findViewById(R.id.tutorial_text_content);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.tutorial_imageCount);

            this.imageView.setImageResource(imageResources[position]);

            switch (position){
                case 0:
                    this.content.setText(ctx.getResources().getString((R.string.tutorial_text1)));
                    this.progressBar.setProgress(position+1);   // Progress wird um 1 erhöht
                    break;
                case 1:
                    this.content.setText(ctx.getResources().getString((R.string.tutorial_text2)));
                    this.progressBar.setProgress(position+1);
                    break;
                case 2:
                    this.content.setText(ctx.getResources().getString((R.string.tutorial_text3)));
                    this.progressBar.setProgress(position+1);
                    break;
                case 3:
                    this.content.setText(ctx.getResources().getString((R.string.tutorial_text4)));
                    this.progressBar.setProgress(position+1);
                    break;
                case 4:
                    this.content.setText(ctx.getResources().getString((R.string.tutorial_text5)));
                    this.progressBar.setProgress(position+1);
                    break;
                case 5:
                    this.content.setText(ctx.getResources().getString((R.string.tutorial_text6)));
                    this.progressBar.setProgress(position+1);
                    break;
                case 6:
                    this.content.setText(ctx.getResources().getString((R.string.tutorial_text7)));
                    this.progressBar.setProgress(position+1);
                    break;
            }


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

