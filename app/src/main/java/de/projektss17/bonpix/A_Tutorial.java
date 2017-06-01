package de.projektss17.bonpix;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Message;
import android.widget.TextView;


public class A_Tutorial extends AppCompatActivity {

    ImageView slide;
    TextView msg;
    Button btn;

    int i=0;
    int imgid[]={R.drawable.box_budget_circle_shape};

    RefreshHandler refreshHandler=new RefreshHandler();

    @SuppressLint("HandlerLeak")
    class RefreshHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            A_Tutorial.this.updateUI();
        }
        public void sleep(long delayMillis){
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };
    public void updateUI(){
        int currentInt=Integer.parseInt((String)msg.getText())+10;
        if(currentInt<=100){
            refreshHandler.sleep(2000);
            msg.setText(String.valueOf(currentInt));
            if(i<imgid.length){
                slide.setImageResource(imgid[i]);

                // slide.setPadding(left, top, right, bottom);
                i++;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_tutorial_content);
        slide = (ImageView) findViewById(R.id.slideshow);
        msg = (TextView) findViewById(R.id.message);

        btn =(Button) findViewById(R.id.tutorial_skip_button);

        this.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                S.startActivitiy(A_Tutorial.this, A_Favoriten.class);
            }
        });

    }

}
