package com.avi.e_bill.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avi.e_bill.app.QR.IntentIntegrator;
import com.avi.e_bill.app.QR.IntentResult;


public class MainActivity extends ActionBarActivity {

    public static final String METER_ID = "com.avi.e_bill.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text = (TextView) findViewById(R.id.textView);
        Button qrbutton = (Button) findViewById(R.id.scanButton);
        Animation left = AnimationUtils.loadAnimation(this, R.anim.block_move_left);

        text.startAnimation(left);

        Animation bottom = AnimationUtils.loadAnimation(this, R.anim.block_bottom);
        qrbutton.startAnimation(bottom);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void scanMethod(View v)
    {
        Log.d("E-BILL","Button Clicked");
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            if (scanResult.getContents() != null) {

                String meter_id =  scanResult.getContents();

                Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, meter_id, Toast.LENGTH_LONG);
                    //toast.show();
                Intent detailintent = new Intent(this,DetailsActivity.class);
                detailintent.putExtra(METER_ID,meter_id);
                startActivity(detailintent);


            }
        }
    }

}
