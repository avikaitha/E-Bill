package com.avi.e_bill.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class DetailsActivity extends Activity {
    private String meter_id;
    TextView registeredTV,phonenoTV,addressTV,meter_idTV,unitsTV,head;
    Button payButton,historyButton;
    public static final String METER_ID = "com.avi.e_bill.app.DetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        meter_id = intent.getStringExtra(MainActivity.METER_ID);
        head = (TextView) findViewById(R.id.textView);
        registeredTV = (TextView) findViewById(R.id.registeredtextView);
        phonenoTV = (TextView) findViewById(R.id.phonenotextView);
        addressTV = (TextView) findViewById(R.id.addresstextView);
        meter_idTV = (TextView) findViewById(R.id.metertextView);
        unitsTV = (TextView) findViewById(R.id.unitstextview);
        payButton = (Button) findViewById(R.id.paybutton);
        historyButton = (Button) findViewById(R.id.historybutton);

        Animation left = AnimationUtils.loadAnimation(this, R.anim.block_left_details);
        Animation left1 = AnimationUtils.loadAnimation(this, R.anim.block_left_details_1);
        Animation left2 = AnimationUtils.loadAnimation(this, R.anim.block_left_details_2);
        Animation left3 = AnimationUtils.loadAnimation(this, R.anim.block_left_details_3);
        Animation left4 = AnimationUtils.loadAnimation(this, R.anim.block_left_details_4);
        Animation left5 = AnimationUtils.loadAnimation(this, R.anim.block_left_details_5);
        head.startAnimation(left);
        registeredTV.startAnimation(left1);
        phonenoTV.startAnimation(left2);
        addressTV.startAnimation(left3);
        meter_idTV.startAnimation(left4);
        unitsTV.startAnimation(left5);
        new GetDetails().execute(meter_id);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    public void historyMethod(View v)
    {
        Intent intent = new Intent(this,HistoryActivity.class);
        intent.putExtra(METER_ID,meter_id);
        startActivity(intent);
    }

    public void payMethod(View v)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mpcz.co.in"));
       // intent.putExtra(METER_ID,meter_id);
        startActivity(intent);
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


    class GetDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = "";
            InputStream isr = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.43.182/e_bill/getmeterdetails.php");
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("meter_id", params[0]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                isr = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error at httpost " + e.toString());
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                isr.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e("log_tag", "Error converting " + e.toString());
            }
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {

                JSONArray jArray = new JSONArray(result);
                String registeredStr = "", phonenoStr = "", addresssStr = "", meter_idStr = "",units="";

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);

                    registeredStr = json.getString("Name");
                    phonenoStr = json.getString("PhoneNo");
                    addresssStr = json.getString("Address");

                    meter_idStr = json.getString("modelno");
                    units = json.getString("load_value");

                    Log.d("Name",registeredStr);


                }



                registeredTV.setText("Registered To: " + registeredStr);
                phonenoTV.setText("Phone No: " + phonenoStr);
                addressTV.setText("Address:\n" + addresssStr);
                meter_idTV.setText("Meter Id: " + meter_idStr);
                unitsTV.setText("Units: " + units);

            } catch (Exception e) {

                Log.e("log_tag", "Error Parsing Data" + e.toString());
            }


        }
    }

}
