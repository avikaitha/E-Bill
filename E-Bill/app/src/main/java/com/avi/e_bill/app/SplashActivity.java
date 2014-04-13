package com.avi.e_bill.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


public class SplashActivity extends ActionBarActivity {

    TextView block1 ;
    TextView block2;
    TextView block3;
    Animation drop;

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
         block1 = (TextView) findViewById(R.id.block_1);
         block2 = (TextView) findViewById(R.id.block_2);
         block3 = (TextView) findViewById(R.id.block_3);
        drop = AnimationUtils.loadAnimation(this, R.anim.block_drop);

        block1.startAnimation(drop);

        block2.startAnimation(drop);

        block3.startAnimation(drop);




        Thread timer = new Thread(){

            @Override
            public void run() {

                // TODO Auto-generated method stub
                super.run();
                try{


                    sleep(3100);
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
            }

        };
        timer.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
