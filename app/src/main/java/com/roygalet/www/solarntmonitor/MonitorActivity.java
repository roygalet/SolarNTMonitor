package com.roygalet.www.solarntmonitor;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import PVOutputData.PVAccountSettings;
import PVOutputData.PVSystemsCollection;

public class MonitorActivity extends AppCompatActivity {
    private String data;
//    BarChart barChart;
    PVOutputData.PVSystemsCollection nearbyCollection;
    int sid = 47892;
    String key = "4012c804abb523bf7466ef415c9ba808e8aae946";
    int postCode = 810;
    int distance = 100;
    WebView webView;
    String html="";
    private ProgressDialog progressDialog;
    TextView btnDaily;
    TextView btnMonthly;
    TextView btnYearly;
    List<String> systems;
    Spinner spinSystems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        new PVOutputConnection().execute();

        btnDaily = (TextView)findViewById(R.id.btnDaily);
        btnDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadDataWithBaseURL("",nearbyCollection.generateMyDailyData(11), "text/html", "UTF-8", "");
                btnDaily.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                btnMonthly.setTypeface(Typeface.DEFAULT);
                btnYearly.setTypeface(Typeface.DEFAULT);
            }
        });

        btnMonthly = (TextView)findViewById(R.id.btnMonthly);
        btnMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadDataWithBaseURL("",nearbyCollection.generateMyMonthlyData(13), "text/html", "UTF-8", "");
                btnMonthly.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                btnDaily.setTypeface(Typeface.DEFAULT);
                btnYearly.setTypeface(Typeface.DEFAULT);
            }
        });

        btnYearly = (TextView)findViewById(R.id.btnYearly);
        btnYearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadDataWithBaseURL("",nearbyCollection.generateMyYearlyData(5), "text/html", "UTF-8", "");
                btnYearly.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                btnMonthly.setTypeface(Typeface.DEFAULT);
                btnDaily.setTypeface(Typeface.DEFAULT);
            }
        });

        spinSystems = (Spinner) findViewById(R.id.spinSystems);
        spinSystems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                html = nearbyCollection.compareDaily(selectedItem, 10);
                webView.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    class PVOutputConnection extends AsyncTask <String, Long, PVOutputData.PVSystemsCollection>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MonitorActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Retrieving Data . . . Please Wait");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected PVSystemsCollection doInBackground(String... params) {
            nearbyCollection = new PVSystemsCollection(new PVAccountSettings(sid, key, postCode));
            nearbyCollection.getMySystem();
            nearbyCollection.getNearbySystemsWithLatestOutputs(distance,5);
            return nearbyCollection;
        }

        @Override
        protected void onPostExecute(PVSystemsCollection pvSystemsCollection) {
            super.onPostExecute(pvSystemsCollection);
            systems = new ArrayList<String>();
            for(int i=0; i<nearbyCollection.getPvSystems().size(); i++){
                systems.add((String) nearbyCollection.getPvSystems().keySet().toArray()[i]);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter <String> (MonitorActivity.this,android.R.layout.simple_spinner_item, systems);
            spinSystems.setAdapter(adapter);
//            html = nearbyCollection.compareDaily("Anula Heights", 10);
            html = nearbyCollection.generateMyYearlyData(5);
            webView.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
            progressDialog.hide();
            System.out.println(html);
        }
    }
}
