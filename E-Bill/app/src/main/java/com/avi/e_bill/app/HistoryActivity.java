package com.avi.e_bill.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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


public class HistoryActivity extends Activity {

    String meter_id;

    TextView dateTV,unitTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        dateTV = (TextView) findViewById(R.id.datetextView);
        unitTV = (TextView) findViewById(R.id.loadtextView);
        Intent intent = getIntent();
        meter_id = intent.getStringExtra(DetailsActivity.METER_ID);
        new GetDetails().execute(meter_id);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history, menu);
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

    class GetDetails extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = "";
            InputStream isr = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.43.182/e_bill/historydetails.php");
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

            ArrayList<String> units = new ArrayList<String>();
            ArrayList<String> date = new ArrayList<String>();
            try {

                JSONArray jArray = new JSONArray(result);

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);

                    date.add(json.getString("date"));
                    units.add(json.getString("load_value"));
                    Log.d("Units:", ""+jArray.length());
                }


                String totdate = "",totunit="";
                for (int i = 0; i < jArray.length(); i++) {

                    totdate = totdate + date.get(i)+"\n";
                    totunit = totunit + units.get(i)+"\n";


                }
                dateTV.setText(totdate);
                unitTV.setText(totunit);




            } catch (Exception e) {

                e.printStackTrace();
                Log.e("log_tag", "Error Parsing Data" + e.toString());
            }


        }
    }

}
